package tmh.nhoctax.githubusers.feature.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import tmh.nhoctax.githubusers.feature.data.remote.GithubApi
import tmh.nhoctax.githubusers.feature.data.repository.UserRepositoryImpl
import tmh.nhoctax.githubusers.feature.domain.repository.UserRepository
import tmh.nhoctax.githubusers.feature.domain.usecase.GetUsersUseCase
import tmh.nhoctax.githubusers.feature.domain.usecase.SearchUsersUseCase
import tmh.nhoctax.githubusers.feature.presentation.GithubUsersViewModel

val featureModule = module {
    single<GithubApi> {
        get<Retrofit>().create(GithubApi::class.java)
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