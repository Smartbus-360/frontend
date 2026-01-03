//package com.smartbus360.app.data.network
//
//
//import com.smartbus360.app.data.repository.PreferencesRepository
//import com.smartbus360.app.data.model.request.RefreshTokenRequest
//import kotlinx.coroutines.runBlocking
//import okhttp3.Interceptor
//import okhttp3.Response
//import com.smartbus360.app.data.api.ApiService
//import retrofit2.Retrofit
//import retrofit2.http.Body
//import retrofit2.http.POST
//import retrofit2.converter.gson.GsonConverterFactory
//import com.smartbus360.app.data.model.response.RefreshTokenResponse
//
//
//interface RefreshApi {
//    @POST("api/auth/refresh")
//    suspend fun refreshAccessToken(@Body data: RefreshTokenRequest): RefreshTokenResponse
//}
//
//
//class TokenInterceptor(
//    private val preferencesRepository: PreferencesRepository,
//) : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        var request = chain.request()
//        val accessToken = preferencesRepository.getAuthToken()
//
//        if (!accessToken.isNullOrBlank()) {
//            request = request.newBuilder()
//                .addHeader("Authorization", "Bearer $accessToken")
//                .build()
//        }
//
//        var response = chain.proceed(request)
//
//        if (response.code == 401) {
//            response.close()
//
//            val refreshToken = preferencesRepository.getRefreshToken()
//            if (!refreshToken.isNullOrBlank()) {
//                val newAccessToken = runBlocking {
//                    try {
//                        val retrofit = Retrofit.Builder()
//                            .baseUrl("https://api.smartbus360.com/") // same as RetrofitBuilder
//                            .addConverterFactory(GsonConverterFactory.create())
//                            .build()
//
//                        val refreshApi = retrofit.create(RefreshApi::class.java)
//                        val refreshResponse = refreshApi.refreshAccessToken(
//                            RefreshTokenRequest(refreshToken)
//                        )
//                        preferencesRepository.setAuthToken(refreshResponse.accessToken)
//                        preferencesRepository.setRefreshToken(refreshResponse.refreshToken) // <-- ADD THIS
//                        refreshResponse.accessToken
//                    } catch (e: Exception) {
//                        null
//                    }
//                }
//
//                if (!newAccessToken.isNullOrBlank()) {
//                    val newRequest = request.newBuilder()
//                        .header("Authorization", "Bearer $newAccessToken")
//                        .build()
//                    response = chain.proceed(newRequest)
//                }
//            }
//        }
//
//        return response
//    }
//}
