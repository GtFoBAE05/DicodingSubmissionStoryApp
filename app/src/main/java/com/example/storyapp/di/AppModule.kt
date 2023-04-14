package com.example.storyapp.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.storyapp.data.UserPreferences
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.local.room.StoryDatabase
import com.example.storyapp.data.remote.api.ApiConfig
import com.example.storyapp.ui.login.LoginViewModel
import com.example.storyapp.ui.maps.MapsViewModel
import com.example.storyapp.ui.profile.ProfileViewModel
import com.example.storyapp.ui.register.RegisterViewModel
import com.example.storyapp.ui.splashscreen.SplashScreenViewModel
import com.example.storyapp.ui.story.add.AddStoryViewModel
import com.example.storyapp.ui.story.detail.DetailStoryViewModel
import com.example.storyapp.ui.story.list.ListStoryViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val Context.dataStore by preferencesDataStore(name = "user_preferences")


val storyDatabase = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            StoryDatabase::class.java, "story"
        ).fallbackToDestructiveMigration()
            .build()
    }
}

val apiModule = module {
    single { ApiConfig.getApiService() }
}

val userPreferencesModule = module {
    single { UserPreferences(androidContext().dataStore) }
}

val repositoryModule = module {
    single { UserRepository(get(), get(), get()) }
}

val viewModelModule = module {
    viewModel { SplashScreenViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { ListStoryViewModel(get()) }
    viewModel { DetailStoryViewModel(get()) }
    viewModel { AddStoryViewModel(get()) }
    viewModel { MapsViewModel(get()) }

}


