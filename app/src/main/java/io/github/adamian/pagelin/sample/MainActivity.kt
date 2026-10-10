package io.github.adamian.pagelin.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adamian.pagelin.CustomPdfPage
import io.github.adamian.pagelin.pdf.Pagelin
import io.github.adamian.pagelin.pdf.PdfConfig
import io.github.adamian.pagelin.pdf.template.report.ReportTemplate
import io.github.adamian.pagelin.pdf.template.table.TableTemplate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.Anthracite
import ui.GenerateSection
import ui.SampleTemplate
import ui.TemplateCarousel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreen(
                    onGeneratePdf = { template ->

                        when (template) {

                            SampleTemplate.Report ->
                                generateReport()

                            SampleTemplate.Table ->
                                generateTable()

                            SampleTemplate.Custom ->
                                generateCustom()
                        }
                    }
                )
            }
        }
    }

    private suspend fun generateReport() {
        val sections = (1..20).map {
            "Section $it" to "Lorem ipsum ".repeat(50)
        }

        val document = Pagelin.generate(
            activity = this@MainActivity,
            config = PdfConfig(
                fileName = "report.pdf"
            ),
            template = ReportTemplate(
                title = "Monthly Report",
                sections = sections
            )
        ).getOrThrow()

        document.preview(this@MainActivity)
    }

    private suspend fun generateTable() {
        val rows = (1..100).map {
            listOf(
                "User $it",
                "user$it@test.com",
                "Admin"
            )
        }

        val document = Pagelin.generate(
            activity = this@MainActivity,
            config = PdfConfig(
                fileName = "table.pdf"
            ),
            template = TableTemplate(
                title = "Users",
                columns = listOf(
                    "Name",
                    "Email",
                    "Role"
                ),
                rows = rows
            )
        ).getOrThrow()

        document.preview(this@MainActivity)
    }

    private suspend fun generateCustom() {
        val document = Pagelin.generate(
            activity = this@MainActivity,
            config = PdfConfig(
                fileName = "custom.pdf"
            ),
            pages = listOf(
                { CustomPdfPage() }
            )
        ).getOrThrow()

        document.preview(this@MainActivity)
    }
}

@Composable
fun MainScreen(
    onGeneratePdf: suspend (SampleTemplate) -> Unit
) {

    var selectedTemplate by remember {
        mutableStateOf(SampleTemplate.Report)
    }

    var isGenerating by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppHeader()

        TemplateCarousel(
            selected = selectedTemplate,
            enabled = !isGenerating,
            onSelected = {
                selectedTemplate = it
            }
        )

        Spacer(modifier = Modifier.height(100.dp))

        val scope = rememberCoroutineScope()

        GenerateSection(
            isGenerating = isGenerating,
            onGenerate = {
                scope.launch {
                    isGenerating = true
                    delay(3000)
                    onGeneratePdf(selectedTemplate)
                    delay(500)
                    isGenerating = false
                }
            }
        )
    }
}

@Composable
private fun AppHeader() {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(
                    x = 12.dp,
                    y = (-48).dp
                )
                .graphicsLayer {
                    rotationZ = 12f
                }
                .size(82.dp)
                .border(
                    width = 1.5.dp,
                    color = Anthracite.copy(alpha = .65f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
                    .border(
                        width = 1.dp,
                        color = Anthracite.copy(alpha = .85f),
                        shape = CircleShape
                    )
            )

            Text(
                text = "PDF",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp,
                    color = Anthracite
                )
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Pagelin",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Compose-first PDF engine",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(100.dp)
            )
        }
    }
}