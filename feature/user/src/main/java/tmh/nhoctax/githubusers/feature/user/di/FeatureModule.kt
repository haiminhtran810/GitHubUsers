package tmh.nhoctax.githubusers.feature.user.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import tmh.nhoctax.githubusers.feature.user.data.remote.UserApi
import tmh.nhoctax.githubusers.feature.user.data.repository.UserRepositoryImpl
import tmh.nhoctax.githubusers.feature.user.domain.repository.UserRepository
import tmh.nhoctax.githubusers.feature.user.domain.usecase.GetUsersUseCase
import tmh.nhoctax.githubusers.feature.user.domain.usecase.SearchUsersUseCase
import tmh.nhoctax.githubusers.feature.user.presentation.GithubUsersViewModel

val featureModule = module {
    single<UserApi> {
        get<Retrofit>().create(UserApi::class.java)
    }

    single<UserRepository> {
        UserRepositoryImpl(api = get())
    }

    factory {
        GetUsersUseCase(repository = get())
    }

    factory {
        SearchUsersUseCase(repository = get())
    }

    viewModel {
        GithubUsersViewModel(
            getUsersUseCase = get(),
            searchUsersUseCase = get()
        )
    }
}