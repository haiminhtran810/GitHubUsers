package tmh.nhoctax.githubusers.feature.user.presentation.userlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.flowOf
import tmh.nhoctax.githubusers.core.ui.component.AppLoading
import tmh.nhoctax.githubusers.core.ui.component.AppUserList
import tmh.nhoctax.githubusers.core.ui.model.UserListItem
import tmh.nhoctax.githubusers.core.ui.theme.GithubUsersTheme

@Composable
fun UserListScreen(vm: UserListViewModel = hiltViewModel<UserListViewModel>()) {
    val state by vm.state.collectAsStateWithLifecycle()
    val pagingItems = vm.userPaging.collectAsLazyPagingItems()

    UserListContentScreen(
        state = state,
        pagingItems = pagingItems,
        onAction = vm::onActive
    )
}

@Composable
fun UserListContentScreen(
    state: UserListUIState,
    pagingItems: LazyPagingItems<UserListItem>,
    onAction: (UserListUIAction) -> Unit
) {
    if (state.isLoading) {
        AppLoading()
    } else if (state.error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = state.error, color = MaterialTheme.colorScheme.error)
        }
    } else {
        AppUserList(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            lazyPagingItems = pagingItems,
            onUserClick = { username, url ->
                onAction.invoke(UserListUIAction.UserClick(username = username, avatarUrl = url))
            },
            onFavoriteClick = {
                onAction.invoke(UserListUIAction.AddUserFavorite("0"))
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserListContentScreenPreview() {
    val pagingData = PagingData.from(
        listOf(
            UserListItem(
                id = 1,
                username = "octocat",
                avatarUrl = "https://avatars.githubusercontent.com/u/583231?v=4",
                isFavorite = false
            ),
            UserListItem(
                id = 2,
                username = "torvalds",
                avatarUrl = "https://avatars.githubusercontent.com/u/1024025?v=4",
                isFavorite = true
            )
        )
    )
    val pagingItems = flowOf(pagingData).collectAsLazyPagingItems()

    GithubUsersTheme {
        UserListContentScreen(
            state = UserListUIState(),
            pagingItems = pagingItems,
            onAction = {}
        )
    }
}

