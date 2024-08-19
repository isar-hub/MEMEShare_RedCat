package com.isar.memeshare.retrofit

import com.isar.memeshare.MainActivity

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

object RetrofitInstance{


     fun getInstance(baseUrl : String): Retrofit {
        val client = OkHttpClient()
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val clientBuilder: OkHttpClient.Builder =
            client.newBuilder().addInterceptor(interceptor as HttpLoggingInterceptor)

        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientBuilder.build())
            .build()
    }

}


interface ApiInterface {
    @GET("gimme/")
    fun getMemes(): Call<MainActivity.Memes>


    @GET
    fun getQuotes(): Call<MainActivity.Memes>
}

