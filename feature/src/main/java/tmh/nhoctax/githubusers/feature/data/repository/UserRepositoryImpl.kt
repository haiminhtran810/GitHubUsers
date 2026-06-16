package tmh.nhoctax.githubusers.feature.data.repository

import tmh.nhoctax.githubusers.feature.data.remote.GithubApi
import tmh.nhoctax.githubusers.feature.data.remote.model.toDomain
import tmh.nhoctax.githubusers.feature.domain.model.User
import tmh.nhoctax.githubusers.feature.domain.repository.UserRepository

class UserRepositoryImpl(
    private val api: GithubApi
) : UserRepository {
    override suspend fun getUsers(): List<User> {
        return api.getUsers().map { it.toDomain() }
    }

    override suspend fun searchUsers(query: String): List<User> {
        return api.searchUsers(query).items.map { it.toDomain() }
    }
}