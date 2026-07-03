package tmh.nhoctax.githubusers.core.security.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmh.nhoctax.githubusers.core.security.AppSecrets
import tmh.nhoctax.githubusers.core.security.AppSecretsImpl

@Module
@InstallIn(SingletonComponent::class)
internal interface SecurityModule {
    @Binds
    fun provideAppSecrets(appSecretsImpl: AppSecretsImpl): AppSecrets
}