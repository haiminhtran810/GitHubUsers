package tmh.nhoctax.githubusers.feature.user.domain.repository

import tmh.nhoctax.githubusers.feature.user.domain.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>
    suspend fun searchUsers(query: String): List<User>
    suspend fun getUserDetail(username: String): User
}