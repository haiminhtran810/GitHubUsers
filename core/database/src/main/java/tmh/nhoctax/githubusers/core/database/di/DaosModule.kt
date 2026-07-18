package tmh.nhoctax.githubusers.core.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmh.nhoctax.githubusers.core.database.AppDatabase
import tmh.nhoctax.githubusers.core.database.dao.FavoriteDAO
import tmh.nhoctax.githubusers.core.database.dao.UserDAO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    @Singleton
    fun provideFavoriteDAO(database: AppDatabase): FavoriteDAO {
        return database.favoriteDAO()
    }

    @Provides
    @Singleton
    fun provideUserDAO(database: AppDatabase): UserDAO {
        return database.userDAO()
    }
}