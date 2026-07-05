package tmh.nhoctax.githubusers.feature.user.di


import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import tmh.nhoctax.githubusers.core.database.dao.UserDAO
import tmh.nhoctax.githubusers.feature.user.data.datastore.UserPreferencesDataStore
import tmh.nhoctax.githubusers.feature.user.data.datastore.UserPreferencesDataStoreImpl
import tmh.nhoctax.githubusers.feature.user.data.remote.UserApi
import tmh.nhoctax.githubusers.feature.user.data.repository.UserRepositoryImpl
import tmh.nhoctax.githubusers.feature.user.data.repository.paging.UserRemoteMediator
import tmh.nhoctax.githubusers.feature.user.domain.repository.UserRepository
import javax.inject.Singleton
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile

private const val USER_PREFERENCES = "user_preferences"

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserDataStore(
        @ApplicationContext context: Context
    ): UserPreferencesDataStore {
        return UserPreferencesDataStoreImpl(
            dataStore = PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile(USER_PREFERENCES) }
            )
        )
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        api: UserApi,
        userDAO: UserDAO,
        remoteMediator: UserRemoteMediator
    ): UserRepository {
        return UserRepositoryImpl(api, userDAO, remoteMediator)
    }
}
