package tmh.nhoctax.githubusers.feature.favorites.presentation

import tmh.nhoctax.githubusers.core.ui.model.UserListItem

sealed interface FavoriteUIAction {
    data class UserClick(val username: String, val avatarUrl: String) : FavoriteUIAction
    data class AddFavoriteUser(val user: UserListItem) : FavoriteUIAction
}