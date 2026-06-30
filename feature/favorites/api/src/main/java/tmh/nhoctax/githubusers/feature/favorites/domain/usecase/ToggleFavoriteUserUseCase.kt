package tmh.nhoctax.githubusers.feature.favorites.domain.usecase

import tmh.nhoctax.githubusers.feature.favorites.domain.model.FavoriteUser

interface ToggleFavoriteUserUseCase {
    suspend operator fun invoke(user: FavoriteUser)
}
