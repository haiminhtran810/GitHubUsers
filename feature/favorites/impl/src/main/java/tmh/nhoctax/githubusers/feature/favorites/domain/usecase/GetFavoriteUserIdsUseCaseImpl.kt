package tmh.nhoctax.githubusers.feature.favorites.domain.usecase

import kotlinx.coroutines.flow.Flow
import tmh.nhoctax.githubusers.feature.favorites.domain.repository.FavoriteRepository

import javax.inject.Inject

class GetFavoriteUserIdsUseCaseImpl @Inject constructor(private val favoriteRepository: FavoriteRepository) :
    GetFavoriteUserIdsUseCase {
    override fun invoke(): Flow<Set<Int>> {
        return favoriteRepository.getFavoriteUserIds()
    }
}