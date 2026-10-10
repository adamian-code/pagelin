package io.github.adamian.pagelin.pdf.template.table

import io.github.adamian.pagelin.pdf.createPdfPageMetrics

internal object TablePaginator {
    fun paginate(
        rows: List<List<String>>,
        metrics: TableMetrics
    ): List<TablePage> {

        val pageMetrics = createPdfPageMetrics()

        val firstPageAvailableHeight =
            pageMetrics.contentHeight -
                    metrics.titleHeight -
                    metrics.headerHeight

        val normalPageAvailableHeight =
            pageMetrics.contentHeight -
                    metrics.headerHeight

        val firstPageRowCount =
            (firstPageAvailableHeight / metrics.rowHeight)
                .coerceAtLeast(1)

        val normalPageRowCount =
            (normalPageAvailableHeight / metrics.rowHeight)
                .coerceAtLeast(1)

        val pages = mutableListOf<TablePage>()

        var remainingRows = rows

        pages.add(
            TablePage(
                showTitle = true,
                rows = remainingRows.take(firstPageRowCount)
            )
        )

        remainingRows =
            remainingRows.drop(firstPageRowCount)

        while (remainingRows.isNotEmpty()) {

            val pageRows =
                remainingRows.take(normalPageRowCount)

            pages.add(
                TablePage(
                    showTitle = false,
                    rows = pageRows
                )
            )

            remainingRows =
                remainingRows.drop(normalPageRowCount)
        }

        return pages
    }
}

internal data class TablePage(
    val showTitle: Boolean,
    val rows: List<List<String>>
)

internal data class TableMetrics(
    val titleHeight: Int,
    val headerHeight: Int,
    val rowHeight: Int
)
