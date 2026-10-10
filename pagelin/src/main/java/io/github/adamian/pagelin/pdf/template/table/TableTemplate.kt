package io.github.adamian.pagelin.pdf.template.table

import androidx.compose.runtime.Composable
import io.github.adamian.pagelin.pdf.template.PdfTemplate

class TableTemplate(
    private val title: String,
    private val columns: List<String>,
    private val rows: List<List<String>>
) : PdfTemplate {

    override val pages: List<@Composable () -> Unit>
        get() {

            val pages =
                TablePaginator.paginate(
                    rows = rows,
                    metrics = TableMetrics(
                        titleHeight = TableMetricsDefaults.TITLE_HEIGHT,
                        headerHeight = TableMetricsDefaults.HEADER_HEIGHT,
                        rowHeight = TableMetricsDefaults.ROW_HEIGHT
                    )
                )

            return pages.map { page ->

                @Composable {
                    TableTemplateContent(
                        title = title,
                        columns = columns,
                        rows = page.rows,
                        showTitle = page.showTitle
                    )
                }
            }
        }
}

internal object TableMetricsDefaults {
    const val TITLE_HEIGHT = 68
    const val HEADER_HEIGHT = 38
    const val ROW_HEIGHT = 65
}
