package com.dicoding.aplikasigithubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.aplikasigithubuser.data.local.UserGithubRepository
import com.dicoding.aplikasigithubuser.data.local.entity.FavoriteUserEntity

class FavoriteViewModel(private val userGithubRepository: UserGithubRepository): ViewModel() {

    fun getUserFavorite(): LiveData<List<FavoriteUserEntity>> = userGithubRepository.getAllFavoriteUser()
}