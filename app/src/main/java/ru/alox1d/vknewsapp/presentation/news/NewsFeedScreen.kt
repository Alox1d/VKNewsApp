package ru.alox1d.vknewsapp.presentation.news

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.alox1d.vknewsapp.domain.FeedPost

@Composable
fun NewsFeedScreen(
    paddingValues: PaddingValues,
    onCommentsClick: (FeedPost) -> Unit,
) {
    val viewModel: NewsFeedViewModel = viewModel() // = ViewModelProvider(LocalViewModelStoreOwner.current!!)[NewsFeedViewModel::class.java]
    val screenState = viewModel.screenState.observeAsState(NewsFeedScreenState.Initial)

    Column {
        when (val currentState = screenState.value) {
            is NewsFeedScreenState.Posts -> FeedPosts(
                posts = currentState.posts,
                viewModel = viewModel,
                paddingValues = paddingValues,
                onCommentsClick = onCommentsClick
            )

            NewsFeedScreenState.Initial -> Unit // do nothing on init state
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun FeedPosts(
    posts: List<FeedPost>,
    viewModel: NewsFeedViewModel,
    paddingValues: PaddingValues,
    onCommentsClick: (FeedPost) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues = paddingValues),
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = 72.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(
            items = posts,
            key = { it.id }) { feedPost ->
            val dismissState = rememberSwipeToDismissBoxState()

            if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                viewModel.remove(feedPost)
            }

            SwipeToDismiss(
                modifier = Modifier.animateItemPlacement(),
                state = dismissState,
                directions = setOf(SwipeToDismissBoxValue.EndToStart),
                background = {},
                dismissContent = {
                    PostCard(
                        feedPost = feedPost,
                        onLikeClickListener = { _ ->
                            viewModel.changeLikeStatus(feedPost)
                        },
                        onViewsClickListener = { statisticItem ->
                            viewModel.updateCount(feedPost, statisticItem)
                        },
                        onShareClickListener = { statisticItem ->
                            viewModel.updateCount(feedPost, statisticItem)
                        },
                        onCommentsClickListener = {
                            onCommentsClick(feedPost)
                        },
                        isFavorite = feedPost.isLiked
                    )
                }
            )
        }
    }
}