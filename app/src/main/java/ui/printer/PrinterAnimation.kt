package ui.printer

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun PrinterAnimation() {

    var printing by remember { mutableStateOf(false) }
    var paperVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        delay(300)

        printing = true

        delay(700)

        paperVisible = true

    }

    val paperOffset by animateDpAsState(
        targetValue = if (paperVisible) 20.dp else (-144).dp,
        animationSpec = tween(
            durationMillis = 1400,
            easing = LinearOutSlowInEasing
        ),
        label = "paper"
    )

    val vibrationOffset by animateDpAsState(
        targetValue = if (printing) 0.dp else 2.dp,
        animationSpec = repeatable(
            iterations = 4,
            animation = tween(
                durationMillis = 60
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "printerVibration"
    )

    Box(
        modifier = Modifier
            .width(200.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        PrinterTop(
            printing = printing,
            modifier = Modifier.offset(x = vibrationOffset)
        )

        PrinterBottom(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 76.dp)
                .offset(x = vibrationOffset)
        )

        Box(
            modifier = Modifier
                .padding(top = 74.dp)
                .width(190.dp)
                .height(180.dp)
                .clipToBounds(),
            contentAlignment = Alignment.TopCenter
        ) {
            Paper(
                modifier = Modifier.offset(y = paperOffset)
            )
        }
    }
}