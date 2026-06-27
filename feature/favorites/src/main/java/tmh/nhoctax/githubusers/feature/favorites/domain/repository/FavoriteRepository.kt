package tmh.nhoctax.githubusers.feature.favorites.domain.repository

import kotlinx.coroutines.flow.Flow
import tmh.nhoctax.githubusers.core.database.model.UserEntity

interface FavoriteRepository {
    fun getFavoriteUsers(): Flow<List<UserEntity>>
    fun isFavorite(id: Int): Flow<Boolean>
    suspend fun addFavorite(userEntity: UserEntity)
    suspend fun remove(id: Int)
}