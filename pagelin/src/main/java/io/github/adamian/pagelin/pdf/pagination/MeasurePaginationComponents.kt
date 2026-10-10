package io.github.adamian.pagelin.pdf.pagination

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.SubcomposeMeasureScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import io.github.adamian.pagelin.pdf.PdfPageConfiguration
import io.github.adamian.pagelin.pdf.createPdfPageMetrics

@Composable
internal fun <T> MeasurePaginationComponents(
    firstPageContent: @Composable () -> Unit,
    items: List<T>,
    itemContent: @Composable (T) -> Unit,
    onMeasured: (PaginationInput<T>) -> Unit
) {
    val renderDensity = Density(
        density =
            PdfPageConfiguration.RENDER_DPI /
                    PdfPageConfiguration.BASE_DPI.toFloat(),
        fontScale = 1f
    )

    CompositionLocalProvider(
        LocalDensity provides renderDensity
    ) {
        SubcomposeLayout {

            val pageMetrics = createPdfPageMetrics()

            val constraints = Constraints(
                minWidth = pageMetrics.contentWidth,
                maxWidth = pageMetrics.contentWidth,
                minHeight = 0,
                maxHeight = pageMetrics.contentHeight
            )

            val firstPageReservedHeight =
                measureComponent(
                    slotId = "first_page_content",
                    constraints = constraints,
                    content = firstPageContent
                )

            val measuredItems =
                items.mapIndexed { index, item ->

                    val height =
                        measureComponent(
                            slotId = "item_$index",
                            constraints = constraints
                        ) {
                            itemContent(item)
                        }

                    MeasuredItem(
                        item = item,
                        height = height
                    )
                }

            onMeasured(
                PaginationInput(
                    firstPageReservedHeight = firstPageReservedHeight,
                    measuredItems = measuredItems
                )
            )

            layout(0, 0) {}
        }
    }
}

private fun SubcomposeMeasureScope.measureComponent(
    slotId: String,
    constraints: Constraints,
    content: @Composable () -> Unit
): Int {
    return subcompose(
        slotId = slotId,
        content = content
    )
        .map {
            it.measure(constraints)
        }
        .maxOfOrNull {
            it.height
        } ?: 0
}