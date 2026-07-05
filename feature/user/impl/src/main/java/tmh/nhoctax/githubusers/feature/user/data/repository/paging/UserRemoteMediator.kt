package tmh.nhoctax.githubusers.feature.user.data.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import tmh.nhoctax.githubusers.core.database.dao.UserDAO
import tmh.nhoctax.githubusers.core.database.model.UserEntity
import tmh.nhoctax.githubusers.feature.user.data.datastore.UserPreferencesDataStore
import tmh.nhoctax.githubusers.feature.user.data.mapper.toUserEntity
import tmh.nhoctax.githubusers.feature.user.data.remote.UserApi
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator @Inject constructor(
    private val api: UserApi,
    private val userDAO: UserDAO,
    private val dataStore: UserPreferencesDataStore
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
                dataStore.updateLastTimeToSaveUsers(System.currentTimeMillis())
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

    // This function inside a RemoteMediator is used to decide whether the application should trigger a network request to refresh the data immediately when pager is first loaded,
    // or whether it should skip the initial refresh and rely entirely on the locally cached data.
    // This function returns an InitializeAction, which has two possible values:
    // 1. InitializeAction.LAUNCH_INITIAL_REFRESH: This tells Paging 3 to immediately trigger the load() function
    // with LoadType.REFRESH as soon as the Pager is initialized. You use this flag when your local database is empty,
    // or when you determine that the local data is stale and needs to be updated from the server.
    // (This is also the default behavior if you just return super.initialize()).

    // 2. InitializeAction.SKIP_INITIAL_REFRESH: This tells Paging 3 to skip the initial API call.
    // Paging 3 will load and display whatever data is currently available in the local database.
    // A network request (triggering the load function) will only happen later when the user scrolls to the bottom of the list (LoadType.APPEND),
    // scrolls to the top (LoadType.PREPEND), or manually performs a pull-to-refresh

    // Q/A: In Room, if data loaded to end. Paging 3 will continue load from API ?
    // Exactly! That is one of the most powerful features of RemoteMediator in Paging 3 library
    // The user scrolls near the bottom of the list. Based on the prefetchDistance = PAGING_SIZE/2 that config in Pager.
    // When user scrolls close to the last cached item. Paging 3 realizes "We are running out of data"
    override suspend fun initialize(): InitializeAction {
        val currentTimeMillis = System.currentTimeMillis()
        val lastTimeUpdatedUser = dataStore.getLastTimeUpdatedUsers().first()
        val isResetCache = (currentTimeMillis - lastTimeUpdatedUser) >= CACHE_USERS_TIMEOUT
        return if (isResetCache) {
            Timber.d("initialize: InitializeAction.LAUNCH_INITIAL_REFRESH")
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            Timber.d("initialize: InitializeAction.SKIP_INITIAL_REFRESH")
            InitializeAction.SKIP_INITIAL_REFRESH
        }

    }

    companion object {
        private val CACHE_USERS_TIMEOUT = 1 * 1000 * 60 * 60
    }

}