//package com.smartbus360.app.viewModels
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.smartbus360.app.data.api.ApiHelper
//import com.smartbus360.app.data.database.AlertStatusDao
//import com.smartbus360.app.data.database.AlertStatusEntity
//import com.smartbus360.app.data.model.response.BusReplacedResponse
//import com.smartbus360.app.data.model.response.GetUserDetailResponseX
//import com.smartbus360.app.data.repository.MainRepository
//import com.smartbus360.app.data.repository.PreferencesRepository
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.count
//import kotlinx.coroutines.isActive
//import kotlinx.coroutines.launch
//import org.koin.core.component.KoinComponent
//import org.koin.core.component.inject
//import org.osmdroid.util.GeoPoint
//import retrofit2.HttpException
//import java.io.IOException
//import com.smartbus360.app.data.network.RetrofitBuilder
//
//class StudentScreenViewModel(
//    private val repository: PreferencesRepository
//
//) : ViewModel() ,KoinComponent{
//
//    private val apiService = RetrofitBuilder.createApiService(repository)
//    private val mainRepository: MainRepository = MainRepository(ApiHelper(apiService))
//
//    private val _state = MutableStateFlow(GetUserDetailResponseX())
//    val   state: StateFlow<GetUserDetailResponseX>
//        get() = _state
//
//    private val _stoppages = MutableStateFlow<List<GeoPoint>>(emptyList())
//    val stoppages: StateFlow<List<GeoPoint>>
//        get() = _stoppages
//
//    private val _stoppagesForPolyLines = MutableStateFlow<List<GeoPoint>>(emptyList())
//    val stoppagesForPolyLines: StateFlow<List<GeoPoint>>
//        get() = _stoppagesForPolyLines
//
//    private val _busReplacedStatus = MutableStateFlow(BusReplacedResponse())
//    val   busReplacedStatus: StateFlow<BusReplacedResponse>
//        get() = _busReplacedStatus
//
//
//    val userId = repository.getUserId()
//    private val authToken = repository.getAuthToken()?:"null"
//    private val alertStatusDao: AlertStatusDao by inject()
//    init {
//        viewModelScope.launch {
//
//            // Start polling every 30 seconds
//            launch {
//                while (isActive) {
//                    delay(3_000) // 30 seconds
//                    loadData()
////                    loadStoppages()
//                }
//            }
//            try {
//                val response = mainRepository.getUserDetail2(userId ?: 0, "Bearer $authToken")
//                _state.value = response
//            } catch (e: IOException) {
//                // Handle network or HTTP-related exceptions here
//                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
//                // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
//            } catch (e: Exception) {
//                // Catch any other unexpected exceptions
//                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
////                _state.value = GetUserDetailResponseX(message = e.message)
//                // Handle the error as needed (e.g., display a message or perform a fallback)
//            } catch (e: HttpException) {
//                if (e.code() == 401) {
//                    // Handle unauthorized error
//                    Log.e("Unauthorized", "Authentication failed. Redirect to login.")
//                } else {
//                    // Handle other HTTP errors
//                    Log.e("HttpError", "Error code: ${e.code()}, message: ${e.message}")
//                }
//            }
//
//            try{
//                val response = mainRepository.getBusReplacedStatus(state.value.user?.busId?: 0,"Bearer $authToken")
//                _busReplacedStatus.value = response
//            } catch (e: IOException) {
//                // Handle network or HTTP-related exceptions here
//                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
//                // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
//            } catch (e: Exception) {
//                // Catch any other unexpected exceptions
//                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
//                // Handle the error as needed (e.g., display a message or perform a fallback)
//            } catch (e: HttpException) {
//                if (e.code() == 401) {
//                    // Handle unauthorized error
//                    Log.e("Unauthorized", "Authentication failed. Redirect to login.")
//                } else
//                {
//                    // Handle other HTTP errors
//                    Log.e("HttpError", "Error code: ${e.code()}, message: ${e.message}")
//                }
//            }
//
//            try {
//                // Map the response's stoppages to a list of GeoPoint objects
//
//                val stoppagesList = state.value.routeStoppages?.map { stoppage ->
//                    GeoPoint(stoppage.latitude.toDouble(), stoppage.longitude.toDouble())
//                }
//                val stoppagesForPolyLines = state.value.routeStoppages?.map { stoppage ->
//                    if (stoppage.reached == 1) {
//                        GeoPoint(0.0, 0.0) // Set to 0.0, 0.0 if reached
//                    } else {
//                        GeoPoint(stoppage.latitude.toDouble(), stoppage.longitude.toDouble()) // Use actual values otherwise
//                    }
//                }
//                // Update the stoppages StateFlow with the new list
//                if (stoppagesList != null) {
//                    _stoppages.value = stoppagesList
//                }
//                if (stoppagesForPolyLines != null) {
//                    _stoppagesForPolyLines.value = stoppagesForPolyLines
//                }
//            } catch (e: IOException) {
//            // Handle network or HTTP-related exceptions here
//            Log.e("LOGIN_ERROR", "Network error: ${e.message}")
//            // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
//        } catch (e: Exception) {
//            // Catch any other unexpected exceptions
//            Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
//            // Handle the error as needed (e.g., display a message or perform a fallback)
//        }
//        catch (e: HttpException) {
//            if (e.code() == 401) {
//                // Handle unauthorized error
//                Log.e("Unauthorized", "Authentication failed. Redirect to login.")
//            } else {
//                // Handle other HTTP errors
//                Log.e("HttpError", "Error code: ${e.code()}, message: ${e.message}")
//            }
//        }
//
//
//            try {
//                // Check if the alertStatus table is empty
//                val existingStatus = alertStatusDao.getAlertStatuses() // Assuming this is a function to fetch all records
//
//
//                if (existingStatus.count() == 0) {
//                    // If the table is empty, insert alert status based on stoppage list with default value (false)
//                    stoppages.value.forEachIndexed { stoppage ,_->
//                        alertStatusDao.insertAlertStatus(
//                            AlertStatusEntity(
//                                stopId = stoppage , // Assuming you can use a unique identifier for each stoppage
//                                isEnabled = false
//                            )
//                        )
//                    }
//                } else {
//                    // If not empty, update or insert based on existing alert status
//                    Log.e("ERROR", "Database error")
//                }
//
//            } catch (e: IOException) {
//                // Handle network or HTTP-related exceptions here
//                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
//                // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
//            } catch (e: Exception) {
//                // Catch any other unexpected exceptions
//                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
//                // Handle the error as needed (e.g., display a message or perform a fallback)
//            } catch (e: HttpException) {
//                if (e.code() == 401) {
//                    // Handle unauthorized error
//                    Log.e("Unauthorized", "Authentication failed. Redirect to login.")
//                } else {
//                    // Handle other HTTP errors
//                    Log.e("HttpError", "Error code: ${e.code()}, message: ${e.message}")
//                }
//            }
//
//
//
//    }
//
//    }
//
//    private suspend fun loadStoppages() {
//        try {
//            // Check if the alertStatus table is empty
//            val existingStatus = alertStatusDao.getAlertStatuses() // Assuming this is a function to fetch all records
//
//
//            if (existingStatus.count() == 0) {
//                // If the table is empty, insert alert status based on stoppage list with default value (false)
//                stoppages.value.forEachIndexed { stoppage ,_->
//                    alertStatusDao.insertAlertStatus(
//                        AlertStatusEntity(
//                            stopId = stoppage , // Assuming you can use a unique identifier for each stoppage
//                            isEnabled = false
//                        )
//                    )
//                }
//            } else {
//                // If not empty, update or insert based on existing alert status
//                Log.e("ERROR", "Database error")
//            }
//
//        } catch (e: IOException) {
//            // Handle network or HTTP-related exceptions here
//            Log.e("LOGIN_ERROR", "Network error: ${e.message}")
//            // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
//        } catch (e: Exception) {
//            // Catch any other unexpected exceptions
//            Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
//            // Handle the error as needed (e.g., display a message or perform a fallback)
//        } catch (e: HttpException) {
//            if (e.code() == 401) {
//                // Handle unauthorized error
//                Log.e("Unauthorized", "Authentication failed. Redirect to login.")
//            } else {
//                // Handle other HTTP errors
//                Log.e("HttpError", "Error code: ${e.code()}, message: ${e.message}")
//            }
//        }
//
//    }
//
//
//    fun loadData() {
//        viewModelScope.launch {
//            try {
//                val response = mainRepository.getUserDetail2(userId ?: 0, "Bearer $authToken")
//                _state.value = response
//            } catch (e: IOException) {
//                // Handle network or HTTP-related exceptions here
//                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
//                // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
//            } catch (e: Exception) {
//                // Catch any other unexpected exceptions
//                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
////                _state.value = GetUserDetailResponseX(message = e.message)
//                // Handle the error as needed (e.g., display a message or perform a fallback)
//            } catch (e: HttpException) {
//                if (e.code() == 401) {
//                    // Handle unauthorized error
//                    Log.e("Unauthorized", "Authentication failed. Redirect to login.")
//                } else {
//                    // Handle other HTTP errors
//                    Log.e("HttpError", "Error code: ${e.code()}, message: ${e.message}")
//                }
//            }
//
//            try{
//                val response = mainRepository.getBusReplacedStatus(state.value.user?.busId?: 0,"Bearer $authToken")
//                _busReplacedStatus.value = response
//            } catch (e: IOException) {
//                // Handle network or HTTP-related exceptions here
//                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
//                // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
//            } catch (e: Exception) {
//                // Catch any other unexpected exceptions
//                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
//                // Handle the error as needed (e.g., display a message or perform a fallback)
//            } catch (e: HttpException) {
//                if (e.code() == 401) {
//                    // Handle unauthorized error
//                    Log.e("Unauthorized", "Authentication failed. Redirect to login.")
//                } else
//                {
//                    // Handle other HTTP errors
//                    Log.e("HttpError", "Error code: ${e.code()}, message: ${e.message}")
//                }
//            }
//
//            try {
//                // Map the response's stoppages to a list of GeoPoint objects
//
//                val stoppagesList = state.value.routeStoppages?.map { stoppage ->
//                    GeoPoint(stoppage.latitude.toDouble(), stoppage.longitude.toDouble())
//                }
//                val stoppagesForPolyLines = state.value.routeStoppages?.map { stoppage ->
//                    if (stoppage.reached == 1) {
//                        GeoPoint(0.0, 0.0) // Set to 0.0, 0.0 if reached
//                    } else {
//                        GeoPoint(stoppage.latitude.toDouble(), stoppage.longitude.toDouble()) // Use actual values otherwise
//                    }
//                }
//                // Update the stoppages StateFlow with the new list
//                if (stoppagesList != null) {
//                    _stoppages.value = stoppagesList
//                }
//                if (stoppagesForPolyLines != null) {
//                    _stoppagesForPolyLines.value = stoppagesForPolyLines
//                }
//            } catch (e: IOException) {
//                // Handle network or HTTP-related exceptions here
//                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
//                // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
//            } catch (e: Exception) {
//                // Catch any other unexpected exceptions
//                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
//                // Handle the error as needed (e.g., display a message or perform a fallback)
//            }
//            catch (e: HttpException) {
//                if (e.code() == 401) {
//                    // Handle unauthorized error
//                    Log.e("Unauthorized", "Authentication failed. Redirect to login.")
//                } else {
//                    // Handle other HTTP errors
//                    Log.e("HttpError", "Error code: ${e.code()}, message: ${e.message}")
//                }
//            }
//
//
//            try {
//                // Check if the alertStatus table is empty
//                val existingStatus = alertStatusDao.getAlertStatuses() // Assuming this is a function to fetch all records
//
//
//                if (existingStatus.count() == 0) {
//                    // If the table is empty, insert alert status based on stoppage list with default value (false)
//                    stoppages.value.forEachIndexed { stoppage ,_->
//                        alertStatusDao.insertAlertStatus(
//                            AlertStatusEntity(
//                                stopId = stoppage , // Assuming you can use a unique identifier for each stoppage
//                                isEnabled = false
//                            )
//                        )
//                    }
//                } else {
//                    // If not empty, update or insert based on existing alert status
//                    Log.e("ERROR", "Database error")
//                }
//
//            } catch (e: IOException) {
//                // Handle network or HTTP-related exceptions here
//                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
//                // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
//            } catch (e: Exception) {
//                // Catch any other unexpected exceptions
//                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
//                // Handle the error as needed (e.g., display a message or perform a fallback)
//            } catch (e: HttpException) {
//                if (e.code() == 401) {
//                    // Handle unauthorized error
//                    Log.e("Unauthorized", "Authentication failed. Redirect to login.")
//                } else {
//                    // Handle other HTTP errors
//                    Log.e("HttpError", "Error code: ${e.code()}, message: ${e.message}")
//                }
//            }
//
//
//
//        }
//    }
//
//
//}

package com.smartbus360.app.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbus360.app.data.api.ApiHelper
import com.smartbus360.app.data.database.AlertStatusDao
import com.smartbus360.app.data.database.AlertStatusEntity
import com.smartbus360.app.data.model.response.BusReplacedResponse
import com.smartbus360.app.data.model.response.GetUserDetailResponseX
import com.smartbus360.app.data.network.RetrofitBuilder.apiService
import com.smartbus360.app.data.repository.MainRepository
import com.smartbus360.app.data.repository.PreferencesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.osmdroid.util.GeoPoint
import retrofit2.HttpException
import java.io.IOException
import com.smartbus360.app.data.model.response.MorningX
import com.smartbus360.app.data.model.response.AfternoonX
import com.smartbus360.app.data.model.response.EveningX

class StudentScreenViewModel(private val repository: PreferencesRepository, private val mainRepository: MainRepository = MainRepository(
    ApiHelper(apiService)
)
) : ViewModel(), KoinComponent {
    private val _state = MutableStateFlow(GetUserDetailResponseX())
    val state: StateFlow<GetUserDetailResponseX>
        get() = _state

    private val _stoppages = MutableStateFlow<List<GeoPoint>>(emptyList())
    val stoppages: StateFlow<List<GeoPoint>>
        get() = _stoppages

    private val _stoppagesForPolyLines = MutableStateFlow<List<GeoPoint>>(emptyList())
    val stoppagesForPolyLines: StateFlow<List<GeoPoint>>
        get() = _stoppagesForPolyLines

    private val _busReplacedStatus = MutableStateFlow(BusReplacedResponse())
    val busReplacedStatus: StateFlow<BusReplacedResponse>
        get() = _busReplacedStatus
//    private val _lastStoppageName = MutableStateFlow<String?>(null)
//    val lastStoppageName: StateFlow<String?> = _lastStoppageName

    private val _upcomingStoppageName = MutableStateFlow<String?>(null)
    val upcomingStoppageName: StateFlow<String?> = _upcomingStoppageName

    private val _currentShift = MutableStateFlow("")
    val currentShift: StateFlow<String> = _currentShift

    private val _currentRound = MutableStateFlow(0)
    val currentRound: StateFlow<Int> = _currentRound

    val userId = repository.getUserId()
    private val authToken = repository.getAuthToken() ?: "null"
    private val alertStatusDao: AlertStatusDao by inject()

    init {
        viewModelScope.launch {

            // Start polling every 30 seconds
            launch {
                while (isActive) {
                    delay(3_000) // 30 seconds
                    loadData()
//                    loadStoppages()
                }
            }
            try {
                val response = mainRepository.getUserDetail2(userId ?: 0, "Bearer $authToken")
                _state.value = response
//
                val stoppagesList = response.routeStoppages?.map { stoppage ->
                    GeoPoint(stoppage.latitude.toDouble(), stoppage.longitude.toDouble())
                }
                if (stoppagesList != null) {
                    _stoppages.value = stoppagesList
                }

// 🔥 Now default time will work correctly
                val defaultTime = getFirstStopDefaultTime()
                defaultTime?.let { repository.saveJourneyStartTime(it) }

// 1️⃣ First read backend values

// 2️⃣ Now apply time-based logic (same as driver)
//                val (timeShift, timeRound) = repository.getShiftAndRoundBasedOnTime()

// 3️⃣ Final shift = ALWAYS use time-based
//                _currentShift.value = timeShift
//                _currentRound.value = timeRound

//                Log.d("STUDENT_SHIFT_SYNC", "Backend: $backendShift-$backendRound | TimeBased: $timeShift-$timeRound")

                // ✅ Find the last reached stoppage
//                val lastStop = response.routeStoppages
//                    ?.filter { it.reached == 1 }
//                    ?.maxByOrNull { it.stopOrder }   // or use stopOrder if available
//
//                _lastStoppageName.value = lastStop?.stopName

            } catch (e: IOException) {
                // Handle network or HTTP-related exceptions here
                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
                // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
            } catch (e: Exception) {
                // Catch any other unexpected exceptions
                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
//                _state.value = GetUserDetailResponseX(message = e.message)
                // Handle the error as needed (e.g., display a message or perform a fallback)
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    // Handle unauthorized error
                    Log.e("Unauthorized", "Authentication failed. Redirect to login.")
                } else {
                    // Handle other HTTP errors
                    Log.e("HttpError", "Error code: ${e.code()}, message: ${e.message}")
                }
            }

            try {
                val response = mainRepository.getBusReplacedStatus(
                    state.value.user?.busId ?: 0,
                    "Bearer $authToken"
                )
                _busReplacedStatus.value = response
            } catch (e: IOException) {
                // Handle network or HTTP-related exceptions here
                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
                // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
            } catch (e: Exception) {
                // Catch any other unexpected exceptions
                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
                // Handle the error as needed (e.g., display a message or perform a fallback)
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    // Handle unauthorized error
                    Log.e("Unauthorized", "Authentication failed. Redirect to login.")
                } else {
                    // Handle other HTTP errors
                    Log.e("HttpError", "Error code: ${e.code()}, message: ${e.message}")
                }
            }

            try {
                // Map the response's stoppages to a list of GeoPoint objects

                val stoppagesList = state.value.routeStoppages?.map { stoppage ->
                    GeoPoint(stoppage.latitude.toDouble(), stoppage.longitude.toDouble())
                }
                val stoppagesForPolyLines = state.value.routeStoppages?.map { stoppage ->
                    if (stoppage.reached == 1) {
                        GeoPoint(0.0, 0.0) // Set to 0.0, 0.0 if reached
                    } else {
                        GeoPoint(
                            stoppage.latitude.toDouble(),
                            stoppage.longitude.toDouble()
                        ) // Use actual values otherwise
                    }
                }
                // Update the stoppages StateFlow with the new list
                if (stoppagesList != null) {
                    _stoppages.value = stoppagesList
                }
                if (stoppagesForPolyLines != null) {
                    _stoppagesForPolyLines.value = stoppagesForPolyLines
                }
            } catch (e: IOException) {
                // Handle network or HTTP-related exceptions here
                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
                // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
            } catch (e: Exception) {
                // Catch any other unexpected exceptions
                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
                // Handle the error as needed (e.g., display a message or perform a fallback)
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    // Handle unauthorized error
                    Log.e("Unauthorized", "Authentication failed. Redirect to login.")
                } else {
                    // Handle other HTTP errors
                    Log.e("HttpError", "Error code: ${e.code()}, message: ${e.message}")
                }
            }


            try {
                // Check if the alertStatus table is empty
                val existingStatus =
                    alertStatusDao.getAlertStatuses() // Assuming this is a function to fetch all records


                if (existingStatus.count() == 0) {
                    // If the table is empty, insert alert status based on stoppage list with default value (false)
                    stoppages.value.forEachIndexed { stoppage, _ ->
                        alertStatusDao.insertAlertStatus(
                            AlertStatusEntity(
                                stopId = stoppage, // Assuming you can use a unique identifier for each stoppage
                                isEnabled = false
                            )
                        )
                    }
                } else {
                    // If not empty, update or insert based on existing alert status
                    Log.e("ERROR", "Database error")
                }

            } catch (e: IOException) {
                // Handle network or HTTP-related exceptions here
                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
                // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
            } catch (e: Exception) {
                // Catch any other unexpected exceptions
                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
                // Handle the error as needed (e.g., display a message or perform a fallback)
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    // Handle unauthorized error
                    Log.e("Unauthorized", "Authentication failed. Redirect to login.")
                } else {
                    // Handle other HTTP errors
                    Log.e("HttpError", "Error code: ${e.code()}, message: ${e.message}")
                }
            }


        }

    }

    private fun distanceBetween(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371e3 // meters
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return earthRadius * c
    }

    fun updateUpcomingStoppage(busLat: Double, busLng: Double) {
        val upcomingStop = _state.value.routeStoppages
            ?.filter { it.reached == 0 }   // only unreached stops
            ?.minByOrNull { stoppage ->
                val lat = stoppage.latitude.toDouble()
                val lng = stoppage.longitude.toDouble()
                distanceBetween(busLat, busLng, lat, lng)
            }
        _upcomingStoppageName.value = upcomingStop?.stopName
    }


    private suspend fun loadStoppages() {
        try {
            // Check if the alertStatus table is empty
            val existingStatus =
                alertStatusDao.getAlertStatuses() // Assuming this is a function to fetch all records


            if (existingStatus.count() == 0) {
                // If the table is empty, insert alert status based on stoppage list with default value (false)
                stoppages.value.forEachIndexed { stoppage, _ ->
                    alertStatusDao.insertAlertStatus(
                        AlertStatusEntity(
                            stopId = stoppage, // Assuming you can use a unique identifier for each stoppage
                            isEnabled = false
                        )
                    )
                }
            } else {
                // If not empty, update or insert based on existing alert status
                Log.e("ERROR", "Database error")
            }

        } catch (e: IOException) {
            // Handle network or HTTP-related exceptions here
            Log.e("LOGIN_ERROR", "Network error: ${e.message}")
            // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
        } catch (e: Exception) {
            // Catch any other unexpected exceptions
            Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
            // Handle the error as needed (e.g., display a message or perform a fallback)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                // Handle unauthorized error
                Log.e("Unauthorized", "Authentication failed. Redirect to login.")
            } else {
                // Handle other HTTP errors
                Log.e("HttpError", "Error code: ${e.code()}, message: ${e.message}")
            }
        }

    }


    fun loadData() {
        viewModelScope.launch {
            try {
                val response = mainRepository.getUserDetail2(userId ?: 0, "Bearer $authToken")
                _state.value = response
                // Apply SAME time-based shift logic in polling loop
//                val (timeShift, timeRound) = repository.getShiftAndRoundBasedOnTime()
//                _currentShift.value = timeShift
//                _currentRound.value = timeRound

                val busLat = _stoppages.value.firstOrNull()?.latitude ?: 0.0
                val busLng = _stoppages.value.firstOrNull()?.longitude ?: 0.0
                updateUpcomingStoppage(busLat, busLng)

            } catch (e: IOException) {
                // Handle network or HTTP-related exceptions here
                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
                // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
            } catch (e: Exception) {
                // Catch any other unexpected exceptions
                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
//                _state.value = GetUserDetailResponseX(message = e.message)
                // Handle the error as needed (e.g., display a message or perform a fallback)
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    // Handle unauthorized error
                    Log.e("Unauthorized", "Authentication failed. Redirect to login.")
                } else {
                    // Handle other HTTP errors
                    Log.e("HttpError", "Error code: ${e.code()}, message: ${e.message}")
                }
            }

            try {
                val response = mainRepository.getBusReplacedStatus(
                    state.value.user?.busId ?: 0,
                    "Bearer $authToken"
                )
                _busReplacedStatus.value = response
            } catch (e: IOException) {
                // Handle network or HTTP-related exceptions here
                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
                // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
            } catch (e: Exception) {
                // Catch any other unexpected exceptions
                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
                // Handle the error as needed (e.g., display a message or perform a fallback)
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    // Handle unauthorized error
                    Log.e("Unauthorized", "Authentication failed. Redirect to login.")
                } else {
                    // Handle other HTTP errors
                    Log.e("HttpError", "Error code: ${e.code()}, message: ${e.message}")
                }
            }

            try {
                // Map the response's stoppages to a list of GeoPoint objects

                val stoppagesList = state.value.routeStoppages?.map { stoppage ->
                    GeoPoint(stoppage.latitude.toDouble(), stoppage.longitude.toDouble())
                }
                val stoppagesForPolyLines = state.value.routeStoppages?.map { stoppage ->
                    if (stoppage.reached == 1) {
                        GeoPoint(0.0, 0.0) // Set to 0.0, 0.0 if reached
                    } else {
                        GeoPoint(
                            stoppage.latitude.toDouble(),
                            stoppage.longitude.toDouble()
                        ) // Use actual values otherwise
                    }
                }
                // Update the stoppages StateFlow with the new list
                if (stoppagesList != null) {
                    _stoppages.value = stoppagesList
                }
                if (stoppagesForPolyLines != null) {
                    _stoppagesForPolyLines.value = stoppagesForPolyLines
                }
            } catch (e: IOException) {
                // Handle network or HTTP-related exceptions here
                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
                // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
            } catch (e: Exception) {
                // Catch any other unexpected exceptions
                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
                // Handle the error as needed (e.g., display a message or perform a fallback)
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    // Handle unauthorized error
                    Log.e("Unauthorized", "Authentication failed. Redirect to login.")
                } else {
                    // Handle other HTTP errors
                    Log.e("HttpError", "Error code: ${e.code()}, message: ${e.message}")
                }
            }


            try {
                // Check if the alertStatus table is empty
                val existingStatus =
                    alertStatusDao.getAlertStatuses() // Assuming this is a function to fetch all records


                if (existingStatus.count() == 0) {
                    // If the table is empty, insert alert status based on stoppage list with default value (false)
                    stoppages.value.forEachIndexed { stoppage, _ ->
                        alertStatusDao.insertAlertStatus(
                            AlertStatusEntity(
                                stopId = stoppage, // Assuming you can use a unique identifier for each stoppage
                                isEnabled = false
                            )
                        )
                    }
                } else {
                    // If not empty, update or insert based on existing alert status
                    Log.e("ERROR", "Database error")
                }

            } catch (e: IOException) {
                // Handle network or HTTP-related exceptions here
                Log.e("LOGIN_ERROR", "Network error: ${e.message}")
                // Optionally, update the UI to show an error message (e.g., via a LiveData or StateFlow)
            } catch (e: Exception) {
                // Catch any other unexpected exceptions
                Log.e("LOGIN_ERROR", "Unexpected error: ${e.message}")
                // Handle the error as needed (e.g., display a message or perform a fallback)
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    // Handle unauthorized error
                    Log.e("Unauthorized", "Authentication failed. Redirect to login.")
                } else {
                    // Handle other HTTP errors
                    Log.e("HttpError", "Error code: ${e.code()}, message: ${e.message}")
                }
            }


        }
    }


//    fun getFirstStopDefaultTime(): String? {
//        val phase = state.value.user?.routeCurrentJourneyPhase ?: return null
//        val round = state.value.user?.routeCurrentRound ?: return null
//
//        val firstStop = state.value.routeStoppages?.firstOrNull { stoppage ->
//            stoppage.stopType == phase &&
//                    stoppage.rounds?.let { r ->
//                        val list = when (phase) {
//                            "morning" -> r.morning
//                            "afternoon" -> r.afternoon
//                            else -> r.evening
//                        }
//                        list?.any { it.round == round } == true
//                    } == true &&
//                    stoppage.stopOrder == 1
//        }
//
//        return firstStop?.arrivalTime   // because your model does NOT contain defaultTime
//    }

//    fun getFirstStopDefaultTime(): String? {
//        val phase = state.value.user?.routeCurrentJourneyPhase ?: return null
//        val round = state.value.user?.routeCurrentRound ?: return null
//
//        val firstStop = state.value.routeStoppages?.firstOrNull { stoppage ->
//            stoppage.stopType == phase &&
//                    (
//                            stoppage.rounds?.let { r ->
//                                val list: List<*>? = when (phase) {
//                                    "morning" -> r.morning
//                                    "afternoon" -> r.afternoon
//                                    else -> r.evening
//                                }
//                                list?.any { item ->
//                                    when (phase) {
////                                        "morning" -> (item as? MorningX)?.round == round
////                                        "afternoon" -> (item as? AfternoonX)?.round == round
//                                        else -> (item as? EveningX)?.round == round
//                                    }
//                                } ?: false
//                            } ?: false
//                            ) &&
//                    stoppage.stopOrder == 1
//        }
//
//        return firstStop?.arrivalTime
//    }

    fun getFirstStopDefaultTime(): String? {
        val phase = state.value.user?.routeCurrentJourneyPhase ?: return null
        val round = state.value.user?.routeCurrentRound ?: return null

        val firstStop = state.value.routeStoppages?.firstOrNull { stoppage ->
            stoppage.stopType == phase &&
                    (
                            stoppage.rounds?.let { r ->
                                val list: List<*>? = when (phase) {
                                    "morning" -> r.morning
                                    "afternoon" -> r.afternoon
                                    else -> r.evening
                                }

                                list?.any { item ->
                                    when (phase) {
                                        "morning" -> (item as? MorningX)?.round == round
                                        "afternoon" -> (item as? AfternoonX)?.round == round
                                        else -> (item as? EveningX)?.round == round
                                    }
                                } ?: false
                            } ?: false
                            ) &&
                    stoppage.stopOrder == 1
        }

        return firstStop?.arrivalTime
    }


}