package ru.alox1d.vknewsapp.presentation.news

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.alox1d.vknewsapp.data.repository.NewsFeedRepositoryImpl
import ru.alox1d.vknewsapp.domain.entity.FeedPost
import ru.alox1d.vknewsapp.domain.entity.NewsFeedResult
import ru.alox1d.vknewsapp.domain.usecases.ChangeLikeStatusUseCase
import ru.alox1d.vknewsapp.domain.usecases.DeletePostUseCase
import ru.alox1d.vknewsapp.domain.usecases.GetRecommendationsUseCase
import ru.alox1d.vknewsapp.domain.usecases.LoadNextDataUseCase
import ru.alox1d.vknewsapp.extensions.mergeWith

class NewsFeedViewModel(private val application: Application) : AndroidViewModel(application) {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d("NewsFeedViewModel", "Exception in NewsFeedViewModel: ")
    }
    private val repository = NewsFeedRepositoryImpl(application)

    private val getRecommendationsUseCase = GetRecommendationsUseCase(repository)
    private val loadNextDataUseCase = LoadNextDataUseCase(repository)
    private val changeLikeStatusUseCase = ChangeLikeStatusUseCase(repository)
    private val deletePostUseCase = DeletePostUseCase(repository)

    private val recommendationsFlow = getRecommendationsUseCase.invoke()
    private val loadNextDataFlow = MutableSharedFlow<NewsFeedScreenState>()

    val screenState = recommendationsFlow
        .filterIsInstance<NewsFeedResult.Success>()
        .filter { it.posts.isNotEmpty() }
        .map { NewsFeedScreenState.Posts(posts = it.posts) as NewsFeedScreenState }
        .onStart { emit(NewsFeedScreenState.Loading) }
        .mergeWith(loadNextDataFlow)

    fun loadNextRecommendations() {
        viewModelScope.launch {
            loadNextDataFlow.emit(
                NewsFeedScreenState.Posts(
                    posts = (recommendationsFlow.value as NewsFeedResult.Success).posts,
                    nextDataIsLoading = true,
                )
            )
            loadNextDataUseCase.invoke()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            changeLikeStatusUseCase.invoke(feedPost)
        }
    }

    fun remove(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            deletePostUseCase.invoke(feedPost)
        }
    }
}