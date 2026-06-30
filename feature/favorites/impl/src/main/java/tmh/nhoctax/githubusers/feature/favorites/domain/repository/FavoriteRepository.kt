package tmh.nhoctax.githubusers.feature.favorites.domain.repository

import kotlinx.coroutines.flow.Flow
import tmh.nhoctax.githubusers.core.database.model.UserEntity

interface FavoriteRepository {
    fun getFavoriteUsers(): Flow<List<UserEntity>>

    // 1. Extremely fast Lookup (0(1) Performance)
    // 2 Uniqueness (No Duplicate Elements)
    // Each user (represent by an ID), there is never a need to store the same ID multiple times
    fun getFavoriteUserIds(): Flow<Set<Int>>

    fun isFavorite(id: Int): Flow<Boolean>

    suspend fun addFavorite(userEntity: UserEntity)

    suspend fun remove(id: Int)
}