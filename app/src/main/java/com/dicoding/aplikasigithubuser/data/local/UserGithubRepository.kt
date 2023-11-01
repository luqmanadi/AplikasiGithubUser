package com.dicoding.aplikasigithubuser.data.local

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.aplikasigithubuser.data.local.entity.FavoriteUserEntity
import com.dicoding.aplikasigithubuser.data.local.room.GithubUserDao
import com.dicoding.aplikasigithubuser.data.remote.response.DetailUserResponse
import com.dicoding.aplikasigithubuser.data.remote.response.ItemsItem
import com.dicoding.aplikasigithubuser.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class UserGithubRepository(
    private val apiService: ApiService,
    private val githubUserDao: GithubUserDao,
    private val settingPreferences: SettingPreferences
){

    // Fungsi pertama untuk mendapatkan user awalan
    fun getUserGithub(username: String): LiveData<Result<List<ItemsItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUser(username)
            val listUser = response.items
            emit(Result.Success(listUser))
        } catch (e: Exception) {
            Log.d(TAG, "getUserGithub: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    // fungsi untuk mendapatkan data detail user
    fun getDetailUser(username: String): LiveData<Result<DetailUserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailUser(username)
            emit(Result.Success(response))
        } catch (e: Exception){
            Log.d(TAG, "getDetailUser: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    // fungsi untuk mendapatkan daftar follower
    fun getFollower(username: String): LiveData<Result<List<ItemsItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFollowers(username)
            emit(Result.Success(response))
        } catch (e: Exception){
            Log.d(TAG, "getFollowers: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    // fungsi untuk mendapatkan daftar following
    fun getFollowing(username: String): LiveData<Result<List<ItemsItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFollowing(username)
            emit(Result.Success(response))
        } catch (e: Exception){
            Log.d(TAG, "getFollowing: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }
    // fungsi untuk mendapatkan daftar Favorite User
    fun getAllFavoriteUser(): LiveData<List<FavoriteUserEntity>>{
        return githubUserDao.getFavoriteUser()
    }

    // fungsi untuk menambah user menjadi favorite user
    suspend fun setInsertFavoriteUser(githubUser: FavoriteUserEntity){
        coroutineScope {
            launch(Dispatchers.IO){
                githubUserDao.insertGithubUser(githubUser)
            }
        }
    }

    // fungsi untuk menghapus user dari tabel favorite
    suspend fun delete(githubUser: FavoriteUserEntity){
        coroutineScope {
            launch(Dispatchers.IO){
                githubUserDao.delete(githubUser)
            }
        }
    }

    fun getIsFavorite(username: String): LiveData<Boolean> = githubUserDao.isFavoriteUser(username)

    fun getThemeSeting(): Flow<Boolean>{
        return settingPreferences.getThemeSetting()
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) = settingPreferences.saveThemeSetting(isDarkModeActive)



    companion object{

        private const val TAG = "UserGithubRepository"

        @Volatile
        private var instance: UserGithubRepository? = null
        fun getInstance(
            apiService: ApiService,
            githubUserDao: GithubUserDao,
            settingPreferences: SettingPreferences
        ): UserGithubRepository =
            instance ?: synchronized(this){
                instance ?: UserGithubRepository(apiService, githubUserDao, settingPreferences)
            }.also { instance = it }
    }
}