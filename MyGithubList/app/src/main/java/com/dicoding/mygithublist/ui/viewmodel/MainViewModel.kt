package com.dicoding.mygithublist.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mygithublist.data.response.GithubResponse
import com.dicoding.mygithublist.data.response.ItemsItem
import com.dicoding.mygithublist.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _notFound= MutableLiveData<Boolean>()
    val notFound: LiveData<Boolean> = _notFound


    companion object{
        private const val TAG = "MainViewModel"
    }

    init{
        getAllUsers()
    }
    private fun getAllUsers() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = ApiConfig.getApiService().getAllUsers()
                if (response.isSuccessful) {
                    val users = response.body()
                    if (users.isNullOrEmpty()) {
                        _notFound.value = true
                    } else {
                        _listUser.value = users
                        _notFound.value = false
                    }
                } else {
                    Log.e(TAG, "Failed to fetch users: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting users: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun search(q: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().search(q)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        val users = response.body()?.items
                        if (users.isNullOrEmpty()) {
                            _notFound.value = true
                        } else {
                            _listUser.value = users
                            _notFound.value = false
                        }
                    }
                } else {
                    throw IllegalAccessException("error")
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

}
