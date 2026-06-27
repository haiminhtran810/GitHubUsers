package tmh.nhoctax.githubusers.feature.favorites.domain.usecase

import kotlinx.coroutines.flow.first
import tmh.nhoctax.githubusers.core.database.model.UserEntity
import tmh.nhoctax.githubusers.feature.favorites.domain.repository.FavoriteRepository
import javax.inject.Inject

class ToggleFavoriteUserUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(userEntity: UserEntity) {
        val userId = userEntity.id
        // check if user is already a favorite
        val isFavorite = favoriteRepository.isFavorite(userId).first()
        if (isFavorite) {
            favoriteRepository.remove(userId)
        } else {
            favoriteRepository.addFavorite(userEntity)
        }
    }
}