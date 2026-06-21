package tmh.nhoctax.githubusers.feature.user.presentation.userlist

import tmh.nhoctax.githubusers.feature.user.domain.model.User

data class UserListUIState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String? = null,
)