package tmh.nhoctax.githubusers.feature.user.presentation.userdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmh.nhoctax.githubusers.core.ui.component.AppAvatarAsyncImage
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
                        onAction(UserDetailUIAction.AddUserFavorite(state.user.username))
                    },
                    modifier = Modifier
                        .fillMaxSize()
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
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppAvatarAsyncImage(imageSize = 120.dp, imageUrl = user.avatarUrl)
        SpacerVer(16.dp)
        Text(
            text = user.username,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        SpacerVer(8.dp)
        Text(
            text = "Country: ${user.country.ifEmpty { "N/A" }}",
            style = MaterialTheme.typography.bodyLarge
        )
        SpacerVer(8.dp)
        Text(
            text = "Followers: ${user.followers} | Following: ${user.following}",
            style = MaterialTheme.typography.bodyMedium
        )
        SpacerVer(8.dp)
        Text(
            text = "Profile URL: ${user.url}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
        SpacerVer(8.dp)
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (user.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (user.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
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
