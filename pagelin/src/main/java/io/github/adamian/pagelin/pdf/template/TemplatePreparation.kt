package io.github.adamian.pagelin.pdf.template

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.resume

internal suspend fun prepareTemplatePages(
    activity: ComponentActivity,
    template: PreparedPdfTemplate,
    timeoutMs: Long = 500,
    theme: @Composable (@Composable () -> Unit) -> Unit = { content -> content() }
): List<@Composable () -> Unit> {

    val rootLayout =
        activity.window.decorView as? ViewGroup
            ?: throw IllegalStateException("Activity decorView must be a ViewGroup")

    val pagesDeferred =
        CompletableDeferred<List<@Composable () -> Unit>>()

    val composeView =
        ComposeView(activity)

    try {
        composeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnDetachedFromWindow
            )

            setContent {
                theme {
                    val pages =
                        template.buildPages()

                    LaunchedEffect(pages) {
                        if (pages.isNotEmpty() && !pagesDeferred.isCompleted) {
                            pagesDeferred.complete(pages)
                        }
                    }
                }
            }
        }

        composeView.alpha = 0f

        rootLayout.addView(
            composeView,
            LayoutParams(1, 1)
        )

        waitForNextFrame(composeView)

        return withTimeout(timeoutMs) {
            pagesDeferred.await()
        }
    } finally {
        rootLayout.removeView(composeView)
    }
}

private suspend fun waitForNextFrame(
    view: View
) = suspendCancellableCoroutine { continuation ->
    view.post {
        continuation.resume(Unit)
    }
}
