package tmh.nhoctax.githubusers.feature.user.domain.usecase

import tmh.nhoctax.githubusers.feature.user.domain.model.User
import tmh.nhoctax.githubusers.feature.user.domain.repository.UserRepository
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(query: String): List<User> {
        return repository.searchUsers(query)
    }
}
