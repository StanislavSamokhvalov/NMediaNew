package ru.netology.nmedia.repository

import ru.netology.nmedia.api.UserApi
import ru.netology.nmedia.dto.User
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import java.io.IOException
import kotlin.math.log

class AuthRepository {
    suspend fun authUser(login: String, password: String): User {
        try {
            val response = UserApi.retrofitService.updateUser(login, password)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw Exception()
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    suspend fun registerUser(login: String, password: String, name: String): User {
        try {
            val response = UserApi.retrofitService.registerUser(login, password, name)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw Exception()
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}