package tmh.nhoctax.githubusers.feature.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import tmh.nhoctax.githubusers.feature.data.remote.model.SearchResponseDto
import tmh.nhoctax.githubusers.feature.data.remote.model.UserDto

interface GithubApi {
    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Int? = null,
        @Query("per_page") perPage: Int? = null
    ): List<UserDto>

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null
    ): SearchResponseDto
}