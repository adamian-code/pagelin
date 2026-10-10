package io.github.adamian.pagelin.pdf.pagination

import io.github.adamian.pagelin.pdf.createPdfPageMetrics


internal data class MeasuredItem<T>(
    val item: T,
    val height: Int
)

internal data class PaginationInput<T>(
    val firstPageReservedHeight: Int,
    val measuredItems: List<MeasuredItem<T>>
)

internal data class PageChunk<T>(
    val index: Int,
    val isFirstPage: Boolean,
    val items: List<T>
)

internal object PdfPaginator {

    fun <T> paginate(
        input: PaginationInput<T>
    ): List<PageChunk<T>> {

        val pageMetrics = createPdfPageMetrics()

        val pages = mutableListOf<PageChunk<T>>()
        val currentItems = mutableListOf<T>()

        var pageIndex = 0

        var remainingHeight =
            pageMetrics.contentHeight -
                    input.firstPageReservedHeight

        input.measuredItems.forEach { item ->

            require(item.height < pageMetrics.contentHeight) {
                "Content item exceeds the available page height."
            }

            if (
                item.height > remainingHeight &&
                currentItems.isNotEmpty()
            ) {

                pages += PageChunk(
                    index = pageIndex,
                    isFirstPage = pageIndex == 0,
                    items = currentItems.toList()
                )

                currentItems.clear()

                pageIndex++

                remainingHeight =
                    pageMetrics.contentHeight
            }

            currentItems += item.item

            remainingHeight -= item.height
        }

        if (currentItems.isNotEmpty()) {
            pages += PageChunk(
                index = pageIndex,
                isFirstPage = pageIndex == 0,
                items = currentItems.toList()
            )
        }

        return pages
    }
}