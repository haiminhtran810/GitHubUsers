package tmh.nhoctax.githubusers.feature.user.data.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import tmh.nhoctax.githubusers.core.database.dao.UserDAO
import tmh.nhoctax.githubusers.core.database.model.UserEntity
import tmh.nhoctax.githubusers.feature.user.data.mapper.toUserEntity
import tmh.nhoctax.githubusers.feature.user.data.remote.UserApi
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
 class UserRemoteMediator @Inject constructor(
    private val api: UserApi,
    private val userDAO: UserDAO
) : RemoteMediator<Int, UserEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        return try {
            val sinceKey = when (loadType) {
                // 1. Initial load or complete data reset
                LoadType.REFRESH -> 0
                // 2. Loading backward (User scrolled to the very top)
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                // 3. Loading forward (User scrolled to the very bottom)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull() ?: return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                    lastItem.id
                }
            }
            val result = api.getUsers(sinceKey, state.config.pageSize)
            val userEntities = result.map { response ->
                response.toUserEntity()
            }
            userDAO.insertAll(userEntities)
            MediatorResult.Success(endOfPaginationReached = result.size < state.config.pageSize)
        } catch (e: Exception) {
            // Tells Paging 3: "Something went wrong (e.g., HTTP 500 or No Internet). Show the error UI."
            MediatorResult.Error(e)
        }
    }

}