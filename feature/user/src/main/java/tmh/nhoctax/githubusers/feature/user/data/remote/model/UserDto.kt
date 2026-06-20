package tmh.nhoctax.githubusers.feature.user.data.remote.model

import com.google.gson.annotations.SerializedName
import tmh.nhoctax.githubusers.feature.user.domain.model.User

data class UserDto(
    @SerializedName("id") val id: Int,
    @SerializedName("login") val login: String,
    @SerializedName("avatar_url") val avatarUrl: String
)

fun UserDto.toDomain(): User {
    return User(
        id = id,
        username = login,
        avatarUrl = avatarUrl
    )
}