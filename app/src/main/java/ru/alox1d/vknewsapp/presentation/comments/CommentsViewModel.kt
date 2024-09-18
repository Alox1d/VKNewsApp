package ru.alox1d.vknewsapp.presentation.comments

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.map
import ru.alox1d.vknewsapp.domain.entity.FeedPost
import ru.alox1d.vknewsapp.domain.usecases.GetCommentsUseCase
import javax.inject.Inject

class CommentsViewModel @Inject constructor(
    private val feedPost: FeedPost,
    private val getCommentsUseCase: GetCommentsUseCase,
) : ViewModel() {

    val screenState = getCommentsUseCase.invoke(feedPost)
        .map {
            CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = it
            )
        }
}
