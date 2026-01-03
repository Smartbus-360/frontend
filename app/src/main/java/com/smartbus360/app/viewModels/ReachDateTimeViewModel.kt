//package com.smartbus360.app.viewModels
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.smartbus360.app.data.api.ApiHelper
//import com.smartbus360.app.data.model.response.DateLogResponse
//import com.smartbus360.app.data.model.response.GetUserDetailResponseX
//import com.smartbus360.app.data.model.response.NotificationResponse
//import com.smartbus360.app.data.model.response.ReachTimeResponse
//import com.smartbus360.app.data.repository.MainRepository
//import com.smartbus360.app.data.repository.PreferencesRepository
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import retrofit2.HttpException
//import java.io.IOException
//import com.smartbus360.app.data.network.RetrofitBuilder
//
//class ReachDateTimeViewModel (
//    private val repository: PreferencesRepository
//) : ViewModel() {
//
//    private val apiService = RetrofitBuilder.createApiService(repository)
//    private val mainRepository: MainRepository = MainRepository(ApiHelper(apiService))
//
//    private val _state = MutableStateFlow(DateLogResponse())
//    val state: StateFlow<DateLogResponse>
//        get() = _state
//
//    private val _stateUser = MutableStateFlow(GetUserDetailResponseX())
//    val   stateUser: StateFlow<GetUserDetailResponseX>
//        get() = _stateUser
//
//
//    private val authToken = repository.getAuthToken() ?: "null"
//    val userId = repository.getUserId()
//
//    init {
//        viewModelScope.launch {
//            try {
//
//                // Step 1: Get user detail
//                val response = mainRepository.getUserDetail2(userId ?: 0, "Bearer $authToken")
//                _stateUser.value = response
//
//                // Step 2: Get reach times using routeId from user detail
//                val routeId = response.user?.routeId ?: 0
//                val reachTimes = mainRepository.getReachTimes(routeId, "Bearer $authToken")
//                _state.value = reachTimes
//
//            } catch (e: HttpException) {
//                if (e.code() == 401) {
//                    Log.e("Unauthorized", "Authentication failed. Redirect to login.")
//                } else {
//                    Log.e("HttpError", "HTTP ${e.code()}: ${e.message}")
//                }
//            } catch (e: IOException) {
//                Log.e("NetworkError", "Network issue: ${e.message}")
//            } catch (e: Exception) {
//                Log.e("UnexpectedError", "Something went wrong: ${e.message}")
//            }
//        }
//    }
//
//}

package com.smartbus360.app.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbus360.app.data.api.ApiHelper
import com.smartbus360.app.data.model.response.DateLogResponse
import com.smartbus360.app.data.model.response.GetUserDetailResponseX
import com.smartbus360.app.data.model.response.NotificationResponse
import com.smartbus360.app.data.model.response.ReachTimeResponse
import com.smartbus360.app.data.network.RetrofitBuilder.apiService
import com.smartbus360.app.data.repository.MainRepository
import com.smartbus360.app.data.repository.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ReachDateTimeViewModel (private val repository: PreferencesRepository, private val mainRepository:
MainRepository = MainRepository(
        ApiHelper(apiService)
    )
) : ViewModel() {
    private val _state = MutableStateFlow(DateLogResponse())
    val state: StateFlow<DateLogResponse>
        get() = _state

    private val _stateUser = MutableStateFlow(GetUserDetailResponseX())
    val   stateUser: StateFlow<GetUserDetailResponseX>
        get() = _stateUser


    private val authToken = repository.getAuthToken() ?: "null"
    val userId = repository.getUserId()

    init {
        viewModelScope.launch {
            try {

                // Step 1: Get user detail
                val response = mainRepository.getUserDetail2(userId ?: 0, "Bearer $authToken")
                _stateUser.value = response

                // Step 2: Get reach times using routeId from user detail
                val routeId = response.user?.routeId ?: 0
//                val reachTimes = mainRepository.getReachTimes(routeId, "Bearer $authToken")
//                _state.value = reachTimes
                val reachTimes = mainRepository.getReachTimes(routeId, "Bearer $authToken")
                // 🔍 Log all reachDateTime values to check what backend sends
                reachTimes.data.forEach { dateEntry ->
                    dateEntry.stops?.forEach { stop ->
                        Log.d("ReachDateTimeCheck", "Backend reachDateTime: ${stop.reachDateTime}")
                        Log.e(
                            "SOURCE_CHECK",
                            "From reach-times API → ${stop.reachDateTime}"
                        )
                    }
                }

                // --- Generate the 3 allowed dates (Option A) ---
                val today = java.time.LocalDate.now()
                val allowedDates = listOf(
                    today.toString(),              // Today
                    today.minusDays(1).toString(), // Yesterday
                    today.minusDays(2).toString()  // Day before yesterday
                )

// --- Filter backend logs to only these 3 dates ---
                val filteredReachTimes = reachTimes.copy(
                    data = reachTimes.data.filter { allowedDates.contains(it.reachDate) }
                )

// --- Replace null reachDateTime with "Not Yet Reached"
                filteredReachTimes.data.forEach { dateEntry ->
                    dateEntry.stops?.forEach { stop ->
                        if (stop.reachDateTime.isNullOrBlank() || stop.reachDateTime == "null") {
                            stop.reachDateTime = "Not Yet Reached"
                        }
                    }
                }

// --- Assign final data to state ---
                _state.value = filteredReachTimes

                Log.d("ReachTimeViewModel", "Loaded dates: $allowedDates")
                Log.d("ReachTimeViewModel", "Filtered entries: ${filteredReachTimes.data.size}")

// ✅ Filter only today's date (to avoid nulls from old days)
//                val today = java.time.LocalDate.now().toString()
//                val filteredReachTimes = reachTimes.copy(
//                    data = reachTimes.data.filter { it.reachDate == today }
//                )

// ✅ Replace null reachDateTime with "Not Yet Reached"
//                filteredReachTimes.data.forEach { dateEntry ->
//                    dateEntry.stops?.forEach { stop ->
//                        if (stop.reachDateTime.isNullOrBlank()) {
//                            stop.reachDateTime = "Not Yet Reached"
//                        }
//                    }
//                }

// ✅ Assign to state
//                _state.value = filteredReachTimes

                Log.d("ReachTimeViewModel", "Filtered reach times for $today: ${filteredReachTimes.data.size} entries")


            } catch (e: HttpException) {
                if (e.code() == 401) {
                    Log.e("Unauthorized", "Authentication failed. Redirect to login.")
                } else {
                    Log.e("HttpError", "HTTP ${e.code()}: ${e.message}")
                }
            } catch (e: IOException) {
                Log.e("NetworkError", "Network issue: ${e.message}")
            } catch (e: Exception) {
                Log.e("UnexpectedError", "Something went wrong: ${e.message}")
            }
        }
    }

}