package tmh.nhoctax.githubusers.feature.user.domain.model

data class UserDetail(
    val id: Int,
    val username: String,
    val avatarUrl: String,
    val country: String,
    val followers: Int,
    val following: Int,
    val url: String,
)