package tmh.nhoctax.githubusers.feature.favorites.domain.usecase

import kotlinx.coroutines.flow.Flow

interface IsFavoriteUserUseCase {
    operator fun invoke(id: Int): Flow<Boolean>
}
