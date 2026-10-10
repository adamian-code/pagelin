package io.github.adamian.pagelin.pdf

import android.graphics.pdf.PdfDocument
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.withSave
import io.github.adamian.pagelin.pdf.PdfPageConfiguration.BASE_DPI
import io.github.adamian.pagelin.pdf.PdfPageConfiguration.OUTPUT_DPI
import io.github.adamian.pagelin.pdf.PdfPageConfiguration.RENDER_DPI
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.io.OutputStream
import kotlin.coroutines.resume

internal class PdfGenerator(
    private val activity: ComponentActivity
) {

    @Suppress("LongParameterList")
    suspend fun generate(
        outputStream: OutputStream,
        margin: Dp,
        timeoutMs: Long,
        pages: List<@Composable () -> Unit>,
        theme: @Composable (@Composable () -> Unit) -> Unit
    ): Result<Unit> = withContext(Dispatchers.Main) {

        val pageMetrics = createPdfPageMetrics(margin)

        val pageWidth = pageMetrics.pageWidth
        val pageHeight = pageMetrics.pageHeight
        val contentWidth = pageMetrics.contentWidth
        val contentHeight = pageMetrics.contentHeight
        val marginPx = pageMetrics.marginPx

        val pdfDensity = object : Density {
            override val density: Float =
                RENDER_DPI / BASE_DPI.toFloat()

            override val fontScale: Float = 1f
        }

        val outputDocumentDensity = RENDER_DPI / OUTPUT_DPI

        outputStream.use { out ->
            val pdfDocument = PdfDocument()

            try {
                pages.forEachIndexed { index, pageContent ->

                    val contentReady = CompletableDeferred<Unit>()

                    val pageInfo = PdfDocument.PageInfo.Builder(
                        (pageWidth / outputDocumentDensity).toInt(),
                        (pageHeight / outputDocumentDensity).toInt(),
                        index + 1
                    ).create()

                    val page = pdfDocument.startPage(pageInfo)

                    val composeView = ComposeView(activity)

                    val rootLayout =
                        activity.window.decorView as ViewGroup

                    try {
                        composeView.apply {
                            alpha = 0f

                            setViewCompositionStrategy(
                                ViewCompositionStrategy.DisposeOnDetachedFromWindow
                            )

                            setContent {
                                theme {
                                    CompositionLocalProvider(
                                        LocalDensity provides pdfDensity
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(
                                                    width = contentWidth.dp,
                                                    height = contentHeight.dp
                                                )
                                                .onGloballyPositioned {
                                                    if (!contentReady.isCompleted) {
                                                        contentReady.complete(Unit)
                                                    }
                                                }
                                        ) {
                                            pageContent()
                                        }
                                    }
                                }
                            }
                        }

                        rootLayout.addView(
                            composeView,
                            ViewGroup.LayoutParams(
                                contentWidth,
                                contentHeight
                            )
                        )

                        withTimeout(timeoutMs) {
                            contentReady.await()
                        }

                        waitForNextFrame(composeView)

                        composeView.measure(
                            View.MeasureSpec.makeMeasureSpec(
                                contentWidth,
                                View.MeasureSpec.EXACTLY
                            ),
                            View.MeasureSpec.makeMeasureSpec(
                                contentHeight,
                                View.MeasureSpec.EXACTLY
                            )
                        )

                        composeView.layout(
                            0,
                            0,
                            contentWidth,
                            contentHeight
                        )

                        waitForDrawReady(composeView)

                        page.canvas.withSave {
                            scale(
                                1f / outputDocumentDensity,
                                1f / outputDocumentDensity
                            )

                            translate(
                                marginPx.toFloat(),
                                marginPx.toFloat()
                            )

                            composeView.draw(this)
                        }

                    } finally {
                        if (composeView.parent === rootLayout) {
                            rootLayout.removeView(composeView)
                        }

                        pdfDocument.finishPage(page)
                    }
                }

                withContext(Dispatchers.IO) {
                    pdfDocument.writeTo(out)
                    out.flush()
                }

                Result.success(Unit)

            } catch (e: Exception) {
                Result.failure(e)

            } finally {
                pdfDocument.close()
            }
        }
    }

    private suspend fun waitForNextFrame(
        view: View
    ) = suspendCancellableCoroutine { continuation ->

        view.post {
            if (continuation.isActive) {
                continuation.resume(Unit)
            }
        }
    }

    private suspend fun waitForDrawReady(
        view: View
    ) = suspendCancellableCoroutine { continuation ->

        val listener = object : ViewTreeObserver.OnPreDrawListener {

            override fun onPreDraw(): Boolean {
                removePreDrawListener(
                    view = view,
                    listener = this
                )

                if (continuation.isActive) {
                    continuation.resume(Unit)
                }

                return true
            }
        }

        view.viewTreeObserver.addOnPreDrawListener(listener)

        continuation.invokeOnCancellation {
            view.post {
                removePreDrawListener(
                    view = view,
                    listener = listener
                )
            }
        }

        view.post {
            if (
                continuation.isActive &&
                view.width > 0 &&
                view.height > 0 &&
                view.isAttachedToWindow
            ) {
                removePreDrawListener(
                    view = view,
                    listener = listener
                )

                continuation.resume(Unit)
            }
        }
    }

    private fun removePreDrawListener(
        view: View,
        listener: ViewTreeObserver.OnPreDrawListener
    ) {
        val observer = view.viewTreeObserver

        if (observer.isAlive) {
            observer.removeOnPreDrawListener(listener)
        }
    }
}