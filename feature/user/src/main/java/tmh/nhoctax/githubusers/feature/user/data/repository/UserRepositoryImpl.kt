package tmh.nhoctax.githubusers.feature.user.data.repository

import kotlinx.coroutines.flow.Flow
import tmh.nhoctax.githubusers.core.common.model.ResultWrapper
import tmh.nhoctax.githubusers.core.network.base.BaseRepo
import tmh.nhoctax.githubusers.feature.user.data.remote.UserApi
import tmh.nhoctax.githubusers.feature.user.data.remote.model.toDomain
import tmh.nhoctax.githubusers.feature.user.domain.model.User
import tmh.nhoctax.githubusers.feature.user.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi
) : UserRepository, BaseRepo() {
    override suspend fun getUsers(): Flow<ResultWrapper<List<User>>> {
        return safeApiCall {
            api.getUsers().map { it.toDomain() }
        }
    }

    override suspend fun getUserDetail(username: String): Flow<ResultWrapper<User>> {
        return safeApiCall {
            api.getUserDetail(username).toDomain()
        }
    }


}
