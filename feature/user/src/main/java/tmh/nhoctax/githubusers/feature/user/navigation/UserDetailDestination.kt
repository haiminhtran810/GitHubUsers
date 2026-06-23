package tmh.nhoctax.githubusers.feature.user.navigation

import kotlinx.serialization.Serializable

@Serializable
data class UserDetailDestination(
    val id: Int,
    val avatarUrl: String
)