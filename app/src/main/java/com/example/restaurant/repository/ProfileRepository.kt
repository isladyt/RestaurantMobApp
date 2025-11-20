package com.example.restaurant.repository

import com.example.restaurant.data.dao.UserDao
import com.example.restaurant.data.entity.User
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val userDao: UserDao,
    private val authRepository: AuthRepository // Добавляем зависимость
) {

    // Получаем текущего пользователя из AuthRepository
    suspend fun getCurrentUser(): User? {
        return authRepository.getCurrentUser()
    }

    suspend fun updateUser(user: User): Result<Unit> {
        return try {
            userDao.insert(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Исправляем метод, чтобы он принимал User, но передавал ID в DAO
    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user.id) // ИСПРАВЛЕНО
    }
}
