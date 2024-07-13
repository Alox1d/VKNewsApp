package ru.alox1d.vknewsapp.presentation.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.alox1d.vknewsapp.R
import ru.alox1d.vknewsapp.domain.FeedPost
import ru.alox1d.vknewsapp.domain.StatisticItem
import ru.alox1d.vknewsapp.domain.StatisticType
import ru.alox1d.vknewsapp.presentation.ui.theme.VKNewsAppTheme

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    feedPost: FeedPost,
    onLikeClickListener: (StatisticItem) -> Unit,
    onCommentsClickListener: (StatisticItem) -> Unit,
    isFavorite: Boolean,
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            PostHeader(feedPost)
            Spacer(modifier = Modifier.height(8.dp))
            PostContent(feedPost)
            Spacer(modifier = Modifier.height(8.dp))
            PostStatistics(
                statistics = feedPost.statistics,
                onCommentClickListener = onCommentsClickListener,
                onLikeClickListener = onLikeClickListener,
                isFavorite = isFavorite,
            )
        }
    }
}

@Composable
private fun PostContent(feedPost: FeedPost) {
    Text(
        text = feedPost.contextText
    )
    Spacer(modifier = Modifier.height(8.dp))
    AsyncImage(
        model = feedPost.contentImageUrl,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        // painter = painterResource(id = feedPost.contentImageUrl),
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}

@Composable
private fun PostHeader(feedPost: FeedPost) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = feedPost.contentImageUrl,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            // painter = painterResource(id = feedPost.communityImageUrl),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = feedPost.communityName,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = feedPost.publicationDate,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
private fun PostStatistics(
    statistics: List<StatisticItem>,
    onCommentClickListener: (StatisticItem) -> Unit,
    onLikeClickListener: (StatisticItem) -> Unit,
    isFavorite: Boolean,
) {
    Row {
        Row(
            modifier = Modifier.weight(1f)
        ) {
            val viewsItem = statistics.getItemByType(StatisticType.VIEWS)
            IconWithText(
                iconResId = R.drawable.ic_views_count,
                text = formatStatisticCount(viewsItem.count)
            )
        }
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val sharesItem = statistics.getItemByType(StatisticType.SHARES)
            val commentsItem = statistics.getItemByType(StatisticType.COMMENTS)
            val likesItem = statistics.getItemByType(StatisticType.LIKES)

            IconWithText(
                iconResId = R.drawable.ic_share,
                text = formatStatisticCount(sharesItem.count)
            )
            IconWithText(
                iconResId = R.drawable.ic_comment,
                text = formatStatisticCount(commentsItem.count)
            ) {
                onCommentClickListener(commentsItem)
            }
            IconWithText(
                iconResId = if (isFavorite) R.drawable.ic_like_set else R.drawable.ic_like,
                text = formatStatisticCount(likesItem.count),
                tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSecondary
            ) {
                onLikeClickListener(likesItem)
            }
        }
    }
}

private fun formatStatisticCount(count: Int): String {
    return if (count > 100_000) {
        String.format(
            "%sK",
            (count / 1000)
        )
    } else if (count > 1000) {
        String.format(
            "%.1fK",
            (count / 1000f)
        )
    } else {
        count.toString()
    }
}

private fun List<StatisticItem>.getItemByType(type: StatisticType): StatisticItem {
    return this.find { it.type == type } ?: throw IllegalStateException("StatisticType isn't found")
}

@Composable
private fun IconWithText(
    iconResId: Int,
    text: String,
    tint: Color = MaterialTheme.colorScheme.onSecondary,
    onItemClickListener: (() -> Unit)? = null,
) {
    val modifier = if (onItemClickListener == null) {
        Modifier
    } else {
        Modifier.clickable(onClick = onItemClickListener)
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = tint
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Preview
@Composable
private fun PreviewLight() {
    VKNewsAppTheme(darkTheme = false) {
        PostCard(
            feedPost = FeedPost(
                id = 123,
                communityName = "Mamie Pickett",
                publicationDate = "sociis",
                communityImageUrl = "https://search.yahoo.com/search?p=fermentum",
                contextText = "pro",
                contentImageUrl = null,
                statistics = listOf(),
                isLiked = true,
                communityId = 5681
            ),
            onLikeClickListener = {},
            onCommentsClickListener = {},
            isFavorite = false
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    VKNewsAppTheme(darkTheme = true) {
        PostCard(
            feedPost = FeedPost(
                id = 123,
                communityName = "Mauricio Leon",
                publicationDate = "amet",
                communityImageUrl = "https://search.yahoo.com/search?p=laudem",
                contextText = "constituto",
                contentImageUrl = null,
                statistics = listOf(),
                isLiked = true,
                communityId = 5171
            ),
            onLikeClickListener = {},
            onCommentsClickListener = {},
            isFavorite = false
        )
    }
}