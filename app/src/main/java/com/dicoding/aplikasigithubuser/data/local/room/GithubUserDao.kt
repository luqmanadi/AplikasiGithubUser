package com.dicoding.aplikasigithubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.aplikasigithubuser.data.local.entity.FavoriteUserEntity

@Dao
interface GithubUserDao {

    @Query("SELECT * FROM newtablefavoriteuser ORDER BY username DESC ")
    fun getFavoriteUser(): LiveData<List<FavoriteUserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGithubUser(favoriteUser: FavoriteUserEntity)

    @Delete
    suspend fun delete(favoriteUser: FavoriteUserEntity)

    @Query("SELECT EXISTS (SELECT * FROM newtablefavoriteuser WHERE username = :username)")
    fun isFavoriteUser(username: String): LiveData<Boolean>

}