package tmh.nhoctax.githubusers.feature.user.domain.usecase

import kotlinx.coroutines.flow.Flow
import tmh.nhoctax.githubusers.core.common.model.ResultWrapper
import tmh.nhoctax.githubusers.feature.user.domain.model.UserDetail
import tmh.nhoctax.githubusers.feature.user.domain.repository.UserRepository
import javax.inject.Inject

class GetUserDetailUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userName: String): Flow<ResultWrapper<UserDetail>> {
        return repository.getUserDetail(userName)
    }
}