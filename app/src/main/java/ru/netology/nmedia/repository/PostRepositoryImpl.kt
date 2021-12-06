package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.dto.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.fromDto
import ru.netology.nmedia.entity.toDto
import ru.netology.nmedia.entity.fromDto
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.AppError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import java.io.IOException
import java.lang.Exception

class PostRepositoryImpl(val postDao: PostDao) : PostRepository {

    override val data: LiveData<List<Post>> = postDao.getAll().map { it.toDto() }

    override suspend fun getAll() {
        try {
            val response = PostApi.retrofitService.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val data = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(data.fromDto())
        } catch (e: IOException) {
            throw NetworkError()
        } catch (e: Exception) {
            throw UnknownError()
        }
    }

    override suspend fun likeById(id: Long) {
        try {
            val response = PostApi.retrofitService.likeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val data = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(data))
        } catch (e: IOException) {
            throw NetworkError()
        } catch (e: Exception) {
            throw UnknownError()
        }
    }

    override suspend fun unlikeById(id: Long) {
        try {
            val response = PostApi.retrofitService.unlikeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val data = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(data))
        } catch (e: IOException) {
            throw NetworkError()
        } catch (e: Exception) {
            throw UnknownError()
        }
    }

    override suspend fun save(post: Post) {
        try {
            val response = PostApi.retrofitService.save(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val data = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(data))
        } catch (e: IOException) {
            throw NetworkError()
        } catch (e: Exception) {
            throw UnknownError()
        }
    }

    override suspend fun removeById(id: Long) {
        try {
            val response = PostApi.retrofitService.removeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            postDao.removeById(id)
        } catch (e: IOException) {
            throw NetworkError()
        } catch (e: Exception) {
            throw UnknownError()
        }
    }
}
