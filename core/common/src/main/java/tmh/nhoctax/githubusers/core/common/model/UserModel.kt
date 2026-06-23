package tmh.nhoctax.githubusers.core.common.model


data class UserModel(
    val id: Int = 0,
    val username: String = "",
    val avatarUrl: String = "",
    val url: String = "",
    val followers: Int? = null,
    val bio: String? = "",
    val isFavorite: Boolean = false,
)
