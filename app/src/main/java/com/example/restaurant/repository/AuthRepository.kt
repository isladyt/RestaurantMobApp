package com.example.restaurant.repository

import com.example.restaurant.data.dao.UserDao
import com.example.restaurant.data.entity.User
import at.favre.lib.crypto.bcrypt.BCrypt
import javax.inject.Inject

class AuthRepository @Inject constructor(private val userDao: UserDao) {

    private val bcrypt = BCrypt.with(BCrypt.Version.VERSION_2A)
    private val verifier = BCrypt.verifyer(BCrypt.Version.VERSION_2A)

    // Обновляем метод
    suspend fun registerUser(login: String, name: String, email: String, phone: String, password: String): Result<Unit> {
        return try {
            if (userDao.findByLogin(login) != null) {
                return Result.failure(Exception("User with this login already exists"))
            }
            val passwordHash = bcrypt.hashToString(12, password.toCharArray())
            val user = User(login = login, name = name, email = email, phone = phone, password_hash = passwordHash)
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

            val result = verifier.verify(password.toCharArray(), user.password_hash)
            if (result.verified) {
                Result.success(user)
            } else {
                Result.failure(Exception("Неверный логин или пароль"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
