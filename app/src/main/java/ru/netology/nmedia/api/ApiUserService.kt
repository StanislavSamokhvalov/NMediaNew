package ru.netology.nmedia.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.User

class ApiUserService {

    private val logging = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val BASE_URL = "${BuildConfig.BASE_URL}/api/users/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val okhttp = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor { chain ->
            AppAuth.getInstance().authStateFlow.value.token?.let { token ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", token)
                    .build()
                return@addInterceptor chain.proceed(newRequest)
            }
            chain.proceed(chain.request())
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(client)
        .build()


    interface UserApiService {
        @FormUrlEncoded
        @POST("users/authentication")
        suspend fun updateUser(
            @Field("login") login: String?,
            @Field("pass") pass: String?
        ): Response<User>

        @FormUrlEncoded
        @POST("users/registration")
        suspend fun registerUser(
            @Field("login") login: String?,
            @Field("pass") pass: String?,
            @Field("name") name: String?
        ): Response<User>

    }

}

object UserApi {
    val retrofitService: ApiUserService.UserApiService by lazy {
        retrofit.create(ApiUserService.UserApiService::class.java)
    }
}