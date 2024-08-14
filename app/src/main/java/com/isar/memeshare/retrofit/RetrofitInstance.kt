package com.isar.memeshare.retrofit

import com.isar.memeshare.Memes
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object RetrofitInstance{
    private const val BASE_URL =" https://meme-api.com/"

     private fun getInstance(): Retrofit {
        val client = OkHttpClient()
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val clientBuilder: OkHttpClient.Builder =
            client.newBuilder().addInterceptor(interceptor as HttpLoggingInterceptor)

        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientBuilder.build())
            .build()
    }
     fun getApiInterface():ApiInterface {
        return RetrofitInstance.getInstance().create(ApiInterface::class.java)
    }
}


interface ApiInterface {
    @GET("gimme/")
    fun getMemes(): Call<Memes>
}