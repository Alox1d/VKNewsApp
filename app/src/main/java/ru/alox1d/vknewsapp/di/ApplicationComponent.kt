package ru.alox1d.vknewsapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.alox1d.vknewsapp.presentation.main.MainActivity

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
//        DomainModule::class,
        ViewModelModule::class,
    ]
)
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun getCommentsScreenComponentFactory(): CommentsScreenComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): ApplicationComponent
    }
}