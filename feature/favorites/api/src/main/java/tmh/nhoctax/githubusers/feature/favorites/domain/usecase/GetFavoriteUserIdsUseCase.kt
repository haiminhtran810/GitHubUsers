package tmh.nhoctax.githubusers.feature.favorites.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetFavoriteUserIdsUseCase {
    operator fun invoke(): Flow<Set<Int>>
}