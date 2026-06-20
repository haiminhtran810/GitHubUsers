package tmh.nhoctax.githubusers.feature.user.data.repository

import tmh.nhoctax.githubusers.feature.user.data.remote.UserApi
import tmh.nhoctax.githubusers.feature.user.data.remote.model.toDomain
import tmh.nhoctax.githubusers.feature.user.domain.model.User
import tmh.nhoctax.githubusers.feature.user.domain.repository.UserRepository

class UserRepositoryImpl(
    private val api: UserApi
) : UserRepository {
    override suspend fun getUsers(): List<User> {
        return api.getUsers().map { it.toDomain() }
    }

    override suspend fun searchUsers(query: String): List<User> {
        return api.searchUsers(query).items.map { it.toDomain() }
    }

    override suspend fun getUserDetail(username: String): User {
        return api.getUserDetail(username).toDomain()
    }
}