package com.example.restaurant.repository

import com.example.restaurant.data.dao.UserDao
import com.example.restaurant.data.entity.User
import javax.inject.Inject

class AuthRepository @Inject constructor(private val userDao: UserDao) {

    suspend fun registerUser(login: String, name: String, email: String, phone: String, password: String): Result<Unit> {
        return try {
            if (userDao.findByLogin(login) != null) {
                return Result.failure(Exception("User with this login already exists"))
            }
            // Просто сохраняем пароль как есть
            val user = User(login = login, name = name, email = email, phone = phone, password = password)
            userDao.insert(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(login: String, password: String): Result<User> {
        return try {
            val user = userDao.findByLogin(login)
                ?: return Result.failure(Exception("Неверный логин или пароль"))

            // Простое сравнение строк
            if (user.password == password) {
                Result.success(user)
            } else {
                Result.failure(Exception("Неверный логин или пароль"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
