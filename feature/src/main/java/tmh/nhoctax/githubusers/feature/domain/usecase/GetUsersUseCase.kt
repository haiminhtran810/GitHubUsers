package tmh.nhoctax.githubusers.feature.domain.usecase

import tmh.nhoctax.githubusers.feature.domain.model.User
import tmh.nhoctax.githubusers.feature.domain.repository.UserRepository

class GetUsersUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): List<User> {
        return repository.getUsers()
    }
}