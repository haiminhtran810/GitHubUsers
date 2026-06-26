package tmh.nhoctax.githubusers.feature.user.presentation.model

data class UserUIItem(
    val id: Int,
    val username: String,
    val avatarUrl: String,
    val isFavorite: Boolean = false
)