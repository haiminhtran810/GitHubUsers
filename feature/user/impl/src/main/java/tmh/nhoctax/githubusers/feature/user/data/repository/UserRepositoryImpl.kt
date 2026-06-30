package tmh.nhoctax.githubusers.feature.user.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tmh.nhoctax.githubusers.core.common.model.ResultWrapper
import tmh.nhoctax.githubusers.core.network.base.BaseRepo
import tmh.nhoctax.githubusers.feature.user.data.mapper.toDomain
import tmh.nhoctax.githubusers.feature.user.data.remote.UserApi
import tmh.nhoctax.githubusers.feature.user.data.repository.paging.UserPagingSource
import tmh.nhoctax.githubusers.feature.user.domain.model.User
import tmh.nhoctax.githubusers.feature.user.domain.model.UserDetail
import tmh.nhoctax.githubusers.feature.user.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi
) : UserRepository, BaseRepo() {
    @OptIn(ExperimentalPagingApi::class)
    override fun getUsers(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = PAGE_SIZE / 2,
                initialLoadSize = PAGE_SIZE
            ),
            remoteMediator = null,
            pagingSourceFactory = {
                UserPagingSource(api)
            }
        ).flow
    }

    override fun getUserDetail(username: String): Flow<ResultWrapper<UserDetail>> {
        return safeApiCall {
            api.getUserDetail(username).toDomain()
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
