package tmh.nhoctax.githubusers.feature.user.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tmh.nhoctax.githubusers.core.common.model.ResultWrapper
import tmh.nhoctax.githubusers.feature.user.domain.model.User
import tmh.nhoctax.githubusers.feature.user.domain.model.UserDetail

interface UserRepository {
    fun getUsers(): Flow<PagingData<User>>
    fun getUserDetail(username: String): Flow<ResultWrapper<UserDetail>>
}