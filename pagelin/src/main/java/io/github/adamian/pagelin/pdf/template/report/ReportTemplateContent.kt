package io.github.adamian.pagelin.pdf.template.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun ReportTemplateContent(
    title: String,
    sections: List<Pair<String, String>>,
    showTitle: Boolean
) {
    Column(
        modifier = Modifier.padding(24.dp)
    ) {
        if (showTitle) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )
        }

        sections.forEach { (sectionTitle, sectionText) ->
            ReportSection(
                title = sectionTitle,
                text = sectionText
            )
        }
    }
}

@Composable
internal fun ReportSection(
    title: String,
    text: String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )
    }
}
