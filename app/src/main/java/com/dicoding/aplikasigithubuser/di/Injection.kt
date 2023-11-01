package com.dicoding.aplikasigithubuser.di

import android.content.Context
import com.dicoding.aplikasigithubuser.data.local.SettingPreferences
import com.dicoding.aplikasigithubuser.data.local.UserGithubRepository
import com.dicoding.aplikasigithubuser.data.local.dataStore
import com.dicoding.aplikasigithubuser.data.local.room.GithubUserRoomDatabase
import com.dicoding.aplikasigithubuser.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserGithubRepository{
        val apiService =ApiConfig.getApiService()
        val database = GithubUserRoomDatabase.getInstance(context)
        val dao = database.githubUserDao()
        val settingPreferences = SettingPreferences(context.dataStore)
        return UserGithubRepository.getInstance(apiService, dao, settingPreferences)
    }
}