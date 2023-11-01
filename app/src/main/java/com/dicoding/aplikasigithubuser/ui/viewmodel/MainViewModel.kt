package com.dicoding.aplikasigithubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.aplikasigithubuser.data.local.UserGithubRepository

class MainViewModel(private val userGithubRepository: UserGithubRepository): ViewModel() {

    companion object{
        var Query = "Muhammad"
    }

    init {
        findUser()
    }

    fun findUser() = userGithubRepository.getUserGithub(Query)

    fun searchUser(username: String) = userGithubRepository.getUserGithub(username)

    fun getThemeSetting(): LiveData<Boolean> {
        return userGithubRepository.getThemeSeting().asLiveData()
    }

}