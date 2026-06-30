package tmh.nhoctax.githubusers.feature.user.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tmh.nhoctax.githubusers.core.common.model.ResultWrapper
import tmh.nhoctax.githubusers.feature.user.domain.model.User
import tmh.nhoctax.githubusers.feature.user.domain.repository.UserRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<PagingData<User>> {
        return repository.getUsers()
    }
}
