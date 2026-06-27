package tmh.nhoctax.githubusers.core.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tmh.nhoctax.githubusers.core.ui.R

@Composable
fun AppToast(
    modifier: Modifier = Modifier,
    message: String,
    visible: Boolean,
    shadowY: Dp = 2.dp,
    fullWidth: Boolean = true,
    onDismiss: (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(percent = 25)

    var toastModifier = modifier
    if (fullWidth) {
        toastModifier = toastModifier.fillMaxWidth()
    }
    toastModifier = toastModifier.height(IntrinsicSize.Min)
    if (shadowY > 0.dp) {
        toastModifier = toastModifier
            .background(
                color = Color(0xFFFFC2C2),
                shape = shape
            )
            .padding(bottom = shadowY)
    }
    toastModifier = toastModifier
        .background(
            color = Color(0xFFFFE6E6),
            shape = shape
        )

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = toastModifier
                .border(1.dp, Color(0xFFFFC2C2), shape)
                .clickable { onDismiss?.invoke() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_toast),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(16.dp).padding(bottom = 2.dp)
            )
            SpacerHor(4.dp)
            Text(
                text = message,
                color = Color.Red,
                fontSize = 12.sp
            )
        }
    }
}
