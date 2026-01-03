//package com.smartbus360.app.data.api
//
//
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.PUT
//import retrofit2.http.Path
//import retrofit2.Response
//import retrofit2.http.Body
//
//
//object RetrofitInstance {
//    private const val BASE_URL = "https://api.smartbus360.com/api/"
//
//    val api: AdminApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(AdminApiService::class.java)
//    }
//
//
//}
//

package com.smartbus360.app.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.smartbus360.app.data.api.ApiService

object RetrofitInstance {

    private const val BASE_URL = "https://api.smartbus360.com/api/"

    private val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()

    // ✅ Single Retrofit instance
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ✅ Admin API (used in AdminDashboardScreen)
    val adminApi: AdminApiService by lazy {
        retrofit.create(AdminApiService::class.java)
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    // ✅ (Optional) other API services can be added here later
    // val driverApi: DriverApiService by lazy { retrofit.create(DriverApiService::class.java) }
}
