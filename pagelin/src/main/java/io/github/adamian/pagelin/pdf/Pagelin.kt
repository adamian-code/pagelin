package io.github.adamian.pagelin.pdf

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import io.github.adamian.pagelin.pdf.template.PdfTemplate
import io.github.adamian.pagelin.pdf.template.PreparedPdfTemplate
import io.github.adamian.pagelin.pdf.template.prepareTemplatePages
import java.io.File
import java.io.FileOutputStream

object Pagelin {
    private const val DEFAULT_TIMEOUT = 500L
    suspend fun generate(
        activity: ComponentActivity,
        config: PdfConfig,
        pages: List<@Composable () -> Unit>,
        theme: @Composable (@Composable () -> Unit) -> Unit = { content -> content() }
    ): Result<GeneratedPdf> {

        val file = File(
            activity.getExternalFilesDir(null),
            config.fileName
        )

        val result = FileOutputStream(file).use { outputStream ->
            PdfGenerator(activity).generate(
                outputStream = outputStream,
                margin = config.marginMm.dp,
                timeoutMs = DEFAULT_TIMEOUT,
                pages = pages,
                theme = theme
            )
        }

        return if (result.isSuccess) {
            Result.success(
                GeneratedPdf(file)
            )
        } else {
            Result.failure(
                result.exceptionOrNull()
                    ?: IllegalStateException("PDF generation failed")
            )
        }
    }

    suspend fun generate(
        activity: ComponentActivity,
        config: PdfConfig,
        template: PdfTemplate,
        theme: @Composable (@Composable () -> Unit) -> Unit = { content -> content() }
    ): Result<GeneratedPdf> {

        val pages =
            if (template is PreparedPdfTemplate) {
                prepareTemplatePages(
                    activity = activity,
                    template = template,
                    theme = theme
                )
            } else {
                template.pages
            }

        return generate(
            activity = activity,
            config = config,
            pages = pages,
            theme = theme
        )
    }
}