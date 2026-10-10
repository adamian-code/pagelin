package io.github.adamian.pagelin.pdf.template.report

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import io.github.adamian.pagelin.pdf.pagination.MeasurePaginationComponents
import io.github.adamian.pagelin.pdf.pagination.PdfPaginator
import io.github.adamian.pagelin.pdf.template.PdfTemplate
import io.github.adamian.pagelin.pdf.template.PreparedPdfTemplate

class ReportTemplate(
    private val title: String,
    private val sections: List<Pair<String, String>>
) : PdfTemplate, PreparedPdfTemplate {

    override val pages: List<@Composable () -> Unit>
        get() = emptyList()

    @Composable
    override fun buildPages(): List<@Composable () -> Unit> =
        rememberReportPages(
            title = title,
            sections = sections
        )
}

@Composable
internal fun rememberReportPages(
    title: String,
    sections: List<Pair<String, String>>
): List<@Composable () -> Unit> {

    val pagesState = remember {
        mutableStateOf<List<@Composable () -> Unit>>(emptyList())
    }

    MeasurePaginationComponents(
        firstPageContent = {
            ReportTitle(title)
        },
        items = sections,
        itemContent = { section ->
            ReportSection(
                title = section.first,
                text = section.second
            )
        }
    ) { input ->

        val pages = PdfPaginator.paginate(input)

        pagesState.value =
            pages.map { page ->
                {
                    ReportTemplateContent(
                        title = title,
                        sections = page.items,
                        showTitle = page.isFirstPage
                    )
                }
            }
    }

    return pagesState.value
}

@Composable
internal fun ReportTitle(
    title: String
) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium
    )
}