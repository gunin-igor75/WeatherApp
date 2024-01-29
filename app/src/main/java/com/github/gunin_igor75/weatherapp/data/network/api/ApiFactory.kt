package com.github.gunin_igor75.weatherapp.data.network.api


import com.github.gunin_igor75.weatherapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalStateException
import java.util.Locale

object ApiFactory {
    private const val BASE_URL = "https://api.weatherapi.com/v1/"
    private const val API_KEY = "key"
    private const val PARAM_LANG = "lang"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(apikeyInterceptor())
        .addInterceptor(loggingInterceptor())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val apiService = retrofit.create(ApiService::class.java)?: throw IllegalStateException(
        "ApiService is null"
    )

    private fun loggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private fun apikeyInterceptor(): Interceptor {
        return Interceptor { chain ->
            val oldRequest = chain.request()
            val newUrl = oldRequest.url.newBuilder()
                .addQueryParameter(API_KEY, BuildConfig.WEATHER_API_KEY)
                .addQueryParameter(PARAM_LANG, Locale.getDefault().language)
                .build()
            val newRequest = oldRequest.newBuilder()
                .url(newUrl)
                .build()
            return@Interceptor chain.proceed(newRequest)
        }
    }
}