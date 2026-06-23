package tmh.nhoctax.githubusers.feature.user.domain.usecase

import tmh.nhoctax.githubusers.feature.user.domain.model.User
import tmh.nhoctax.githubusers.feature.user.domain.repository.UserRepository
import javax.inject.Inject

class GetUserDetailUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userName: String): User {
        return repository.getUserDetail(userName)
    }
}