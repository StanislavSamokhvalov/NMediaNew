package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post
import javax.security.auth.callback.Callback

interface PostRepository {
    fun likeByIdAsync(id: Long, getPostCallback: GetPostCallback)
    fun unlikeByIdAsync(id: Long, callback: GetPostCallback)
    fun saveAsync(post: Post, getPostCallback: GetPostCallback)
    fun removeByIdAsync(id: Long, callback: RemoveByIdCallback)
    fun shareByIdAsync(id: Long, callback: GetPostCallback)
    fun getAllAsync(callback: GetAllCallback)

    interface GetAllCallback{
        fun onSuccess(posts: List<Post>)
        fun onError(e: Exception)
    }

    interface RemoveByIdCallback{
        fun onSuccess(id: Long)
        fun onError(e: Exception)
    }

    interface GetPostCallback{
        fun onSuccess(post: Post)
        fun onError(e: Exception)
    }
}
