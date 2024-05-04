package ru.alox1d.vknewsapp.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import ru.alox1d.vknewsapp.MainViewModel
import ru.alox1d.vknewsapp.domain.PostComment

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(viewModel: MainViewModel, paddingValues: PaddingValues) {
    val feedPosts = viewModel.feedPosts.observeAsState(listOf())

    if (feedPosts.value.isNotEmpty()) {
        val list = mutableListOf<PostComment>()
            .apply {
                repeat(20) {
                    add(
                        PostComment(id = it)
                    )
                }
            }
        CommentsScreen(
            feedPost = feedPosts.value.get(0),
            comments = list
        )
    }

//    LazyColumn(
//        modifier = Modifier.padding(paddingValues = paddingValues),
//        contentPadding = PaddingValues(
//            top = 16.dp,
//            start = 8.dp,
//            end = 8.dp,
//            bottom = 72.dp
//        ),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//
//        items(
//            items = feedPosts.value,
//            key = { it.id }) { feedPost ->
//            val dismissState = rememberDismissState()
//
//            if (dismissState.isDismissed(DismissDirection.EndToStart)) {
//                viewModel.remove(feedPost)
//            }
//
//            SwipeToDismiss(
//                modifier = Modifier.animateItemPlacement(),
//                state = dismissState,
//                directions = setOf(DismissDirection.EndToStart),
//                background = {},
//                dismissContent = {
//                    PostCard(
//                        feedPost = feedPost,
//                        onLikeClickListener = { statisticItem ->
//                            viewModel.updateCount(feedPost, statisticItem)
//                        },
//                        onViewsClickListener = { statisticItem ->
//                            viewModel.updateCount(feedPost, statisticItem)
//                        },
//                        onShareClickListener = { statisticItem ->
//                            viewModel.updateCount(feedPost, statisticItem)
//                        },
//                        onCommentClickListener = { statisticItem ->
//                            viewModel.updateCount(feedPost, statisticItem)
//                        }
//                    )
//                }
//            )
//        }
//    }
}