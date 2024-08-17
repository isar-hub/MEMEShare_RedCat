package com.isar.memeshare.retrofit

import com.isar.memeshare.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository {

    private val apiService = RetrofitInstance.getApiInterface()
    fun getMemes(callback: (Result<String>) -> Unit) {
        apiService.getMemes().enqueue(object : Callback<MainActivity.Memes>{
            override fun onResponse(call: Call<MainActivity.Memes>, response: Response<MainActivity.Memes>) {
                if (response.isSuccessful) {
                    val url = response.body()?.url ?: ""
                    callback(Result.success(url))
                } else {
                    callback(Result.failure(Throwable("Response error")))
                }
            }

            override fun onFailure(call: Call<MainActivity.Memes>, t: Throwable) {
                callback(Result.failure(t))
            }

        })
    }
}