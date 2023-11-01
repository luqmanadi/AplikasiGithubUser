package com.dicoding.aplikasigithubuser.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.aplikasigithubuser.data.local.UserGithubRepository
import com.dicoding.aplikasigithubuser.di.Injection

class ViewModelFactory private constructor(private val userGithubRepository: UserGithubRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)){
            return DetailUserViewModel(userGithubRepository) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(userGithubRepository) as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(userGithubRepository) as T
        } else if (modelClass.isAssignableFrom(SettingViewModel::class.java)){
            return SettingViewModel(userGithubRepository) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class : " + modelClass.name)
    }

    companion object{
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this){
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }. also { instance = it }
    }
}