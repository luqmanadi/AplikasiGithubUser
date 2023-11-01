package com.dicoding.aplikasigithubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.aplikasigithubuser.data.local.UserGithubRepository
import com.dicoding.aplikasigithubuser.data.local.entity.FavoriteUserEntity
import kotlinx.coroutines.launch

class DetailUserViewModel(private val userGithubRepository: UserGithubRepository): ViewModel() {

    fun insertUserToFavorite(favoriteUser: FavoriteUserEntity){
        viewModelScope.launch {
            userGithubRepository.setInsertFavoriteUser(favoriteUser)
        }
    }

    fun deleteFavoriteUser(favoriteUser: FavoriteUserEntity){
        viewModelScope.launch {
            userGithubRepository.delete(favoriteUser)
        }
    }

    fun getIsFavorite(username: String): LiveData<Boolean> = userGithubRepository.getIsFavorite(username)

    fun detailUser(username: String) = userGithubRepository.getDetailUser(username)

    fun listFollower(username: String) = userGithubRepository.getFollower(username)

    fun listFollowing(username: String) = userGithubRepository.getFollowing(username)


}