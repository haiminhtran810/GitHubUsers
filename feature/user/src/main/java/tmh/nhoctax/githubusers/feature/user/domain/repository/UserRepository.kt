package tmh.nhoctax.githubusers.feature.user.domain.repository

import kotlinx.coroutines.flow.Flow
import tmh.nhoctax.githubusers.core.common.model.ResultWrapper
import tmh.nhoctax.githubusers.feature.user.domain.model.User

interface UserRepository {
    suspend fun getUsers(): Flow<ResultWrapper<List<User>>>
    suspend fun getUserDetail(username: String): Flow<ResultWrapper<User>>
}