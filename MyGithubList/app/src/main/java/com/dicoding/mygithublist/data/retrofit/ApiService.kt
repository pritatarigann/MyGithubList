package com.dicoding.mygithublist.data.retrofit


import com.dicoding.mygithublist.data.response.GithubResponse
import com.dicoding.mygithublist.data.response.DetailResponse
import com.dicoding.mygithublist.data.response.ItemsItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun getAllUsers(): Response<List<ItemsItem>>

    @GET("search/users")
    fun search(@Query("q") query: String): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetail(@Path("username") username: String): Call<DetailResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>
}