package ru.alox1d.vknewsapp.presentation.comments

import android.app.Application
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.map
import ru.alox1d.vknewsapp.data.repository.NewsFeedRepositoryImpl
import ru.alox1d.vknewsapp.domain.entity.FeedPost
import ru.alox1d.vknewsapp.domain.usecases.GetCommentsUseCase

class CommentsViewModel(
    feedPost: FeedPost,
    application: Application
) : ViewModel() {

    private val repository = NewsFeedRepositoryImpl(application)

    private val getCommentsUseCase = GetCommentsUseCase(repository)

    val screenState = getCommentsUseCase.invoke(feedPost)
        .map {
            CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = it
            )
        }
}
