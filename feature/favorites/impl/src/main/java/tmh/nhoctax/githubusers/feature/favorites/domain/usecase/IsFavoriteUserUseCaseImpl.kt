package tmh.nhoctax.githubusers.feature.favorites.domain.usecase

import kotlinx.coroutines.flow.Flow
import tmh.nhoctax.githubusers.feature.favorites.domain.repository.FavoriteRepository
import javax.inject.Inject

class IsFavoriteUserUseCaseImpl @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : IsFavoriteUserUseCase {
    override operator fun invoke(id: Int): Flow<Boolean> {
        return favoriteRepository.isFavorite(id)
    }
}