package tmh.nhoctax.githubusers.core.ui.model

data class UserListItem(
    val id: Int,
    val username: String,
    val avatarUrl: String,
    val isFavorite: Boolean = false
)
