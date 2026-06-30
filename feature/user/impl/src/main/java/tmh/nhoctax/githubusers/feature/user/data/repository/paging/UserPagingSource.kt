package tmh.nhoctax.githubusers.feature.user.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import tmh.nhoctax.githubusers.feature.user.data.mapper.toDomain
import tmh.nhoctax.githubusers.feature.user.data.remote.UserApi
import tmh.nhoctax.githubusers.feature.user.domain.model.User
import java.io.IOException

class UserPagingSource(
    private val api: UserApi
) : PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        // API use "since" (the last ID User) to paging. Default is 0
        val since = params.key ?: 0

        return try {
            val response = api.getUsers(
                since = since,
                perPage = params.loadSize
            )

            // Map UserResponse to User (Domain layer)
            val users = response.map { it.toDomain() }

            val nextKey = if (users.isEmpty()) {
                null
            } else {
                users.last().id
            }
            LoadResult.Page(
                data = users,
                prevKey = if (since == 0) null else since,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        // return null (start again 0 when refresh list)
        return null
    }
}
