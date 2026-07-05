package tmh.nhoctax.githubusers.feature.user.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmh.nhoctax.githubusers.core.common.model.ResultWrapper
import tmh.nhoctax.githubusers.core.database.dao.UserDAO
import tmh.nhoctax.githubusers.core.network.base.BaseRepo
import tmh.nhoctax.githubusers.feature.user.data.mapper.toDomain
import tmh.nhoctax.githubusers.feature.user.data.remote.UserApi
import tmh.nhoctax.githubusers.feature.user.data.repository.paging.UserPagingSource
import tmh.nhoctax.githubusers.feature.user.data.repository.paging.UserRemoteMediator
import tmh.nhoctax.githubusers.feature.user.domain.model.User
import tmh.nhoctax.githubusers.feature.user.domain.model.UserDetail
import tmh.nhoctax.githubusers.feature.user.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
    private val userDAO: UserDAO,
    private val remoteMediator: UserRemoteMediator
) : UserRepository, BaseRepo() {
    @OptIn(ExperimentalPagingApi::class)
    override fun getUsers(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,// Items per page
                enablePlaceholders = true,
                prefetchDistance = PAGE_SIZE / 2, // Load next page when PAGE_SIZE / 2 items from bottom
                initialLoadSize = PAGE_SIZE
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = {
                userDAO.getPagingSource()
            }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                entity.toDomain()
            }
        }
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
