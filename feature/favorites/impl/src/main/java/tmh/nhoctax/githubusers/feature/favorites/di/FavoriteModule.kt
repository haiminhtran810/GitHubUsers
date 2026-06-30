package tmh.nhoctax.githubusers.feature.favorites.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmh.nhoctax.githubusers.feature.favorites.data.repository.FavoriteRepositoryImpl
import tmh.nhoctax.githubusers.feature.favorites.domain.repository.FavoriteRepository
import tmh.nhoctax.githubusers.feature.favorites.domain.usecase.GetFavoriteUserIdsUseCase
import tmh.nhoctax.githubusers.feature.favorites.domain.usecase.GetFavoriteUserIdsUseCaseImpl
import tmh.nhoctax.githubusers.feature.favorites.domain.usecase.IsFavoriteUserUseCase
import tmh.nhoctax.githubusers.feature.favorites.domain.usecase.IsFavoriteUserUseCaseImpl
import tmh.nhoctax.githubusers.feature.favorites.domain.usecase.ToggleFavoriteUserUseCase
import tmh.nhoctax.githubusers.feature.favorites.domain.usecase.ToggleFavoriteUserUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoriteModule {
    @Binds
    abstract fun provideFavoriteRepository(
        favoriteRepositoryImpl: FavoriteRepositoryImpl
    ): FavoriteRepository

    @Binds
    abstract fun bindToggleFavoriteUserUseCase(
        impl: ToggleFavoriteUserUseCaseImpl
    ): ToggleFavoriteUserUseCase

    @Binds
    abstract fun bindIsFavoriteUserUseCase(
        impl: IsFavoriteUserUseCaseImpl
    ): IsFavoriteUserUseCase

    @Binds
    abstract fun bindGetFavoriteUserIdsUseCase(
        impl: GetFavoriteUserIdsUseCaseImpl
    ): GetFavoriteUserIdsUseCase
}