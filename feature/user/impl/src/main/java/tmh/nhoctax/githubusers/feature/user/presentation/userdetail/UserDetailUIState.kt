package tmh.nhoctax.githubusers.feature.user.presentation.userdetail

import tmh.nhoctax.githubusers.feature.user.presentation.model.UserDetailUIItem

data class UserDetailUIState(
    val user: UserDetailUIItem? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)