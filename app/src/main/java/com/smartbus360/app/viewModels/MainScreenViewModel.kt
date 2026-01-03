//package com.smartbus360.app.viewModels
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.smartbus360.app.data.api.ApiHelper
//import com.smartbus360.app.data.model.request.BusReachedStoppageRequest
//import com.smartbus360.app.data.model.request.MarkFinalStopRequest
//import com.smartbus360.app.data.model.response.GetDriverDetailResponseNewXX
//import com.smartbus360.app.data.model.response.GetUserDetailResponseX
//import com.smartbus360.app.data.repository.MainRepository
//import com.smartbus360.app.data.repository.PreferencesRepository
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import retrofit2.HttpException
//import java.io.IOException
//import com.smartbus360.app.data.network.RetrofitBuilder
//
//class MainScreenViewModel (
//    private val repository: PreferencesRepository,
//) : ViewModel(){
//
//    private val apiService = RetrofitBuilder.createApiService(repository)
//
//    private val mainRepository: MainRepository = MainRepository(ApiHelper(apiService))
//
//
//    private val _state = MutableStateFlow(GetDriverDetailResponseNewXX())
//    val   state: StateFlow<GetDriverDetailResponseNewXX>
//        get() = _state
//
//
//    private val _stateException = MutableStateFlow<Exception?>(null)
//    val stateException: StateFlow<Exception?>
//        get() = _stateException
//
//    private val _stateExceptionStatus = MutableStateFlow(false)
//    val stateExceptionStatus: StateFlow<Boolean>
//        get() = _stateExceptionStatus
//
//    private val authToken = repository.getAuthToken() ?: "null"
//
//    init {
//        viewModelScope.launch {
//            try {
//                val userId = repository.getDriverId() ?: null
//
//                val res = mainRepository.getDriverDetailNew(userId ?: 0, "Bearer $authToken")
//                _state.value = res
//            }   catch (e: HttpException) {
//                // Handle HTTP exceptions with specific status codes
//                when (e.code()) {
//                    401->   repository.setLoggedIn(false)
//                    404 ->
//                        e.message
////                                Log.e("Unauthorized", "Authentication failed. Redirect to login.")
//
//                    403 -> Log.e("LOGIN_ERROR", "Access denied (403): ${e.message}")
//                    else -> Log.e("LOGIN_ERROR", "HTTP error: ${e.code()} - ${e.message()}")
//                }
//                _stateException.value = e
//                _stateExceptionStatus.value = true
//            } catch (e: IOException) {
//                // Handle network-related exceptions
//                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
//                _stateException.value = e
//                _stateExceptionStatus.value = true
//            } catch (e: Exception) {
//                // Handle any other exceptions
//                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
//                _stateException.value = e
//                _stateExceptionStatus.value = true
//            }
//
//            catch (e: IOException) {
//                // Handle network or HTTP-related exceptions here
//                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
//                // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
//            } catch (e: Exception) {
//                // Catch any other unexpected exceptions
//                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
//                // Handle the error as needed (e.g., display a message or perform a fallback)
//            }
//        }
//
//    }
//
//    fun markFinalStop(routeId: Int){
//
//        viewModelScope.launch {
//            try{
//                val response = mainRepository.markFinalStop("Bearer $authToken", data= MarkFinalStopRequest(routeId))
//                val res = response
//        } catch (e: IOException) {
//            // Handle network or HTTP-related exceptions here
//            Log.e("LOGIN_ERROR", "Network error: ${e.message}")
//            // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
//        } catch (e: Exception) {
//            // Catch any other unexpected exceptions
//            Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
////                _state.value = GetDriverDetailResponseNewXX(message = e.message)
//            // Handle the error as needed (e.g., display a message or perform a fallback)
//        }
//
//        }
//
//    }
//
//    fun busReachedStoppage(data: BusReachedStoppageRequest)
//    {
//        viewModelScope.launch {
//
//            try {
//                val response = mainRepository.busReachedStoppage( "Bearer $authToken", data)
//                val res = response
////              _state.value = res
//
//            } catch (e: IOException) {
//                // Handle network or HTTP-related exceptions here
//                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
//                // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
//            } catch (e: Exception) {
//                // Catch any other unexpected exceptions
//                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
//                // Handle the error as needed (e.g., display a message or perform a fallback)
//            }
//
//
//        }
//
//    }
//
//}

package com.smartbus360.app.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbus360.app.data.api.ApiHelper
import com.smartbus360.app.data.model.request.BusReachedStoppageRequest
import com.smartbus360.app.data.model.request.MarkFinalStopRequest
import com.smartbus360.app.data.model.response.GetDriverDetailResponseNewXX
import com.smartbus360.app.data.model.response.GetUserDetailResponseX
import com.smartbus360.app.data.network.RetrofitBuilder.apiService
import com.smartbus360.app.data.repository.MainRepository
import com.smartbus360.app.data.repository.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import kotlinx.coroutines.delay
import com.smartbus360.app.data.model.request.UpdateShiftRequest
import com.smartbus360.app.data.model.response.RouteStoppage
import com.smartbus360.app.data.model.response.RouteStoppageXX
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar


class MainScreenViewModel (private val repository: PreferencesRepository, private val mainRepository: MainRepository = MainRepository(
    ApiHelper(apiService)
)
) : ViewModel() {
    private val _state = MutableStateFlow(GetDriverDetailResponseNewXX())
//    val currentPhase: String
//        get() = _state.value.routes?.firstOrNull()?.routeCurrentJourneyPhase ?: "morning"

//    val currentRound: Int
//        get() = _state.value.routes?.firstOrNull()?.routeCurrentRound ?: 1

    val state: StateFlow<GetDriverDetailResponseNewXX>
        get() = _state

//    private var lastShift: String = repository.getShiftAndRoundBasedOnTime().first
//    private var lastRound: Int = repository.getShiftAndRoundBasedOnTime().second

//    private var lastShift: String = ""
//    private var lastRound: Int = 1

    private val _stateException = MutableStateFlow<Exception?>(null)
    val stateException: StateFlow<Exception?>
        get() = _stateException

    private val _stateExceptionStatus = MutableStateFlow(false)
    val stateExceptionStatus: StateFlow<Boolean>
        get() = _stateExceptionStatus

    private val authToken = repository.getAuthToken() ?: "null"

    init {
        viewModelScope.launch {
            try {
                val userId = repository.getDriverId() ?: null

                val res = mainRepository.getDriverDetailNew(userId ?: 0, "Bearer $authToken")
                _state.value = res
//                val shift = res.routes?.firstOrNull()?.routeCurrentJourneyPhase ?: "morning"
//                val round = res.routes?.firstOrNull()?.routeCurrentRound ?: 1
//                repository.saveLiveShift(shift, round)
            } catch (e: HttpException) {
                // Handle HTTP exceptions with specific status codes
                when (e.code()) {
                    401 -> repository.setLoggedIn(false)
                    404 ->
                        e.message
//                                Log.e("Unauthorized", "Authentication failed. Redirect to login.")

                    403 -> Log.e("LOGIN_ERROR", "Access denied (403): ${e.message}")
                    else -> Log.e("LOGIN_ERROR", "HTTP error: ${e.code()} - ${e.message()}")
                }
                _stateException.value = e
                _stateExceptionStatus.value = true
            } catch (e: IOException) {
                // Handle network-related exceptions
                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
                _stateException.value = e
                _stateExceptionStatus.value = true
            } catch (e: Exception) {
                // Handle any other exceptions
                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
                _stateException.value = e
                _stateExceptionStatus.value = true
            } catch (e: IOException) {
                // Handle network or HTTP-related exceptions here
                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
                // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
            } catch (e: Exception) {
                // Catch any other unexpected exceptions
                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
                // Handle the error as needed (e.g., display a message or perform a fallback)
            }
        }
//        watchShiftAutomatically()

    }

    fun markFinalStop(routeId: Int) {

        viewModelScope.launch {
            try {

                val response = mainRepository.markFinalStop(
                    "Bearer $authToken",
                    data = MarkFinalStopRequest(routeId)
                )
                val res = response
            } catch (e: IOException) {
                // 1️⃣ Time Based Protection
//                val defaultTime = repository.getJourneyStartTime()
//                if (!isJourneyFinishAllowed(defaultTime)) {
//                    Log.e("JOURNEY_FINISH", "Blocked: Before scheduled time")
//                    return@launch
//                }

                // Handle network or HTTP-related exceptions here
                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
                // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
            } catch (e: Exception) {
                // Catch any other unexpected exceptions
                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
//                _state.value = GetDriverDetailResponseNewXX(message = e.message)
                // Handle the error as needed (e.g., display a message or perform a fallback)
            }

        }

    }


//    fun markFinalStop(routeId: Int) {
//        viewModelScope.launch {
//
//            val (shift, round) = repository.getShiftAndRoundBasedOnTime()
//
//            // 1️⃣ Update backend shift/round MANUALLY
//            updateShiftToBackend(shift, round)
//            repository.saveLiveShift(shift, round)
//
//
//            // 2️⃣ Now mark final stop
//            try {
//                mainRepository.markFinalStop(
//                    "Bearer $authToken",
//                    MarkFinalStopRequest(routeId)
//                )
//                Log.d("MANUAL_STOP", "Manual STOP sent $shift Round $round")
//            } catch (e: Exception) {
//                Log.e("FINAL_STOP", e.message ?: "")
//            }
//        }
//    }

    fun busReachedStoppage(data: BusReachedStoppageRequest) {
        viewModelScope.launch {

            try {
                val response = mainRepository.busReachedStoppage("Bearer $authToken", data)
                val res = response
//              _state.value = res

            } catch (e: IOException) {
                // Handle network or HTTP-related exceptions here
                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
                // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
            } catch (e: Exception) {
                // Catch any other unexpected exceptions
                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
                // Handle the error as needed (e.g., display a message or perform a fallback)
            }


        }
    }

    //            private fun isJourneyFinishAllowed(defaultTime: String?): Boolean {
//                if (defaultTime == null) return false
//
//                try {
//                    val formatter =
//                        java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
//                    val date = formatter.parse(defaultTime)
//
//                    val cal = java.util.Calendar.getInstance()
//                    cal.set(java.util.Calendar.HOUR_OF_DAY, date.hours)
//                    cal.set(java.util.Calendar.MINUTE, date.minutes)
//                    cal.set(java.util.Calendar.SECOND, date.seconds)
//
//                    val journeyStartMillis = cal.timeInMillis
//                    val now = System.currentTimeMillis()
//
//                    return now >= journeyStartMillis
//                } catch (e: Exception) {
//                    return false
//                }
//            }
    fun autoUpdateShiftAndRound(
        currentShift: String,
        currentRound: Int,
        lastStopTime: String,
        routeId: Int
    ) {
        val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val stopTime = formatter.parse(lastStopTime)
        val cal = Calendar.getInstance().apply { time = stopTime }
        val hour = cal.get(Calendar.HOUR_OF_DAY)

        // Decide next shift
        val nextShift = when {
            hour < 12 -> "morning"
            hour in 12..15 -> "afternoon"
            else -> "evening"
        }

        // Round logic
        val nextRound =
            if (currentRound >= 2) 1      // Reset to Round 1 if Round 2 done
            else currentRound + 1         // Morning R1 → R2, Afternoon R1 → R2

        viewModelScope.launch {
            try {
                val driverId = repository.getDriverId() ?: 0

                // call updateShift API
                mainRepository.updateShift(
                    "Bearer $authToken",
                    UpdateShiftRequest(
                        driverId=driverId,
                        shift = nextShift,
                        round = nextRound
                    )
                )

                // Update SharedPreferences locally
                repository.saveLiveShift(nextShift, nextRound)

                // Refresh UI
                val userId = repository.getDriverId() ?: 0
                val updated = mainRepository.getDriverDetailNew(userId, "Bearer $authToken")
                _state.value = updated

                Log.d("AUTO_SHIFT", "Shift changed to $nextShift R$nextRound")
            } catch (e: Exception) {
                Log.e("AUTO_SHIFT", "Error: ${e.message}")
            }
        }
    }

    fun getLastStopOfCurrentRound(stoppages: List<RouteStoppageXX>, shift: String, round: Int): RouteStoppageXX? {
        val filtered = stoppages.filter { stop ->
            stop.stopType == shift && (
                    when (shift) {
                        "morning"   -> stop.rounds?.morning?.any { it.round == round } == true
                        "afternoon" -> stop.rounds?.afternoon?.any { it.round == round } == true
                        "evening"   -> stop.rounds?.evening?.any { it.round == round } == true
                        else -> false
                    }
                    )
        }
        return filtered.maxByOrNull { it.stopOrder }
        // Last stop
    }


//    private fun startAutoShiftWatcher() {
//        viewModelScope.launch {
//            while (true) {
//
//                val (currentShift, currentRound) = repository.getShiftAndRoundBasedOnTime()
//
//                // Check if shift or round changed
//                if (currentShift != lastShift || currentRound != lastRound) {
//
//                    val routeId = _state.value.routes?.firstOrNull()?.routeId ?: 0
//
//                    if (routeId != 0) {
//                        try {
//                            // AUTO CALL same API as Mark Journey button
//                            mainRepository.markFinalStop(
//                                "Bearer $authToken",
//                                MarkFinalStopRequest(routeId)
//                            )
//
//                            Log.d("AUTO_SHIFT", "Auto STOP sent for $lastShift - Round $lastRound")
//
//                        } catch (e: Exception) {
//                            Log.e("AUTO_SHIFT", "Auto STOP error: ${e.message}")
//                        }
//                    }
//
//                    // UPDATE last shift/round
//                    lastShift = currentShift
//                    lastRound = currentRound
//
//                    // UPDATE shared preferences
////                    repository.setJourneyFinished(currentShift)
////                    repository.setStartedSwitchState
//                    updateShiftToBackend(currentShift, currentRound)
//
//                }
//
//                delay(60_000)  // check every 1 minute
//            }
//        }
//    }
//private fun startAutoShiftWatcher() {
//    viewModelScope.launch {
//        while (true) {
//
////            val (currentShift, currentRound) = repository.getShiftAndRoundBasedOnTime()
//
////            if (currentShift != lastShift || currentRound != lastRound) {
//
//                val routeId = _state.value.routes?.firstOrNull()?.routeId ?: 0
//
//                if (routeId != 0) {
//                    try {
//
//                        // 1️⃣ AUTO SEND: Mark Journey Finished
//                        mainRepository.markFinalStop(
//                            "Bearer $authToken",
//                            MarkFinalStopRequest(routeId)
//                        )
//                        // 2️⃣ FETCH updated driver journey after shift
//                        val userId = repository.getDriverId() ?: 0
//                        val refreshed = mainRepository.getDriverDetailNew(userId, "Bearer $authToken")
//                        _state.value = refreshed
//
//                        Log.d("AUTO_SHIFT", "Auto STOP sent for $currentShift - Round $currentRound")
//
//                        // 2️⃣ AUTO UPDATE BACKEND SHIFT (same as button)
////                        updateShiftToBackend(currentShift, currentRound)
//
//                        // 3️⃣ Save live shift locally
////                        repository.saveLiveShift(currentShift, currentRound)
//
//                    } catch (e: Exception) {
//                        Log.e("AUTO_SHIFT", "Auto STOP error: ${e.message}")
//                    }
//                }

                // 4️⃣ Save new shift state
//                lastShift = currentShift
//                lastRound = currentRound
//            }
//
//            delay(60_000) // check every 1 minute
//        }
//    }
}

//    private fun startAutoShiftWatcher() {
//        viewModelScope.launch {
//            while (true) {
//
//                val (currentShift, currentRound) = repository.getShiftAndRoundBasedOnTime()
//
//                if (currentShift != lastShift || currentRound != lastRound) {
//
//                    val routeId = _state.value.routes?.firstOrNull()?.routeId ?: 0
//
//                    if (routeId != 0) {
//                        try {
//
//                            // 1️⃣ AUTO SEND: Mark Journey Finished
//                            mainRepository.markFinalStop(
//                                "Bearer $authToken",
//                                MarkFinalStopRequest(routeId)
//                            )
//                            Log.d("AUTO_SHIFT", "Auto STOP sent for $currentShift - Round $currentRound")
//
//                            // 2️⃣ VERY IMPORTANT — UPDATE BACKEND SHIFT
//                            updateShiftToBackend(currentShift, currentRound)
//
//                        } catch (e: Exception) {
//                            Log.e("AUTO_SHIFT", "Auto STOP error: ${e.message}")
//                        }
//                    }
//
//                    // 3️⃣ Update local tracking
//                    lastShift = currentShift
//                    lastRound = currentRound
//                }
//
//                delay(60_000) // check every 1 minute
//            }
//        }
//    }

    //    private fun watchShiftAutomatically() {
//        viewModelScope.launch {
//            while (true) {
//                delay(60_000) // Check every 1 minute
//
//                val (newShift, newRound) = repository.getShiftAndRoundBasedOnTime()
//
//                // If shift OR round changed → treat like "Mark Journey Finished"
//                if (newShift != lastShift || newRound != lastRound) {
//
//                    Log.d("AUTO_SHIFT", "Shift changed automatically: $lastShift → $newShift")
//
//                    // 1️⃣ Send auto "Mark Final Stop" to backend
//                    try {
////                        val routeId = _state.value.routeId ?: repository.getJourneyId()
//                        val routeId = _state.value.routes.firstOrNull()?.routeId
//                            ?: repository.getJourneyId()
//
//                        mainRepository.markFinalStop(
//                            "Bearer $authToken",
//                            MarkFinalStopRequest(routeId)
//                        )
//                    } catch (e: Exception) {
//                        Log.e("AUTO_SHIFT", "Auto markFinalStop failed", e)
//                    }
//
//                    // 2️⃣ Update local values
//                    lastShift = newShift
//                    lastRound = newRound
//                    repository.setJourneyRound(newRound)
//
//                    // 3️⃣ Fetch new journey for the new shift
//                    try {
//                        val userId = repository.getDriverId() ?: 0
//                        val res = mainRepository.getDriverDetailNew(userId, "Bearer $authToken")
//                        _state.value = res
//                    } catch (e: Exception) {
//                        Log.e("AUTO_SHIFT", "Auto load new journey failed", e)
//                    }
//                }
//            }
//        }
//    }


