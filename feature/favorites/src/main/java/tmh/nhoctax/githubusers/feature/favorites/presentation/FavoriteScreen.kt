package tmh.nhoctax.githubusers.feature.favorites.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tmh.nhoctax.githubusers.core.ui.component.AppUserListStandard
import tmh.nhoctax.githubusers.core.ui.model.UserListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(viewModel: FavoriteViewModel = hiltViewModel<FavoriteViewModel>()) {
    val favoriteUsers by viewModel.favoriteUsers.collectAsStateWithLifecycle()
    FavoriteScreenContent(favoriteUsers, viewModel::onAction)
}

@Composable
fun FavoriteScreenContent(
    favoriteUsers: List<UserListItem>,
    onAction: (FavoriteUIAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        AppUserListStandard(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            users = favoriteUsers,
            onUserClick = { username, avatarUrl ->
                onAction.invoke(FavoriteUIAction.UserClick(username, avatarUrl))
            },
            onFavoriteClick = { userListItem ->
                onAction.invoke(FavoriteUIAction.AddFavoriteUser(userListItem))
            }
        )
    }
}
