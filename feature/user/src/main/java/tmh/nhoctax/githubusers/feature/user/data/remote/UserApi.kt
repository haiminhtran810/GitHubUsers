package tmh.nhoctax.githubusers.feature.user.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import tmh.nhoctax.githubusers.feature.user.data.remote.model.UserEntity

interface UserApi {
    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Int? = null,
        @Query("per_page") perPage: Int? = null
    ): List<UserEntity>

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String
    ): UserEntity
}