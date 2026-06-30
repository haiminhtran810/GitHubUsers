package tmh.nhoctax.githubusers.feature.user.navigation

import kotlinx.serialization.Serializable

@Serializable
data object UserListDestination

@Serializable
data class UserId(
    val userId: String
)