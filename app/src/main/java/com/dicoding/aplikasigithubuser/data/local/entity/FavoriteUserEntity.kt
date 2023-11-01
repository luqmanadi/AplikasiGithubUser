package com.dicoding.aplikasigithubuser.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "NewTableFavoriteUser")
@Parcelize
data class FavoriteUserEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("username")
    var username: String = "",

    @ColumnInfo("avatarUrl")
    var avatarUrl: String? = null
):Parcelable
