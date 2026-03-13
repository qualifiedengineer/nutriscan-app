package com.nutriscan.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val USDA_BASE_URL = "https://api.nal.usda.gov/fdc/"
    private const val OPEN_FOOD_FACTS_BASE_URL = "https://world.openfoodfacts.org/"
    const val USDA_API_KEY = "YCozANSyjR9C5wsiD1tNWgDqUMj5QdhuUZAFwtds"
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    
    val usdaApi: USDAApi by lazy {
        Retrofit.Builder()
            .baseUrl(USDA_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(USDAApi::class.java)
    }
    
    val openFoodFactsApi: OpenFoodFactsApi by lazy {
        Retrofit.Builder()
            .baseUrl(OPEN_FOOD_FACTS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenFoodFactsApi::class.java)
    }
}
