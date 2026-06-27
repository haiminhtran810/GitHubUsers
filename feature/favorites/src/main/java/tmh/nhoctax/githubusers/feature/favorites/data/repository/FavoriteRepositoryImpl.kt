package tmh.nhoctax.githubusers.feature.favorites.data.repository

import kotlinx.coroutines.flow.Flow
import tmh.nhoctax.githubusers.core.database.dao.FavoriteDAO
import tmh.nhoctax.githubusers.core.database.model.UserEntity
import tmh.nhoctax.githubusers.feature.favorites.domain.repository.FavoriteRepository
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(private val favoriteDAO: FavoriteDAO) :
    FavoriteRepository {
    override fun getFavoriteUsers(): Flow<List<UserEntity>> {
        return favoriteDAO.getUsers()
    }

    override fun isFavorite(id: Int): Flow<Boolean> {
        return favoriteDAO.isFavorite(id)
    }

    override suspend fun addFavorite(userEntity: UserEntity) {
        return favoriteDAO.insertUser(userEntity)
    }

    override suspend fun remove(id: Int) {
        return favoriteDAO.removeUser(id)
    }
}