package ru.alox1d.vknewsapp.presentation.comments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import ru.alox1d.vknewsapp.R
import ru.alox1d.vknewsapp.domain.entity.FeedPost
import ru.alox1d.vknewsapp.domain.entity.PostComment
import ru.alox1d.vknewsapp.presentation.ViewModelFactory
import ru.alox1d.vknewsapp.presentation.ui.theme.VKNewsAppTheme

@Composable
fun CommentsScreen(
    onBackPressed: () -> Unit,
    feedPost: FeedPost,
    viewModelFactory: ViewModelFactory,
) {
    val viewModel: CommentsViewModel = viewModel(factory = viewModelFactory)
    val screenState = viewModel.screenState.collectAsState(CommentsScreenState.Initial)

    when (val currentState = screenState.value) {
        is CommentsScreenState.Comments -> Comments(currentState, onBackPressed)
        CommentsScreenState.Initial -> Unit
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Comments(currentState: CommentsScreenState.Comments, onBackPressed: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.comments_title))
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackPressed()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 8.dp,
                end = 8.dp,
                bottom = 88.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = currentState.comments,
                key = { it.id }
            ) { comment ->
                CommentItem(comment = comment)
            }
        }
    }
}

@Composable
fun CommentItem(
    comment: PostComment,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 4.dp
            )
    ) {
        AsyncImage(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            model = comment.authorAvatarUrl,
            contentDescription = null
        )
        Spacer(
            modifier = Modifier.width(8.dp)
        )
        Column {
            Text(
                text = comment.authorName,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = comment.commentText,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = comment.publicationDate,
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 12.sp
            )
        }
    }
}

@Preview
@Composable
private fun PreviewComment() {
    VKNewsAppTheme {
        CommentItem(
            comment = PostComment(
                id = 7913,
                authorName = "Marva Hatfield",
                authorAvatarUrl = "http://www.bing.com/search?q=platea",
                commentText = "appareat",
                publicationDate = "porta",
            )
        )
    }
}