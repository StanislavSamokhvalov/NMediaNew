package ru.netology.nmedia.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.dto.Post

private val logging = HttpLoggingInterceptor().apply {
    if (BuildConfig.DEBUG) {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

private val client = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()

private const val BASE_URL = "${BuildConfig.BASE_URL}/api/slow/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(client)
    .build()


interface ApiPostService {
    @GET("posts")
    suspend fun getAll(): Response<List<Post>>

    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Long): Response<Unit>

    @POST("posts")
    suspend fun save(@Body post: Post): Response<Post>

    @DELETE("posts/{id}/likes")
    suspend fun unlikeById(@Path("id") id: Long): Response<Post>

    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): Response<Post>
}

object PostApi {
    val retrofitService: ApiPostService by lazy {
        retrofit.create(ApiPostService::class.java)
    }
}