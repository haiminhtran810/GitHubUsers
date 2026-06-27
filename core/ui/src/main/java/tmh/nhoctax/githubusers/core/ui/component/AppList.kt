package tmh.nhoctax.githubusers.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.flowOf
import tmh.nhoctax.githubusers.core.ui.model.UserListItem
import tmh.nhoctax.githubusers.core.ui.theme.GithubUsersTheme

@Composable
fun AppUserListStandard(
    modifier: Modifier = Modifier,
    users: List<UserListItem>,
    onUserClick: (String, String) -> Unit,
    onFavoriteClick: (UserListItem) -> Unit
) {
    Box(modifier = modifier) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = users, key = { it.id }) { item ->
                UserItem(
                    user = item,
                    onUserClick = {
                        onUserClick.invoke(
                            item.username,
                            item.avatarUrl
                        )
                    },
                    onFavoriteClick = {
                        onFavoriteClick.invoke(item)
                    }
                )
            }
        }

        if (users.isEmpty()) {
            Text(
                text = "No favorite users found",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun AppUserList(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<UserListItem>,
    onUserClick: (String, String) -> Unit,
    onFavoriteClick: (UserListItem) -> Unit
) {
    Box(modifier = modifier) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(
                count = lazyPagingItems.itemCount,
                key = lazyPagingItems.itemKey { keyValue ->
                    keyValue.id
                }
            ) { index ->
                lazyPagingItems[index]?.let { item ->
                    UserItem(
                        user = item,
                        onUserClick = {
                            onUserClick.invoke(
                                item.username,
                                item.avatarUrl
                            )
                        },
                        onFavoriteClick = {
                            onFavoriteClick.invoke(item)
                        }
                    )
                }
            }
        }

        // 1. Handle the append state (Loading more items at the bottom)
        when (val pagingStateAppend = lazyPagingItems.loadState.append) {
            is LoadState.Loading -> {
                AppLoading()
            }

            is LoadState.Error -> {
                AppToast(
                    message = "Error: ${pagingStateAppend.error.localizedMessage}",
                    visible = true
                )
            }

            is LoadState.NotLoading -> Unit
        }

        // 2. Handle the refresh state (Initial loading / Empty state / Error State)
        when (val pagingStateRefresh = lazyPagingItems.loadState.refresh) {
            is LoadState.Loading -> {
                AppLoading()
            }

            is LoadState.Error -> {
                AppToast(
                    message = "Error: ${pagingStateRefresh.error.localizedMessage}",
                    visible = true
                )
            }

            is LoadState.NotLoading -> {
                if (lazyPagingItems.itemCount == 0) {
                    Text(
                        text = "No users found",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }

}

@Composable
private fun UserItem(
    modifier: Modifier = Modifier,
    user: UserListItem,
    onUserClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onUserClick.invoke() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppAvatarAsyncImage(
                imageSize = 60.dp,
                imageUrl = user.avatarUrl
            )

            SpacerHor(16.dp)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.username,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "@${user.username}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                SpacerVer(4.dp)
            }

            SpacerHor(8.dp)

            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = "Favorite",
                modifier = Modifier
                    .size(16.dp)
                    .clickable(
                        onClick = {
                            onFavoriteClick.invoke()
                        }
                    ),
                tint = if (user.isFavorite) Color.Yellow else Color.Gray
            )
        }
        SpacerVer(16.dp)
    }
}

@Preview(showBackground = true)
@Composable
private fun AppUserListPreview() {
    val sampleUsers = listOf(
        UserListItem(id = 1, username = "user1", avatarUrl = "", isFavorite = false),
        UserListItem(id = 2, username = "user2", avatarUrl = "", isFavorite = true),
        UserListItem(id = 3, username = "user3", avatarUrl = "", isFavorite = false)
    )
    val pagingData = PagingData.from(sampleUsers)
    val lazyPagingItems = flowOf(pagingData).collectAsLazyPagingItems()

    GithubUsersTheme {
        AppUserList(
            modifier = Modifier.padding(horizontal = 16.dp),
            lazyPagingItems = lazyPagingItems,
            onUserClick = { _, _ -> },
            onFavoriteClick = {}
        )
    }
}