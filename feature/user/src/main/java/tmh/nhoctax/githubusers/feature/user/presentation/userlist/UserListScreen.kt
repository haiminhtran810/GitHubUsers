package tmh.nhoctax.githubusers.feature.user.presentation.userlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import tmh.nhoctax.githubusers.feature.user.domain.model.User

@Composable
fun UserListScreen(vm: UserListViewModel = hiltViewModel<UserListViewModel>()) {
    val state by vm.state.collectAsStateWithLifecycle()

    UserListScreen(
        state = state,
        onAction = vm::onActive
    )
}

@Composable
fun UserListScreen(
    state: UserListUIState,
    onAction: (UserListUIAction) -> Unit
) {
    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (state.error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = state.error, color = MaterialTheme.colorScheme.error)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(
                state.users,
                key = {
                    it.id
                }) { user ->
                UserItem(
                    user = user,
                    onClick = { onAction(UserListUIAction.UserClick(user.id.toString())) },
                    onFavoriteClick = { onAction(UserListUIAction.AddUserFavorite(user.id.toString())) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.padding(top = 8.dp),
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
fun UserItem(
    user: User,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = user.avatarUrl,
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

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
            Spacer(modifier = Modifier.height(4.dp))
        }

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.Outlined.Star,
            contentDescription = "Favorite",
            modifier = Modifier
                .size(16.dp)
                .clickable(
                    onClick = onFavoriteClick
                ),
            tint = Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserListScreenPreview() {
    MaterialTheme {
        UserListScreen(
            state = UserListUIState(
                isLoading = false,
                users = listOf(
                    User(
                        id = 1,
                        username = "mojombo",
                        avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4"
                    ),
                    User(
                        id = 2,
                        username = "defunkt",
                        avatarUrl = "https://avatars.githubusercontent.com/u/2?v=4"
                    ),
                    User(
                        id = 3,
                        username = "pjhyett",
                        avatarUrl = "https://avatars.githubusercontent.com/u/3?v=4"
                    )
                )
            ),
            onAction = {}
        )
    }
}