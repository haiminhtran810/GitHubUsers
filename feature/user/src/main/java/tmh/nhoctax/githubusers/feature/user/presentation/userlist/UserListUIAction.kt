package tmh.nhoctax.githubusers.feature.user.presentation.userlist

sealed interface UserListUIAction {
    data class UserClick(val id: String) : UserListUIAction
    data class AddUserFavorite(val id: String) : UserListUIAction
}