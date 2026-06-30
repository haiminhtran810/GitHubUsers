package tmh.nhoctax.githubusers.feature.favorites.domain.usecase

import kotlinx.coroutines.flow.first
import tmh.nhoctax.githubusers.core.database.model.UserEntity
import tmh.nhoctax.githubusers.feature.favorites.domain.repository.FavoriteRepository
import javax.inject.Inject

import tmh.nhoctax.githubusers.feature.favorites.domain.model.FavoriteUser

class ToggleFavoriteUserUseCaseImpl @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ToggleFavoriteUserUseCase {
    override suspend operator fun invoke(user: FavoriteUser) {
        val userId = user.id
        // check if user is already a favorite
        val isFavorite = favoriteRepository.isFavorite(userId).first()
        if (isFavorite) {
            favoriteRepository.remove(userId)
        } else {
            favoriteRepository.addFavorite(UserEntity(user.id, user.username, user.url))
        }
    }
}