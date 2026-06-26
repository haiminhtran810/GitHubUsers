package tmh.nhoctax.githubusers.feature.user.presentation.userlist

sealed interface UserListUIAction {
    data class UserClick(val username: String, val avatarUrl: String) : UserListUIAction
    data class AddUserFavorite(val id: String) : UserListUIAction
}