package io.github.adamian.pagelin.pdf.template

import androidx.compose.runtime.Composable

internal interface PreparedPdfTemplate {
    @Composable
    fun buildPages(): List<@Composable () -> Unit>
}