package ru.alox1d.vknewsapp.presentation.news

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.alox1d.vknewsapp.domain.FeedPost
import ru.alox1d.vknewsapp.presentation.ui.theme.Blue

@Composable
fun NewsFeedScreen(
    paddingValues: PaddingValues,
    onCommentsClick: (FeedPost) -> Unit,
) {
    val viewModel: NewsFeedViewModel = viewModel() // = ViewModelProvider(LocalViewModelStoreOwner.current!!)[NewsFeedViewModel::class.java]
    val screenState = viewModel.screenState.collectAsState(NewsFeedScreenState.Initial)

    Column {
        when (val currentState = screenState.value) {
            is NewsFeedScreenState.Posts -> FeedPosts(
                posts = currentState.posts,
                viewModel = viewModel,
                paddingValues = paddingValues,
                onCommentsClick = onCommentsClick,
                nextDataIsLoading = currentState.nextDataIsLoading
            )

            NewsFeedScreenState.Initial -> Unit // do nothing on init state
            NewsFeedScreenState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Blue)
                }
            }
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
private fun FeedPosts(
    posts: List<FeedPost>,
    viewModel: NewsFeedViewModel,
    paddingValues: PaddingValues,
    onCommentsClick: (FeedPost) -> Unit,
    nextDataIsLoading: Boolean,
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues = paddingValues),
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = 16.dp
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
                        onCommentsClickListener = {
                            onCommentsClick(feedPost)
                        },
                        isFavorite = feedPost.isLiked
                    )
                }
            )
        }
        item {
            if (nextDataIsLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Blue)
                }
            } else {
                SideEffect {
                    viewModel.loadNextRecommendations()
                }
            }
        }
    }

}