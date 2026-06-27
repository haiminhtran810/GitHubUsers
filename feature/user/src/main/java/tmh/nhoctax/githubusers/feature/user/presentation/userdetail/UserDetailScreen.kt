package tmh.nhoctax.githubusers.feature.user.presentation.userdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import tmh.nhoctax.githubusers.core.ui.component.AppAvatarAsyncImage
import tmh.nhoctax.githubusers.core.ui.component.SpacerHor
import tmh.nhoctax.githubusers.core.ui.component.SpacerVer
import tmh.nhoctax.githubusers.core.ui.theme.GithubUsersTheme
import tmh.nhoctax.githubusers.feature.user.presentation.model.UserDetailUIItem

@Composable
fun UserDetailScreen(vm: UserDetailViewModel = hiltViewModel<UserDetailViewModel>()) {
    val state by vm.state.collectAsState()

    UserDetailContent(
        state = state,
        onAction = vm::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailContent(
    state: UserDetailUIState,
    onAction: (UserDetailUIAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "User Details") },
                windowInsets = WindowInsets(0.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFF5EF),
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                ),
                navigationIcon = {
                    IconButton(onClick = { onAction(UserDetailUIAction.onBackAction) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF5EF))
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.error != null) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.user != null) {
                UserDetailInfo(
                    user = state.user,
                    onFavoriteClick = {
                        onAction(UserDetailUIAction.AddUserFavorite)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun UserDetailInfo(
    user: UserDetailUIItem,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppAvatarAsyncImage(imageSize = 120.dp, imageUrl = user.avatarUrl)

            SpacerVer(24.dp)

            Text(
                text = user.username,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            SpacerVer(8.dp)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Location",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
                SpacerHor(4.dp)
                Text(
                    text = user.country.ifEmpty { "N/A" },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            SpacerVer(32.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                UserStatItem(
                    label = "Followers",
                    icon = Icons.Filled.Person,
                    iconTint = Color(0xFFFFB300),
                    value = user.followers.toString()
                )

                UserStatItem(
                    label = "Following",
                    icon = Icons.Filled.Star,
                    iconTint = Color(0xFFFF7043),
                    value = user.following.toString()
                )

                UserStatItem(
                    label = "Favorite",
                    icon = if (user.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    iconTint = if (user.isFavorite) Color(0xFFE91E63) else Color.Gray,
                    value = if (user.isFavorite) "Yes" else "No",
                    modifier = Modifier.clickable { onFavoriteClick() }
                )
            }
        }
    }
}

@Composable
private fun UserStatItem(
    label: String,
    icon: ImageVector,
    iconTint: Color,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        SpacerVer(8.dp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
            SpacerHor(8.dp)
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserDetailContentLoadingPreview() {
    GithubUsersTheme {
        UserDetailContent(
            state = UserDetailUIState(isLoading = true),
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserDetailContentErrorPreview() {
    GithubUsersTheme {
        UserDetailContent(
            state = UserDetailUIState(error = "User not found"),
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserDetailContentSuccessPreview() {
    GithubUsersTheme {
        UserDetailContent(
            state = UserDetailUIState(
                user = UserDetailUIItem(
                    id = 0,
                    username = "johndoe",
                    avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                    country = "United States",
                    followers = 100,
                    following = 50,
                    url = "https://github.com/johndoe",
                    isFavorite = true
                )
            ),
            onAction = {}
        )
    }
}
