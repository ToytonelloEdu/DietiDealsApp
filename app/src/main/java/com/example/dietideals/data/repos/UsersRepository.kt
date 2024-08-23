package com.example.dietideals.data.repos

import com.example.dietideals.data.network.NetworkApiService
import com.example.dietideals.data.persistence.daos.OwnUserDao
import com.example.dietideals.domain.AuthenticationUseCase
import com.example.dietideals.domain.models.User

interface UsersRepository {
    suspend fun getOwnUser(): User
    suspend fun getUserByHandle(handle: String): User
    suspend fun updateUser(user: User): User
    suspend fun addUser(user: User): User
    suspend fun deleteUser(user: User)
}

class NetworkUsersRepository(
    private val networkData : NetworkApiService
) : UsersRepository {
    override suspend fun getOwnUser(): User {
        val user = AuthenticationUseCase.user
        if (user != null) {
            return networkData.getUserByHandle(user.username).toUser()
        } else throw IllegalStateException()
    }

    override suspend fun getUserByHandle(handle: String): User =
        networkData.getUserByHandle(handle).toUser()


    override suspend fun updateUser(user: User): User {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun addUser(user: User): User {
        TODO("Not to be implemented")
    }

}

class OfflineUsersRepository(
    private val ownUserDao: OwnUserDao
) : UsersRepository {
    override suspend fun getOwnUser() =
        ownUserDao.getOwnUser().toUser()

    override suspend fun getUserByHandle(handle: String): User {
        TODO("Not yet implemented")
    }

    override suspend fun addUser(user: User): User {
        ownUserDao.insert(user.toDbUser())
        return user
    }

    override suspend fun updateUser(user: User): User {
        ownUserDao.update(user.toDbUser())
        return ownUserDao.getOwnUser().toUser()
    }

    override suspend fun deleteUser(user: User) {
        ownUserDao.delete(user.toDbUser())
    }

}