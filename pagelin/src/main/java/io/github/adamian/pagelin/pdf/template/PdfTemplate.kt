package io.github.adamian.pagelin.pdf.template

import androidx.compose.runtime.Composable

interface PdfTemplate {
    val pages: List<@Composable () -> Unit>
}