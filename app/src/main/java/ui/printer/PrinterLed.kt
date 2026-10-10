package ui.printer

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PrinterLed(
    color: Color,
    blinking: Boolean = false
) {
    val alpha =
        if (blinking) {
            val transition =
                rememberInfiniteTransition(
                    label = "led"
                )

            transition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(450),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "alpha"
            ).value

        } else {
            1f
        }

    Box(
        modifier = Modifier
            .size(7.dp)
            .alpha(alpha)
            .clip(CircleShape)
            .background(color)
    )
}