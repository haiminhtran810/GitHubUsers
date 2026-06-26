package tmh.nhoctax.githubusers.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tmh.nhoctax.githubusers.core.ui.R

@Composable
fun AppAvatarAsyncImage(
    modifier: Modifier = Modifier,
    imageSize: Dp = 40.dp,
    imageUrl: String? = "",
    borderSize: Dp = 1.dp,
    borderColor: Color = MaterialTheme.colorScheme.outline
) {
    Box(
        modifier = modifier
            .size(imageSize)
            .clip(CircleShape)
            .border(borderSize, borderColor, CircleShape)
            .background(MaterialTheme.colorScheme.onSurface),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = modifier.size(imageSize),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.placeholder),
            error = painterResource(R.drawable.placeholder),
        )
    }
}