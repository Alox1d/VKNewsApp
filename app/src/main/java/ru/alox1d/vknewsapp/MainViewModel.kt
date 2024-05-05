package ru.alox1d.vknewsapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.alox1d.vknewsapp.domain.FeedPost
import ru.alox1d.vknewsapp.domain.StatisticItem
import ru.alox1d.vknewsapp.ui.HomeScreenState
import java.util.Collections.replaceAll

class MainViewModel : ViewModel() {

    private val initialList = mutableListOf<FeedPost>().apply {
        repeat(10) {
            add(FeedPost(id = it))
        }
    }
    private val initialState = HomeScreenState.Posts(posts = initialList)

    private val _screenState = MutableLiveData<HomeScreenState>(initialState)
    val screenState: LiveData<HomeScreenState> = _screenState

    fun updateCount(feedPost: FeedPost, item: StatisticItem) {
        val oldPosts = screenState.value?.toMutableList() ?: throw IllegalStateException("FeedPosts mustn't be null")
        val oldStatistics = feedPost.statistics
        val newStatistics = oldStatistics.toMutableList().apply {
            replaceAll { oldItem ->
                if (oldItem.type == item.type) {
                    oldItem.copy(count = oldItem.count + 1)
                } else {
                    oldItem
                }
            }
        }
        val newFeedPost = feedPost.copy(statistics = newStatistics)
        _screenState.value = oldPosts.apply {
            replaceAll {
                if (feedPost.id == it.id) {
                    newFeedPost
                } else {
                    it
                }
            }
        }
    }

    fun remove(feedPost: FeedPost) {
        val oldPosts = screenState.value?.toMutableList() ?: throw IllegalStateException("FeedPosts mustn't be null")
        oldPosts.remove(feedPost)
        _screenState.value = oldPosts
    }
}