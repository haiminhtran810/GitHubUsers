package tmh.nhoctax.githubusers.feature.user.data.mapper

import tmh.nhoctax.githubusers.feature.user.data.remote.model.UserDetailResponse
import tmh.nhoctax.githubusers.feature.user.data.remote.model.UserResponse
import tmh.nhoctax.githubusers.feature.user.domain.model.User
import tmh.nhoctax.githubusers.feature.user.domain.model.UserDetail

fun UserResponse.toDomain(): User {
    return User(
        id = id,
        username = login,
        avatarUrl = avatarUrl
    )
}

fun UserDetailResponse.toDomain(): UserDetail {
    return UserDetail(
        id = id,
        username = login.orEmpty(),
        avatarUrl = avatarUrl.orEmpty(),
        country = location.orEmpty(),
        followers = followers ?: 0,
        following = following ?: 0,
        url = url.orEmpty()
    )
}