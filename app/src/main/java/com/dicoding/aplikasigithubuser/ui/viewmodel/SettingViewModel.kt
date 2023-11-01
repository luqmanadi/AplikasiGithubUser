package com.dicoding.aplikasigithubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.aplikasigithubuser.data.local.UserGithubRepository
import kotlinx.coroutines.launch

class SettingViewModel(private val userGithubRepository: UserGithubRepository): ViewModel() {

    fun getThemeSetting(): LiveData<Boolean>{
        return userGithubRepository.getThemeSeting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean){
        viewModelScope.launch {
            userGithubRepository.saveThemeSetting(isDarkModeActive)
        }
    }
}