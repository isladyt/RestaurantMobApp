package com.example.restaurant.repository

import com.example.restaurant.data.dao.UserDao
import com.example.restaurant.data.entity.User
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class AuthRepositoryTest {

    @Mock
    private lateinit var userDao: UserDao

    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        authRepository = AuthRepository(userDao)
    }

    @Test
    fun `registerUser success`() = runBlocking {
        `when`(userDao.getUserByLogin(anyString())).thenReturn(null)

        val result = authRepository.registerUser("test", "test", "test")

        assert(result.isSuccess)
        verify(userDao).insertUser(any())
    }

    @Test
    fun `registerUser failure user exists`() = runBlocking {
        `when`(userDao.getUserByLogin(anyString())).thenReturn(User(1, "test", "", ""))

        val result = authRepository.registerUser("test", "test", "test")

        assert(result.isFailure)
        verify(userDao, never()).insertUser(any())
    }
}
