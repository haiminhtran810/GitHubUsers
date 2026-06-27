package tmh.nhoctax.githubusers.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import tmh.nhoctax.githubusers.core.database.AppDatabase
import tmh.nhoctax.githubusers.core.database.dao.FavoriteDAO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private val DATABASE_NAME = "nhoctax.db"

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteDAO(database: AppDatabase): FavoriteDAO {
        return database.favoriteDAO()
    }
}