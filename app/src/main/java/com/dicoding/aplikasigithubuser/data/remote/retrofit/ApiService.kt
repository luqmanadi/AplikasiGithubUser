package com.dicoding.aplikasigithubuser.data.remote.retrofit

import com.dicoding.aplikasigithubuser.data.remote.response.DetailUserResponse
import com.dicoding.aplikasigithubuser.data.remote.response.GithubResponse
import com.dicoding.aplikasigithubuser.data.remote.response.ItemsItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/search/users")
    suspend fun getUser(
        @Query("q") q: String
    ): GithubResponse

    @GET("users/{username}")
    suspend fun getDetailUser(
        @Path("username") username: String
    ): DetailUserResponse

    @GET("users/{username}/followers")
    suspend fun getFollowers(
        @Path("username") username: String
    ): List<ItemsItem>

    @GET("users/{username}/following")
    suspend fun getFollowing(
        @Path("username") username: String
    ): List<ItemsItem>

}