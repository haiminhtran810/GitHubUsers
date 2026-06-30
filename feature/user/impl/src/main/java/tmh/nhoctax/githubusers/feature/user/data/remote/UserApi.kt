package tmh.nhoctax.githubusers.feature.user.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import tmh.nhoctax.githubusers.feature.user.data.remote.model.UserDetailResponse
import tmh.nhoctax.githubusers.feature.user.data.remote.model.UserResponse

interface UserApi {
    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Int? = null,
        @Query("per_page") perPage: Int? = null
    ): List<UserResponse>

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String
    ): UserDetailResponse
}