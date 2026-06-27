package tmh.nhoctax.githubusers.feature.favorites.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmh.nhoctax.githubusers.feature.favorites.data.repository.FavoriteRepositoryImpl
import tmh.nhoctax.githubusers.feature.favorites.domain.repository.FavoriteRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoriteModule {
    @Binds
    abstract fun provideFavoriteRepository(
        favoriteRepositoryImpl: FavoriteRepositoryImpl
    ): FavoriteRepository
}