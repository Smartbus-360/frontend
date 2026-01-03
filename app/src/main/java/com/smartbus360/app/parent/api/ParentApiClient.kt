//package com.smartbus360.app.parent.api
//
//import okhttp3.OkHttpClient
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//object ParentApiClient {
//
//    private const val BASE_URL = "https://erp.smartbus360.com/"
//
//    private val client = OkHttpClient.Builder().build()
//
//    val api: ParentApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(client)
//            .build()
//            .create(ParentApiService::class.java)
//    }
//}

package com.smartbus360.app.parent.api

import android.content.Context
import com.smartbus360.app.data.repository.PreferencesRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ParentApiClient {

    private const val BASE_URL = "https://erp.smartbus360.com/api/"

    private lateinit var prefs: PreferencesRepository

    fun init(context: Context) {
        prefs = PreferencesRepository(context)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val token = prefs.getAuthToken()
            val request = if (!token.isNullOrEmpty() && token != "default_token") {
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .addHeader("Content-Type", "application/json")
                    .build()
            } else {
                chain.request()
            }
            chain.proceed(request)
        }
        .build()

    val api: ParentApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ParentApiService::class.java)
    }
}

