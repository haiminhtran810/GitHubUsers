package tmh.nhoctax.githubusers.feature.user.presentation.userlist

import tmh.nhoctax.githubusers.core.ui.model.UserListItem

sealed interface UserListUIAction {
    data class UserClick(val username: String, val avatarUrl: String) : UserListUIAction
    data class AddUserFavorite(val item: UserListItem) : UserListUIAction
}