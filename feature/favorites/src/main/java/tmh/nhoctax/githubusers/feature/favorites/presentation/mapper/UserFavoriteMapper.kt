package tmh.nhoctax.githubusers.feature.favorites.presentation.mapper

import tmh.nhoctax.githubusers.core.database.model.UserEntity
import tmh.nhoctax.githubusers.core.ui.model.UserListItem

fun UserEntity.toUserListItem() = UserListItem(
    id = id,
    username = username,
    avatarUrl = url,
    isFavorite = true
)

fun UserListItem.toUserEntity() = UserEntity(
    id = id,
    username = username,
    url = avatarUrl,
)