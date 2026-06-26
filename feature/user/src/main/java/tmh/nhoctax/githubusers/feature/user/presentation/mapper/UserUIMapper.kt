package tmh.nhoctax.githubusers.feature.user.presentation.mapper

import tmh.nhoctax.githubusers.feature.user.domain.model.User
import tmh.nhoctax.githubusers.feature.user.domain.model.UserDetail
import tmh.nhoctax.githubusers.feature.user.presentation.model.UserDetailUIItem
import tmh.nhoctax.githubusers.feature.user.presentation.model.UserUIItem

fun User.toUIItem(isFavorite: Boolean = false): UserUIItem {
    return UserUIItem(
        id = id,
        username = username,
        avatarUrl = avatarUrl,
        isFavorite = isFavorite
    )
}

fun UserDetail.toUIItem(isFavorite: Boolean = false): UserDetailUIItem {
    return UserDetailUIItem(
        username = username,
        avatarUrl = avatarUrl,
        country = country,
        followers = followers,
        following = following,
        url = url,
        isFavorite = isFavorite
    )
}
