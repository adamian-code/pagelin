package ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.printer.PrinterAnimation

@Composable
fun GenerateSection(
    isGenerating: Boolean,
    onGenerate: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedContent(
            targetState = isGenerating,
            label = "generate"
        ) { generating ->
            if (generating) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PrinterAnimation()
                }

            } else {
                Button(
                    onClick = onGenerate,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Anthracite,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.height(52.dp)
                ) {
                    Text("Generate PDF")
                }
            }
        }
    }
}