package tmh.nhoctax.githubusers.feature.user.presentation.userlist

import tmh.nhoctax.githubusers.feature.user.presentation.model.UserUIItem

data class UserListUIState(
    val isLoading: Boolean = false,
    val users: List<UserUIItem> = emptyList(),
    val error: String? = null,
)