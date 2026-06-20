package tmh.nhoctax.githubusers.feature.user.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import tmh.nhoctax.githubusers.feature.user.data.remote.model.SearchResponseDto
import tmh.nhoctax.githubusers.feature.user.data.remote.model.UserDto

interface UserApi {
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

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String
    ): UserDto
}