package tmh.nhoctax.githubusers.feature.user.presentation.model

data class UserDetailUIItem(
    val username: String,
    val avatarUrl: String,
    val country: String,
    val followers: Int,
    val following: Int,
    val url: String,
    val isFavorite: Boolean = false
)