package tmh.nhoctax.githubusers.feature.domain.usecase

import tmh.nhoctax.githubusers.feature.domain.model.User
import tmh.nhoctax.githubusers.feature.domain.repository.UserRepository

class SearchUsersUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(query: String): List<User> {
        return repository.searchUsers(query)
    }
}