package tmh.nhoctax.githubusers.feature.domain.repository

import tmh.nhoctax.githubusers.feature.domain.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>
    suspend fun searchUsers(query: String): List<User>
}