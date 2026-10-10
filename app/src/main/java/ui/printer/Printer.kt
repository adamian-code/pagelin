package ui.printer


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.Anthracite
import ui.Charcoal
import ui.Graphite
import ui.SuccessGreen
import ui.WarningAmber

@Composable
internal fun PrinterTop(
    printing: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(190.dp)
            .height(82.dp),
        shape = RoundedCornerShape(
            topStart = 22.dp,
            topEnd = 22.dp
        ),
        color = Anthracite,
        shadowElevation = 12.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        PrinterLed(
                            color = SuccessGreen
                        )

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )

                        PrinterLed(
                            color = WarningAmber,
                            blinking = printing
                        )
                    }

                    Surface(
                        modifier = Modifier.size(20.dp),
                        shape = CircleShape,
                        color = Charcoal
                    ) {

                        Box(
                            contentAlignment = Alignment.Center
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .border(
                                        1.dp,
                                        Color(0xFF8F949B),
                                        CircleShape
                                    )
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "PAGELIN",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Charcoal,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.8.sp,
                        shadow = Shadow(
                            color = Color.White.copy(alpha = .08f),
                            offset = Offset(0f, 1f),
                            blurRadius = 1f
                        )
                    )
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .width(120.dp)
                    .height(6.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 6.dp,
                            topEnd = 6.dp
                        )
                    )
                    .background(Graphite)
            )
        }
    }
}

@Composable
internal fun PrinterBottom(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(190.dp)
            .height(48.dp),
        shape = RoundedCornerShape(
            bottomStart = 22.dp,
            bottomEnd = 22.dp
        ),
        color = Anthracite
    ) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .width(120.dp)
                    .height(6.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 6.dp,
                            bottomEnd = 6.dp
                        )
                    )
                    .background(Graphite)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .width(110.dp)
                    .height(2.dp)
                    .background(
                        Color.Black.copy(alpha = .35f)
                    )
            )
        }
    }
}
