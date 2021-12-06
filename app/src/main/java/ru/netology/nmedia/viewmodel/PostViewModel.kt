package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.repository.*
import ru.netology.nmedia.util.SingleLiveEvent

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    authorAvatar = "",
    likedByMe = false,
    likes = 0,
    published = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository =
        PostRepositoryImpl(AppDb.getInstance(application).postDao())
    private val _data = repository.data.map { FeedModel(posts = it, empty = it.isEmpty()) }
    val data: LiveData<FeedModel>
        get() = _data
    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() = viewModelScope.launch {
        try {
            _dataState.postValue(FeedModelState(loading = true))
            repository.getAll()
            _dataState.postValue(FeedModelState())
        } catch (e: Exception) {
            _dataState.postValue(FeedModelState(error = true))
        }
    }

    fun refreshPosts() = viewModelScope.launch {
        try {
            _dataState.postValue(FeedModelState(refreshing = true))
            repository.getAll()
            _dataState.postValue(FeedModelState())
        } catch (e: Exception) {
            _dataState.postValue(FeedModelState(error = true))
        }
    }

    fun save() {
        edited.value?.let {
            _postCreated.value = Unit
            viewModelScope.launch {
                try {
                    repository.save(it)
                    _dataState.value = FeedModelState()
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                }
            }
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }


    fun likeById(id: Long) = viewModelScope.launch {
        try {
            repository.likeById(id)
        } catch (e: Exception) {
            _dataState.postValue(FeedModelState(error = true))
        }
    }

    fun unlikeById(id: Long) = viewModelScope.launch {
        try {
            repository.unlikeById(id)
        } catch (e: Exception) {
            _dataState.postValue(FeedModelState(error = true))
        }
    }

    fun removeById(id: Long) = viewModelScope.launch {
        try {
            repository.removeById(id)
        } catch (e: Exception) {
            _dataState.postValue(FeedModelState(error = true))
        }
    }

}
