package tmh.nhoctax.githubusers.feature.favorites.domain.usecase

import kotlinx.coroutines.flow.Flow
import tmh.nhoctax.githubusers.core.database.model.UserEntity
import tmh.nhoctax.githubusers.feature.favorites.domain.repository.FavoriteRepository
import javax.inject.Inject

class GetFavoriteUsersUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    operator fun invoke(): Flow<List<UserEntity>> {
        return favoriteRepository.getFavoriteUsers()
    }
}