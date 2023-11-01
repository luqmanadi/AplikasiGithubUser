package com.dicoding.aplikasigithubuser.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val username: String
): Parcelable
