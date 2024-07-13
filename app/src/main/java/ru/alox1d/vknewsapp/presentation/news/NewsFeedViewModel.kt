package ru.alox1d.vknewsapp.presentation.news

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.alox1d.vknewsapp.data.mapper.NewsFeedMapper
import ru.alox1d.vknewsapp.data.repository.NewsFeedRepository
import ru.alox1d.vknewsapp.domain.FeedPost
import ru.alox1d.vknewsapp.presentation.main.appDataStore

class NewsFeedViewModel(private val application: Application) : AndroidViewModel(application) {

    private val repository = NewsFeedRepository(application)

    private val dataStore = application.applicationContext.appDataStore

    private val initialState = NewsFeedScreenState.Initial

    private val _screenState = MutableLiveData<NewsFeedScreenState>(initialState)
    val screenState: LiveData<NewsFeedScreenState> = _screenState

    private val mapper = NewsFeedMapper()

    init {
        _screenState.value = NewsFeedScreenState.Loading
        loadRecommendations()
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch {
            repository.changeLikeStatus(feedPost)
            _screenState.value = NewsFeedScreenState.Posts(posts = repository.feedPosts)
        }
    }

    fun remove(feedPost: FeedPost) {
        viewModelScope.launch {
            repository.deletePost(feedPost)
            _screenState.value = NewsFeedScreenState.Posts(posts = repository.feedPosts)
        }
    }

    fun loadNextRecommendations() {
        _screenState.value = NewsFeedScreenState.Posts(
            posts = repository.feedPosts,
            nextDataIsLoading = true
        )
        loadRecommendations()
    }

    private fun loadRecommendations() {
        viewModelScope.launch {
            // delay(500) // to visualize of loading next recommendations
            val feedPosts = repository.loadRecommendations()
            _screenState.value = NewsFeedScreenState.Posts(feedPosts)
        }
    }
}