//package com.smartbus360.app.viewModels
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.smartbus360.app.data.api.ApiHelper
//import com.smartbus360.app.data.database.AlertStatusEntity
//import com.smartbus360.app.data.model.response.BusNotificationResponse
//import com.smartbus360.app.data.model.response.GetDriverDetailResponseNewXX
//import com.smartbus360.app.data.model.response.NotificationResponse
//import com.smartbus360.app.data.repository.MainRepository
//import com.smartbus360.app.data.repository.PreferencesRepository
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.count
//import kotlinx.coroutines.launch
//import org.osmdroid.util.GeoPoint
//import retrofit2.HttpException
//import java.io.IOException
//import com.smartbus360.app.data.network.RetrofitBuilder
//
//class NotificationViewModel (
//    private val repository: PreferencesRepository
//) : ViewModel() {
//
//    private val apiService = RetrofitBuilder.createApiService(repository)
//    private val mainRepository: MainRepository = MainRepository(ApiHelper(apiService))
//
//    private val _state = MutableStateFlow(NotificationResponse())
//    val state: StateFlow<NotificationResponse>
//        get() = _state
//
//    private val _stateBusNoti = MutableStateFlow(NotificationResponse())
//    val stateBusNoti: StateFlow<NotificationResponse>
//        get() = _stateBusNoti
//
//
//
//    private val authToken = repository.getAuthToken() ?: "null"
//
//    init {
//        viewModelScope.launch {
//            try {
//
//
//                val res = mainRepository.notifications( "Bearer $authToken")
//                val res2 = mainRepository.busNotifications( "Bearer $authToken")
//                val res1 = res2
//                _stateBusNoti.value = res1
//                _state.value = res
//
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
//        }
//
//    }
//
//    fun loadData() {
//        viewModelScope.launch {
//            try {
//
//
//                val res = mainRepository.notifications( "Bearer $authToken")
//                val res2 = mainRepository.busNotifications( "Bearer $authToken")
//                val res1 = res2
//                _stateBusNoti.value = res1
//                _state.value = res
//
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
//        }
//
//    }
//}

package com.smartbus360.app.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbus360.app.data.api.ApiHelper
import com.smartbus360.app.data.database.AlertStatusEntity
import com.smartbus360.app.data.model.response.BusNotificationResponse
import com.smartbus360.app.data.model.response.GetDriverDetailResponseNewXX
import com.smartbus360.app.data.model.response.NotificationResponse
import com.smartbus360.app.data.network.RetrofitBuilder.apiService
import com.smartbus360.app.data.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import retrofit2.HttpException
import java.io.IOException
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import com.smartbus360.app.data.model.response.Notification
import com.smartbus360.app.data.api.RetrofitInstance
import com.smartbus360.app.MainActivity
import com.smartbus360.app.utility.NotificationHelper
import com.smartbus360.app.data.repository.PreferencesRepository


class NotificationViewModel (private val repository: PreferencesRepository, private val mainRepository:
MainRepository = MainRepository(
        ApiHelper(apiService)
    )
) : ViewModel() {
    private val _state = MutableStateFlow(NotificationResponse())
    val state: StateFlow<NotificationResponse>
        get() = _state

    private val _stateBusNoti = MutableStateFlow(NotificationResponse())
    val stateBusNoti: StateFlow<NotificationResponse>
        get() = _stateBusNoti


//    private val socket: Socket = IO.socket("https://api.smartbus360.com/students")
    private val authToken = repository.getAuthToken() ?: "null"
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications

    init {
        viewModelScope.launch {
            try {


                val res = mainRepository.notifications( "Bearer $authToken")
                val res2 = mainRepository.busNotifications( "Bearer $authToken")
                val res1 = res2
                _stateBusNoti.value = res1
                _state.value = res


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

    fun loadData() {
        viewModelScope.launch {
            try {


                val res = mainRepository.notifications( "Bearer $authToken")
                val res2 = mainRepository.busNotifications( "Bearer $authToken")
                val res1 = res2
                _stateBusNoti.value = res1
                _state.value = res


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
    data class LiveNotification(
        val title: String,
        val message: String,
        val time: String,
        val date: String
    )

    private val _liveNotification = MutableStateFlow<LiveNotification?>(null)
    val liveNotification: StateFlow<LiveNotification?> = _liveNotification

    fun pushNotification(title: String, message: String, time: String, date: String) {
        _liveNotification.value = LiveNotification(title, message, time, date)
    }
    fun clearNotification() {
        _liveNotification.value = null
    }
//    fun connectSocket(studentId: Int) {
//        socket.connect()
//
//        // Join student-specific room
//        socket.emit("registerStudent", studentId)
//
//        // Listen for attendance notifications
//        socket.on("attendance_notification") { args ->
////            if (args.isNotEmpty()) {
////                val data = args[0] as JSONObject
////                val title = data.optString("title")
////                val message = data.optString("message")
////                val time = data.optString("time")
////                val date = data.optString("date")
////
////                val notification = Notification(
////                    id = null,
////                    busId = 0,
////                    message = "$title - $message",
////                    instituteType = "",
////                    status = "$date $time",
////                    expiryDate = null,
////                    createdAt = null,
////                    updatedAt = null,
////                    isMandatory = 0
////                )
////
////                Log.d("SOCKET_NOTIFICATION", "✅ Received: $notification")
////
////                _notifications.value = _notifications.value + notification
////            }
//            if (args.isNotEmpty()) {
//                val data = args[0] as JSONObject
//                val title = data.optString("title")
//                val message = data.optString("message")
//                val time = data.optString("time")
//                val date = data.optString("date")
//
//                // Push to Compose UI (foreground)
//                if (MainActivity.AppState.isForeground) {
//                    _liveNotification.value = LiveNotification(title, message, time, date)
//                }
//                // Show system notification (background)
//                else {
//                    NotificationHelper.showSystemNotification(
//                        context = repository.getContext(),  // ✅ Add context in repository constructor
//                        title = title,
//                        message = "$message\n⏰ $time | 📅 $date"
//                    )
//                    repository.incrementUnreadAttendance()
//
//                }
//            }
//
//        }
//    }

}