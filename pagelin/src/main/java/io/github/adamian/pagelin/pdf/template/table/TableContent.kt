package io.github.adamian.pagelin.pdf.template.table

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun TableTemplateContent(
    title: String,
    columns: List<String>,
    rows: List<List<String>>,
    showTitle: Boolean = true
) {
    Column(
        modifier = Modifier.padding(24.dp)
    ) {
        if (showTitle) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        TableHeader(columns = columns)

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp)
        )

        rows.forEach { row ->
            TableRow(row = row)

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }
    }
}

@Composable
internal fun TableHeader(
    columns: List<String>
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        columns.forEach { column ->
            Text(
                text = column,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
internal fun TableRow(
    row: List<String>
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        row.forEach { value ->
            Text(
                text = value,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}