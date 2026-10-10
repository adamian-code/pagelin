package io.github.adamian.pagelin.pdf

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class PdfConfig(
    val fileName: String,
    val marginMm: Float = DEFAULT_MARGIN_MM
) {
    companion object {
        const val DEFAULT_MARGIN_MM = 10f
    }
}

internal object PdfPageConfiguration {

    const val BASE_DPI = 160
    const val RENDER_DPI = 300
    const val OUTPUT_DPI = 150f

    const val A4_HEIGHT_MM = 297f
    const val A4_WIDTH_MM = 210f
    const val MM_PER_INCH = 25.4f

    fun millimetersToPixels(value: Float): Int =
        (value / MM_PER_INCH * RENDER_DPI).toInt()
}

internal data class PdfPageMetrics(
    val pageWidth: Int,
    val pageHeight: Int,
    val contentWidth: Int,
    val contentHeight: Int,
    val marginPx: Int
)

internal fun createPdfPageMetrics(
    margin: Dp = 10.dp
): PdfPageMetrics {

    val pageWidth = PdfPageConfiguration.millimetersToPixels(
        value = PdfPageConfiguration.A4_WIDTH_MM
    )

    val pageHeight = PdfPageConfiguration.millimetersToPixels(
        value = PdfPageConfiguration.A4_HEIGHT_MM
    )

    val marginPx = margin.value.toInt()

    return PdfPageMetrics(
        pageWidth = pageWidth,
        pageHeight = pageHeight,
        contentWidth = pageWidth - marginPx * 2,
        contentHeight = pageHeight - marginPx * 2,
        marginPx = marginPx
    )
}