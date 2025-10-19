package com.example.restaurant.repository

import com.example.restaurant.data.dao.UserDao
import com.example.restaurant.data.entity.User
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val userDao: UserDao) {

    suspend fun updateUser(user: User): Result<Unit> {
        return try {
            userDao.updateUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteUser(userId: Int): Result<Unit> {
        return try {
            userDao.deleteUser(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(login: String): User? {
        return userDao.findByLogin(login)
    }
}
