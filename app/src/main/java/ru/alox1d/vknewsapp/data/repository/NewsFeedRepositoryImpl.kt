package ru.alox1d.vknewsapp.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import ru.alox1d.vknewsapp.data.mapper.NewsFeedMapper
import ru.alox1d.vknewsapp.data.network.ApiService
import ru.alox1d.vknewsapp.domain.entity.AuthState
import ru.alox1d.vknewsapp.domain.entity.FeedPost
import ru.alox1d.vknewsapp.domain.entity.NewsFeedResult
import ru.alox1d.vknewsapp.domain.entity.PostComment
import ru.alox1d.vknewsapp.domain.entity.StatisticItem
import ru.alox1d.vknewsapp.domain.entity.StatisticType
import ru.alox1d.vknewsapp.domain.repository.NewsFeedRepository
import ru.alox1d.vknewsapp.extensions.mergeWith
import ru.alox1d.vknewsapp.presentation.main.DataStore.prefsAccessTokenKey
import javax.inject.Inject

class NewsFeedRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val apiService: ApiService,
    private val mapper: NewsFeedMapper,
) : NewsFeedRepository {

    private val coroutinesScope = CoroutineScope(Dispatchers.Default)

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var nextFrom: String? = null

    private val checkAuthStateEvents = MutableSharedFlow<Unit>(replay = 1)
    override val authStateFlow = flow {
        checkAuthStateEvents.emit(Unit) // чтобы отработал 1-ый эмит
        checkAuthStateEvents.collect {
            val token = getAccessToken()

            Log.d(
                "checkAuth",
                "some secret: $token"
            )
            val state = if (token?.isNotEmpty() == true) {
                AuthState.Authorized
            } else {
                AuthState.NotAuthorized
            }
            emit(state)
        }
    }.stateIn(
        scope = coroutinesScope,
        started = SharingStarted.Lazily,
        initialValue = AuthState.Initial
    )

    // НЕ StateFlow, т.к. там distinctUntilChanged -> 1-ый эмит сработает,
    // но последующие эмиты Unit не будут триггерить collect
    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    private val refreshedListFlow = MutableSharedFlow<NewsFeedResult>()
    private val loadedListFlow = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            val token = getAccessToken() ?: throw IllegalStateException("AT is null")
            val startFrom = nextFrom

            if (startFrom == null && feedPosts.isNotEmpty()) {
                emit(feedPosts)
                return@collect
            }

            val response = if (startFrom == null) {
                apiService.loadRecommendations(token)
            } else {
                apiService.loadRecommendations(
                    token = token,
                    startFrom = startFrom
                )
            }
            nextFrom = response.newsFeedContentDto.nextFrom
            val posts = mapper.mapDtoToDomain(response)
            _feedPosts.addAll(posts)

            emit(feedPosts)
        }
    }
        .map { NewsFeedResult.Success(posts = it) as NewsFeedResult }
        .retry {
            delay(RETRY_TIMEOUT_MILLIS)
            true
        }.catch {
            emit(NewsFeedResult.Error)
        }

    override val recommendations: StateFlow<NewsFeedResult> =
        loadedListFlow
            .mergeWith(refreshedListFlow)
            .stateIn(
                scope = coroutinesScope,
                started = SharingStarted.Lazily, // для того, чтобы при 1 подписке началась загрузка, и этот Флоу был всегда готов эмитить данные на протяжении всей работы приложения
                initialValue = NewsFeedResult.Success(feedPosts)
            )

    override suspend fun loadNextData() {
        nextDataNeededEvents.emit(Unit)
    }

    override fun getComments(feedPost: FeedPost): Flow<List<PostComment>> = flow {
        val comments = apiService.getComments(
            accessToken = getAccessToken() ?: throw IllegalStateException("AT is null"),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )
        emit(mapper.mapResponseToComments(comments))
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }

    override suspend fun changeLikeStatus(feedPost: FeedPost) {
        val response = if (feedPost.isLiked) {
            apiService.deleteLike(
                token = getAccessToken() ?: throw IllegalStateException("AT is null"),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        } else {
            apiService.addLike(
                token = getAccessToken() ?: throw IllegalStateException("AT is null"),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        }
        val newLikesCount = response.likes.count
        val newStats = feedPost.statistics.toMutableList()
            .apply {
                removeIf { it.type == StatisticType.LIKES }
                add(
                    StatisticItem(
                        type = StatisticType.LIKES,
                        count = newLikesCount
                    )
                )
            }
        val newPost = feedPost.copy(
            statistics = newStats,
            isLiked = !feedPost.isLiked
        )
        val postIndex = _feedPosts.indexOf(feedPost)
        _feedPosts[postIndex] = newPost
        refreshedListFlow.emit(NewsFeedResult.Success(feedPosts))
    }

    override suspend fun deletePost(feedPost: FeedPost) {
        apiService.ignorePost(
            token = getAccessToken() ?: throw IllegalStateException("AT is null"),
            ownerId = feedPost.communityId,
            postId = feedPost.id,
        )
        _feedPosts.remove(feedPost)
        refreshedListFlow.emit(NewsFeedResult.Success(feedPosts))
    }

    override suspend fun getAccessToken(): String? {
        return dataStore.data.firstOrNull()?.get(prefsAccessTokenKey)
    }

    override suspend fun updateAuthState(token: String?) {
        dataStore.edit { prefs ->
            prefs[prefsAccessTokenKey] = token ?: ""
        }
        checkAuthStateEvents.emit(Unit)
    }

    companion object {
        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }
}