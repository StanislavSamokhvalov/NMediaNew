package ru.netology.nmedia.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface PostRepositoryModule {
    @Binds
    fun bindPostRepository(impl: PostRepositoryImpl): PostRepository
}