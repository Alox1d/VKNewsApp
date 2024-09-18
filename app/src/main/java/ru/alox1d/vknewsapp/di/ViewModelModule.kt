package ru.alox1d.vknewsapp.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.alox1d.vknewsapp.presentation.comments.CommentsViewModel
import ru.alox1d.vknewsapp.presentation.main.MainViewModel
import ru.alox1d.vknewsapp.presentation.news.NewsFeedViewModel

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(NewsFeedViewModel::class)
    @Binds
    fun bindNewsFeedViewModel(impl: NewsFeedViewModel): ViewModel

    @IntoMap
    @ViewModelKey(CommentsViewModel::class)
    @Binds
    fun bindCommentsViewModel(impl: CommentsViewModel): ViewModel

    @IntoMap
    @ViewModelKey(MainViewModel::class)
    @Binds
    fun bindMainViewModel(impl: MainViewModel): ViewModel
}