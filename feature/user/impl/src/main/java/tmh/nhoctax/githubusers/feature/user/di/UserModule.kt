package tmh.nhoctax.githubusers.feature.user.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import tmh.nhoctax.githubusers.core.database.dao.UserDAO
import tmh.nhoctax.githubusers.feature.user.data.remote.UserApi
import tmh.nhoctax.githubusers.feature.user.data.repository.UserRepositoryImpl
import tmh.nhoctax.githubusers.feature.user.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(api: UserApi, userDAO: UserDAO): UserRepository {
        return UserRepositoryImpl(api, userDAO)
    }
}
