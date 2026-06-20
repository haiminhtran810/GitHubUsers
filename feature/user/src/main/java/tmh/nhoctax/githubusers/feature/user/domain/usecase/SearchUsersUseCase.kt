package tmh.nhoctax.githubusers.feature.user.domain.usecase

import tmh.nhoctax.githubusers.feature.user.domain.model.User
import tmh.nhoctax.githubusers.feature.user.domain.repository.UserRepository

class SearchUsersUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(query: String): List<User> {
        return repository.searchUsers(query)
    }
}