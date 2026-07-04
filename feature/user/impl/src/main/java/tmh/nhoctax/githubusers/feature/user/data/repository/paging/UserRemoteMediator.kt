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

            val currentUsersRemote = api.getUsers(sinceKey, state.config.pageSize)

            val isRefresh = loadType == LoadType.REFRESH
            // 1. Delete all non-favorite cache items
            if (isRefresh) {
                userDAO.deleteNonFavorites()
            }

            // 2. Fetch current favorite IDs using suspend function from database (not flow!)
            // NOTED: Since favoriteDAO.getFavoriteIds() returns a Flow<List<Int>> (a hot Flow from Room database),
            // calling .toSet() on it is a terminal operator
            // that suspends forever waiting for the Flow to finish
            // (which it never does). This completely blocked the paging coroutine,
            // preventing any network or database cache from ever being mapped
            // or displayed in the UI.
            // Explain:
            // Because fun getFavoriteIds(): Flow<List<Int>> in FavoriteDAO is Hot Flow.
            // . Room establishes an active connection (an observer to the users table)
            // . Every time any data in the Users table changes. Room automatically queries DB again and emit new List<Int>
            // With .toSet() behaves on a Flow, is a Terminal Operator on a Flow
            // When you call flow.toSet(),
            // Kotlin interprets it as: "Please collect all values emitted by this Flow from the beginning until the Flow completes,
            // group them into a Set, and then return it."
            val favoriteIds = userDAO.getFavoriteIds().toSet()

            val userEntities = currentUsersRemote.map { response ->
                response.toUserEntity().copy(
                    isFavorite = favoriteIds.contains(response.id)
                )
            }
            userDAO.insertAll(userEntities)
            MediatorResult.Success(endOfPaginationReached = currentUsersRemote.size < state.config.pageSize)
        } catch (e: Exception) {
            // Tells Paging 3: "Something went wrong (e.g., HTTP 500 or No Internet). Show the error UI."
            MediatorResult.Error(e)
        }
    }

}