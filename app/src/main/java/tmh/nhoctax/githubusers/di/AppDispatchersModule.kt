package tmh.nhoctax.githubusers.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmh.nhoctax.githubusers.core.common.dispatcher.AppCoroutineDispatchers
import tmh.nhoctax.githubusers.dispatchers.AppAppCoroutineDispatchersImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class AppDispatchersModule {

    @Binds
    abstract fun bindAppCoroutineDispatchers(
        impl: AppAppCoroutineDispatchersImpl
    ): AppCoroutineDispatchers
}