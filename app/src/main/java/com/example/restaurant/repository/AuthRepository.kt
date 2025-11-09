package com.example.restaurant.repository

import com.example.restaurant.data.dao.UserDao
import com.example.restaurant.data.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val userDao: UserDao) {

    // Храним текущего пользователя
    private var currentUser: User? = null

    suspend fun registerUser(login: String, name: String, email: String, phone: String, password: String): Result<Unit> {
        return try {
            if (userDao.findByLogin(login) != null) {
                return Result.failure(Exception("User with this login already exists"))
            }
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

            if (user.password == password) {
                currentUser = user // Сохраняем текущего пользователя
                Result.success(user)
            } else {
                Result.failure(Exception("Неверный логин или пароль"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Новый метод для проверки, является ли текущий пользователь админом
    fun isUserAdmin(): Flow<Boolean> {
        // Просто возвращаем флаг is_admin текущего пользователя
        return kotlinx.coroutines.flow.flow { emit(currentUser?.is_admin ?: false) }
    }
}
