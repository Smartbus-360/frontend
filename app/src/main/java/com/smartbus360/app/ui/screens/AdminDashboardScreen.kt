//package com.smartbus360.app.ui.screens
//
//import android.content.Context
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import com.smartbus360.app.ui.theme.Poppins
//import com.smartbus360.app.data.api.RetrofitInstance
//import com.smartbus360.app.data.repository.PreferencesRepository
//import kotlinx.coroutines.launch
//import io.socket.client.IO
//import io.socket.client.Socket
//import org.json.JSONObject
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Refresh
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.activity.compose.BackHandler
//import android.app.Activity
//import io.socket.client.Manager
//import androidx.compose.foundation.ExperimentalFoundationApi
//import androidx.compose.foundation.basicMarquee
//import com.smartbus360.app.data.api.MarkFinalStopNoAuthRequest
//import android.util.Log
//
//
//
//data class BusInfo(
//    val busId: Int,
//    val busNo: String,
//    var driverName: String = "",
//    var driverPhone: String = "",
//    var status: Boolean,
//    var latitude: Double,
//    var longitude: Double,
//    var speed: Double,
//    var placeName: String = "",
//    var routeId: Int = 0 ,  // 👈 NEW FIELD
//    var stopType: String? = null,                // 👈 NEW
//    var routeCurrentRound: Int? = null,          // 👈 NEW
//    var routeCurrentJourneyPhase: String? = null // 👈 NEW
//)
//
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AdminDashboardScreen(
//    navController: NavController
//) {
//    val context = navController.context
//    BackHandler {
//        // Move app to background instead of navigating or logging out
//        (context as? Activity)?.moveTaskToBack(true)
//    }
//
//    var busList by remember { mutableStateOf<List<BusInfo>>(emptyList()) }
//    var isLoading by remember { mutableStateOf(true) }
//    val coroutineScope = rememberCoroutineScope()
//    var isRefreshing by remember { mutableStateOf(false) } // 👈 for the floating pill
//
//    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
//    val token = "Bearer " + (sharedPreferences.getString("ACCESS_TOKEN", "") ?: "")
//
//    // ✅ Socket.IO instance
//    var mSocket: Socket? by remember { mutableStateOf(null) }
//
//    // ✅ Fetch initial bus list (REST API) and subscribe for updates
//    LaunchedEffect(true) {
//        coroutineScope.launch {
//            try {
//                isLoading = true
//                val response = RetrofitInstance.api.getBuses(token)
//                val initialList = response.map {
//                    BusInfo(
//                        busId = it.busId,                  // 👈 use busId, not driverId
//                        busNo = it.busNumber,
//                        driverName = it.driverName ?: "",
//                        driverPhone = "",
//                        status = (it.latitude ?: 0.0 != 0.0 && it.longitude ?: 0.0 != 0.0),
//                        latitude = it.latitude ?: 0.0,
//                        longitude = it.longitude ?: 0.0,
//                        speed = it.speed ?: 0.0,
//                        routeId = it.assignedRouteId ?: 0,
//                        stopType = it.stopType,                          // 👈 NEW
//                        routeCurrentRound = it.routeCurrentRound,        // 👈 NEW
//                        routeCurrentJourneyPhase = it.routeCurrentJourneyPhase // 👈 NEW
//                    )
//                }
//
//
//                busList = initialList
//
//                // ✅ Connect to WebSocket after loading buses
//                val options = IO.Options()
//                options.transports = arrayOf(io.socket.engineio.client.transports.WebSocket.NAME)
//                options.reconnection = true
//                options.forceNew = true
//                options.query = "token=${token.replace("Bearer ", "")}"  // ✅ Pass token
//                mSocket = IO.socket("https://api.smartbus360.com/admin/notification", options)
//
//                mSocket?.connect()
//                mSocket?.on(Socket.EVENT_CONNECT) {
//                    println("✅ Connected to Admin Notification Socket")
//                    val current = busList
//                    current.forEach { bus ->
//                        val data = JSONObject().put("driverId", bus.busId) // send as NUMBER
//                        mSocket?.emit("subscribeToDriver", data)
//                    }
//                }
//// Also handle explicit reconnect events (older Socket.IO clients fire these)
//                mSocket?.io()?.on(Manager.EVENT_RECONNECT) {
//                    val current = busList
//                    current.forEach { bus ->
//                        val data = JSONObject().put("driverId", bus.busId)
//                        mSocket?.emit("subscribeToDriver", data)
//                    }
//                }
//
//
//                mSocket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
//                    println("❌ Socket connection error: ${args.joinToString()}")
//                }
//
//
////                // ✅ Subscribe to each driver room
////                initialList.forEach { bus ->
////                    val data = JSONObject()
////                        .put("driverId", bus.busId)   // ✅ Ensure it's a string
////                    mSocket?.emit("subscribeToDriver", data)  // ✅ send object, not raw int
////                }
//
//
//                // ✅ Listen for location updates
//                mSocket?.on("locationUpdate") { args ->
//                    if (args.isNotEmpty()) {
//                        val data = args[0] as JSONObject
//                        val driverInfo = data.getJSONObject("driverInfo")
//                        val driverId = driverInfo.getInt("id")  // ✅ Match this to bus.busId
//                        val driverName = driverInfo.optString("name", "")
//                        val driverPhone = driverInfo.optString("phone", "")
//                        val latitude = data.getDouble("latitude")
//                        val longitude = data.getDouble("longitude")
//                        val speed = data.optDouble("speed", 0.0)
//                        val placeName = if (data.has("placeName") && !data.isNull("placeName")) {
//                            data.getString("placeName")
//                        } else {
//                            "Fetching location..."
//                        }
//// ✅ turn off loader after setup completes
//                        isLoading = false
//
//                        busList = busList.map { bus ->
//                            if (bus.busId == driverId) {
//                                bus.copy(
//                                    driverName = driverName,
//                                    driverPhone = driverPhone,
//                                    latitude = latitude,
//                                    longitude = longitude,
//                                    speed = if (speed >= 0) speed else bus.speed,
//                                    placeName = placeName,
//                                    status = (latitude != 0.0 && longitude != 0.0)
//                                )
//                            } else bus
//                        }
//                    }
//                }
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//
//                isLoading = false
//            }
//        }
//    }
//
//    DisposableEffect(Unit) {
//        onDispose {
//            mSocket?.disconnect()
//            mSocket?.off("locationUpdate")
//        }
//    }
//    suspend fun refresh() {
//        try {
//            isLoading = true
//            val response = RetrofitInstance.api.getBuses(token)
//            val newList = response.map {
//                BusInfo(
//                    busId = it.driverId ?: 0,
//                    busNo = it.busNumber,
//                    driverName = "",
//                    driverPhone = "",
//                    status = (it.latitude ?: 0.0 != 0.0 && it.longitude ?: 0.0 != 0.0),
//                    latitude = it.latitude ?: 0.0,
//                    longitude = it.longitude ?: 0.0,
//                    speed = it.speed ?: 0.0,
//                    routeId = it.assignedRouteId ?: 0,
//                    stopType = it.stopType,
//                    routeCurrentRound = it.routeCurrentRound,
//                    routeCurrentJourneyPhase = it.routeCurrentJourneyPhase
//                )
//            }
//
//            busList = newList
//
//            // Re-subscribe to rooms
//            newList.forEach { bus ->
//                val data = JSONObject().put("driverId", bus.busId)
//                mSocket?.emit("subscribeToDriver", data)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
//            isLoading = false
//        }
//    }
//
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Institute Admin Dashboard", fontWeight = FontWeight.Bold) },
//                actions = {
//                    IconButton(onClick = { coroutineScope.launch { refresh() } }) {
//                        Icon(
//                            Icons.Default.Refresh,
//                            contentDescription = "Refresh",
//                            tint = Color.White
//                        )
//                    }
//                    TextButton(onClick = {
//                        sharedPreferences.edit().clear().apply()
//                        val repo = PreferencesRepository(context)
//                        repo.setLoggedIn(false)
//                        repo.setUserName("")
//                        repo.setUserPass("")
//                        navController.navigate("role") {
//                            popUpTo("adminDashboard") { inclusive = true }
//                        }
//                    }) {
//                        Text("Logout", color = Color.White)
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color(0xFF1976D2),
//                    titleContentColor = Color.White
//                )
//            )
//        }
//    ) { paddingValues ->
//        // Wrap content in a Box so the banner can align TopCenter (and stay fixed)
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .background(Color(0xFFF6F6F6))
//        ) {
//
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color(0xFFF6F6F6))
//                    .padding(horizontal = 10.dp, vertical = 10.dp)
//
//            ) {
//                Spacer(modifier = Modifier.height(20.dp))
//
//
//                Text(
//                    text = "Live Bus Status",
//                    fontFamily = Poppins,
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.padding(8.dp)
//                )
//
//                if (isLoading) {
//                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                        CircularProgressIndicator()
//                    }
//                } else {
//                    LazyColumn(
//                        modifier = Modifier.fillMaxSize(),
//                        verticalArrangement = Arrangement.spacedBy(10.dp)
//                    ) {
//                        if (busList.isEmpty()) {
//                            item {
//                                Text(
//                                    text = "No buses available",
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(20.dp),
//                                    fontSize = 16.sp,
//                                    color = Color.Gray
//                                )
//                            }
//                        } else {
//                            items(busList) { bus ->
//                                BusCard(bus)
//                            }
//                        }
//                    }
//                }
//            }
//
//            TopAlertMarquee(
//                text = ("Refresh for better location updates • Live GPS active • Tap to refresh •  ").repeat(2),
//                modifier = Modifier
//                    .align(Alignment.TopCenter)   // inside the same Box
//                    .fillMaxWidth(),
//                onClick = {
//                    // optional: trigger refresh from the banner
//                    coroutineScope.launch {
//                        isRefreshing = true
//                        refresh()
//                        isRefreshing = false
//                    }
//                }
//            )
//
//
//        }
//    }
//}
//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun TopAlertMarquee(
//    text: String,
//    modifier: Modifier = Modifier,
//    onClick: () -> Unit = {}   // ← make it non-null with a default
//) {
//    Surface(
//        modifier = modifier,
//        color = Color(0xFFD32F2F),
//        contentColor = Color.White,
//        shadowElevation = 2.dp,
//        onClick = onClick        // ← types now match
//    ) {
//        Text(
//            text = text,
//            maxLines = 1,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 12.dp, vertical = 6.dp)
//                .basicMarquee(velocity = 60.dp),
//            fontSize = 14.sp,
//            fontWeight = FontWeight.SemiBold
//        )
//    }
//}
//
////    @Composable
////    fun BusCard(bus: BusInfo) {
////        Card(
////            modifier = Modifier
////                .fillMaxWidth()
////                .wrapContentHeight(),
////            shape = RoundedCornerShape(12.dp),
////            elevation = CardDefaults.cardElevation(4.dp),
////            colors = CardDefaults.cardColors(containerColor = Color.White)
////        ) {
////            Column(
////                modifier = Modifier.padding(12.dp)
////            ) {
////                Row(
////                    modifier = Modifier.fillMaxWidth(),
////                    horizontalArrangement = Arrangement.SpaceBetween,
////                    verticalAlignment = Alignment.CenterVertically
////                ) {
////                    Text("Bus No: ${bus.busNo}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
////                    StatusChip(bus.status)
////                }
////
////                Spacer(modifier = Modifier.height(6.dp))
////
////                Text("Driver: ${bus.driverName}", fontSize = 16.sp, color = Color.DarkGray)
////                Text("Phone: ${bus.driverPhone}", fontSize = 16.sp, color = Color.DarkGray)
////                Text(
////                    "Speed: ${"%.2f".format(bus.speed)} km/h",
////                    fontSize = 16.sp,
////                    color = Color.DarkGray
////                )
////                Text(
////                    "Location: ${bus.latitude}, ${bus.longitude}",
////                    fontSize = 16.sp,
////                    color = Color.Gray
////                )
////                Text(
////                    text = "Place: ${if (bus.placeName.isNotBlank()) bus.placeName else "Fetching location..."}",
////                    fontSize = 16.sp,
////                    color = Color.DarkGray
////                )
////
////
////            }
////        }
////    }
////
////@Composable
////fun BusCard(bus: BusInfo) {
////    val coroutineScope = rememberCoroutineScope()
////    var showMessage by remember { mutableStateOf<String?>(null) }
////
////    Card(
////        modifier = Modifier
////            .fillMaxWidth()
////            .wrapContentHeight(),
////        shape = RoundedCornerShape(12.dp),
////        elevation = CardDefaults.cardElevation(4.dp),
////        colors = CardDefaults.cardColors(containerColor = Color.White)
////    ) {
////        Column(
////            modifier = Modifier.padding(12.dp)
////        ) {
////            Row(
////                modifier = Modifier.fillMaxWidth(),
////                horizontalArrangement = Arrangement.SpaceBetween,
////                verticalAlignment = Alignment.CenterVertically
////            ) {
////                Text("Bus No: ${bus.busNo}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
////                StatusChip(bus.status)
////            }
////
////            Spacer(modifier = Modifier.height(6.dp))
////
////            Text("Driver: ${bus.driverName}", fontSize = 16.sp, color = Color.DarkGray)
////            Text("Phone: ${bus.driverPhone}", fontSize = 16.sp, color = Color.DarkGray)
////            Text(
////                "Speed: ${"%.2f".format(bus.speed)} km/h",
////                fontSize = 16.sp,
////                color = Color.DarkGray
////            )
////            Text(
////                "Location: ${bus.latitude}, ${bus.longitude}",
////                fontSize = 16.sp,
////                color = Color.Gray
////            )
////            Text(
////                text = "Place: ${if (bus.placeName.isNotBlank()) bus.placeName else "Fetching location..."}",
////                fontSize = 16.sp,
////                color = Color.DarkGray
////            )
////            if (bus.stopType != null && bus.routeCurrentRound != null) {
////                Text(
////                    text = "Journey: ${bus.stopType?.replaceFirstChar { it.uppercase() }} Round ${bus.routeCurrentRound}",
////                    fontSize = 16.sp,
////                    fontWeight = FontWeight.Medium,
////                    color = Color(0xFF1976D2)  // blue highlight
////                )
////            }
////
////            Spacer(modifier = Modifier.height(10.dp))
////
////            // 🚍 New Button
////            Button(
//////                onClick = {
//////                    coroutineScope.launch {
//////                        try {
//////                            val request = MarkFinalStopNoAuthRequest(
//////                                driverId = bus.busId,
//////                                routeId = bus.routeId
//////                            )
//////                            val response = RetrofitInstance.api.markFinalStopNoAuth(request)
//////
//////                            if (response.isSuccessful) {
//////                                showMessage = "Journey finished for Bus ${bus.busNo}"
//////                            } else {
//////                                showMessage = "Failed: ${response.code()}"
//////                            }
//////                        } catch (e: Exception) {
//////                            showMessage = "Error: ${e.message}"
//////                        }
//////                    }
//////                },
////                onClick = {
////                    coroutineScope.launch {
////                        try {
////                            val request = MarkFinalStopNoAuthRequest(
////                                driverId = bus.busId,
////                                routeId = bus.routeId
////                            )
////                            Log.d("ADMIN_DASHBOARD", "Sending request: $request")   // 👈 add this
////                            val response = RetrofitInstance.api.markFinalStopNoAuth(request)
////                            Log.d("ADMIN_DASHBOARD", "Response code: ${response.code()}")  // 👈 add this
////
////                            if (response.isSuccessful) {
////                                showMessage = "Journey finished for Bus ${bus.busNo}"
////                            } else {
////                                showMessage = "Failed: ${response.code()}"
////                            }
////                        } catch (e: Exception) {
////                            Log.e("ADMIN_DASHBOARD", "Error calling API", e)  // 👈 add this
////                            showMessage = "Error: ${e.message}"
////                        }
////                    }
////                },
////                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
////            ) {
////                Text("Mark Journey Finished", color = Color.White)
////            }
////
////            // Optional feedback
////            showMessage?.let {
////                Spacer(modifier = Modifier.height(6.dp))
////                Text(it, color = Color.Gray, fontSize = 14.sp)
////            }
////        }
////    }
////}
////
////    @Composable
////    fun StatusChip(isOnline: Boolean) {
////        val bgColor = if (isOnline) Color(0xFF4CAF50) else Color(0xFFF44336)
////        val text = if (isOnline) "Online" else "Offline"
////
////        Box(
////            modifier = Modifier
////                .background(bgColor, RoundedCornerShape(50))
////                .padding(horizontal = 10.dp, vertical = 4.dp),
////            contentAlignment = Alignment.Center
////        ) {
////            Text(text = text, color = Color.White, fontWeight = FontWeight.SemiBold)
////        }
////    }
////
////
//
//
//@Composable
//fun BusCard(bus: BusInfo) {
//    val coroutineScope = rememberCoroutineScope()
//    var showMessage by remember { mutableStateOf<String?>(null) }
//    var showDialog by remember { mutableStateOf(false) }   // 👈 NEW state for popup
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .wrapContentHeight(),
//        shape = RoundedCornerShape(12.dp),
//        elevation = CardDefaults.cardElevation(4.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White)
//    ) {
//        Column(
//            modifier = Modifier.padding(12.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text("Bus No: ${bus.busNo}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
//                StatusChip(bus.status)
//            }
//
//            Spacer(modifier = Modifier.height(6.dp))
//
//            Text("Driver: ${bus.driverName}", fontSize = 16.sp, color = Color.DarkGray)
//            Text("Phone: ${bus.driverPhone}", fontSize = 16.sp, color = Color.DarkGray)
//            Text("Speed: ${"%.2f".format(bus.speed)} km/h", fontSize = 16.sp, color = Color.DarkGray)
//            Text("Location: ${bus.latitude}, ${bus.longitude}", fontSize = 16.sp, color = Color.Gray)
//            Text(
//                text = "Place: ${if (bus.placeName.isNotBlank()) bus.placeName else "Fetching location..."}",
//                fontSize = 16.sp,
//                color = Color.DarkGray
//            )
//
//            if (!bus.routeCurrentJourneyPhase.isNullOrBlank()) {
//                Text(
//                    text = "Journey: ${bus.routeCurrentJourneyPhase}",
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Medium,
//                    color = Color(0xFF1976D2)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            // 🚍 Button now only opens confirmation dialog
//            Button(
//                onClick = { showDialog = true },   // 👈 show dialog first
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
//            ) {
//                Text("Mark Journey Finished", color = Color.White)
//            }
//
//            // ✅ Confirmation Dialog
//            if (showDialog) {
//                AlertDialog(
//                    onDismissRequest = { showDialog = false },
//                    title = { Text("Confirm Action") },
//                    text = { Text("Are you sure you want to mark this journey as finished for Bus ${bus.busNo}?") },
//                    confirmButton = {
//                        TextButton(
//                            onClick = {
//                                showDialog = false
//                                coroutineScope.launch {
//                                    try {
//                                        val request = MarkFinalStopNoAuthRequest(
//                                            driverId = bus.busId,
//                                            routeId = bus.routeId
//                                        )
//                                        Log.d("ADMIN_DASHBOARD", "Sending request: $request")
//                                        val response = RetrofitInstance.api.markFinalStopNoAuth(request)
//                                        Log.d("ADMIN_DASHBOARD", "Response code: ${response.code()}")
//
//                                        if (response.isSuccessful) {
//                                            showMessage = "Journey finished for Bus ${bus.busNo}"
//                                        } else {
//                                            showMessage = "Failed: ${response.code()}"
//                                        }
//                                    } catch (e: Exception) {
//                                        Log.e("ADMIN_DASHBOARD", "Error calling API", e)
//                                        showMessage = "Error: ${e.message}"
//                                    }
//                                }
//                            }
//                        ) {
//                            Text("Yes", color = Color.Red)
//                        }
//                    },
//                    dismissButton = {
//                        TextButton(onClick = { showDialog = false }) {
//                            Text("Cancel")
//                        }
//                    }
//                )
//            }
//
//            // Optional feedback
//            showMessage?.let {
//                Spacer(modifier = Modifier.height(6.dp))
//                Text(it, color = Color.Gray, fontSize = 14.sp)
//            }
//        }
//    }
//}
//
//
//// ⬇️ place this at the bottom of AdminDashboardScreen.kt
//@Composable
//fun StatusChip(isOnline: Boolean) {
//    val bgColor = if (isOnline) Color(0xFF4CAF50) else Color(0xFFF44336)
//    val text = if (isOnline) "Online" else "Offline"
//
//    Box(
//        modifier = Modifier
//            .background(bgColor, RoundedCornerShape(50))
//            .padding(horizontal = 10.dp, vertical = 4.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(text = text, color = Color.White, fontWeight = FontWeight.SemiBold)
//    }
//}

package com.smartbus360.app.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.smartbus360.app.ui.theme.Poppins
import com.smartbus360.app.data.api.RetrofitInstance
import com.smartbus360.app.data.repository.PreferencesRepository
import kotlinx.coroutines.launch
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.activity.compose.BackHandler
import android.app.Activity
import io.socket.client.Manager
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.launch
import com.smartbus360.app.data.api.GenerateDriverQrRequest
import com.smartbus360.app.data.api.DriverQrTokenResponse
import java.time.Instant
import kotlinx.coroutines.delay
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.graphics.Bitmap
import android.os.Build
import android.content.ContentValues
import android.provider.MediaStore
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import android.app.NotificationChannel
import android.app.NotificationManager
import com.smartbus360.app.R
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts


data class BusInfo(
    val busId: Int,
    val busNo: String,
    var driverName: String = "",
    var driverPhone: String = "",
    var status: Boolean,
    var latitude: Double,
    var longitude: Double,
    var speed: Double,
    var placeName: String = "",
    var upcomingStop: String? = null
)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    navController: NavController
) {
    val context = navController.context
    BackHandler {
        // Move app to background instead of navigating or logging out
        (context as? Activity)?.moveTaskToBack(true)
    }
    AskNotificationPermission()

    var busList by remember { mutableStateOf<List<BusInfo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) } // 👈 for the floating pill

    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val token = "Bearer " + (sharedPreferences.getString("ACCESS_TOKEN", "") ?: "")

    // ✅ Socket.IO instance
    var mSocket: Socket? by remember { mutableStateOf(null) }

    // ✅ Fetch initial bus list (REST API) and subscribe for updates
    LaunchedEffect(true) {
        coroutineScope.launch {
            try {
                isLoading = true
                val response = RetrofitInstance.adminApi.getBuses(token)
                val initialList = response.map {
                    BusInfo(
                        busId = it.driverId ?: 0,
                        busNo = it.busNumber,
                        driverName = "",
                        driverPhone = "",
                        status = (it.latitude ?: 0.0 != 0.0 && it.longitude ?: 0.0 != 0.0),
                        latitude = it.latitude ?: 0.0,
                        longitude = it.longitude ?: 0.0,
                        speed = it.speed ?: 0.0,


                    )
                }
                busList = initialList

                // ✅ Connect to WebSocket after loading buses
                val options = IO.Options()
                options.transports = arrayOf(io.socket.engineio.client.transports.WebSocket.NAME)
                options.reconnection = true
                options.forceNew = true
                options.query = "token=${token.replace("Bearer ", "")}"  // ✅ Pass token
                mSocket = IO.socket("https://api.smartbus360.com/admin/notification", options)

                mSocket?.connect()
                mSocket?.on(Socket.EVENT_CONNECT) {
                    println("✅ Connected to Admin Notification Socket")
                    val current = busList
                    current.forEach { bus ->
                        val data = JSONObject().put("driverId", bus.busId) // send as NUMBER
                        mSocket?.emit("subscribeToDriver", data)
                    }
                }
// Also handle explicit reconnect events (older Socket.IO clients fire these)
                mSocket?.io()?.on(Manager.EVENT_RECONNECT) {
                    val current = busList
                    current.forEach { bus ->
                        val data = JSONObject().put("driverId", bus.busId)
                        mSocket?.emit("subscribeToDriver", data)
                    }
                }


                mSocket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
                    println("❌ Socket connection error: ${args.joinToString()}")
                }


//                // ✅ Subscribe to each driver room
//                initialList.forEach { bus ->
//                    val data = JSONObject()
//                        .put("driverId", bus.busId)   // ✅ Ensure it's a string
//                    mSocket?.emit("subscribeToDriver", data)  // ✅ send object, not raw int
//                }


                // ✅ Listen for location updates
                mSocket?.on("locationUpdate") { args ->
                    if (args.isNotEmpty()) {
                        val data = args[0] as JSONObject
                        val driverInfo = data.getJSONObject("driverInfo")
                        val driverId = driverInfo.getInt("id")  // ✅ Match this to bus.busId
                        val driverName = driverInfo.optString("name", "")
                        val driverPhone = driverInfo.optString("phone", "")
                        val latitude = data.getDouble("latitude")
                        val longitude = data.getDouble("longitude")
                        val speed = data.optDouble("speed", 0.0)
                        val upcomingStop = if (data.has("upcomingStop") && !data.isNull("upcomingStop")) {
                            data.getString("upcomingStop")
                        } else {
                            "Fetching..."
                        }
//                        val placeName = if (data.has("placeName") && !data.isNull("placeName"))
//                        {
//                            data.getString("placeName")
//                        } else {
//                            "Fetching location..."
//                        }
// ✅ turn off loader after setup completes
                        isLoading = false


                            busList = busList.map { bus ->
                                if (bus.busId == driverId) {
                                    bus.copy(
                                        driverName = driverName,
                                        driverPhone = driverPhone,
                                        latitude = latitude,
                                        longitude = longitude,
                                        speed = if (speed >= 0) speed else bus.speed,
                                        status = (latitude != 0.0 && longitude != 0.0),
                                        upcomingStop = upcomingStop   // 👈 NEW

                                    )
                                } else bus
                            }

                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()

                isLoading = false
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mSocket?.disconnect()
            mSocket?.off("locationUpdate")
        }
    }
    suspend fun refresh() {
        try {
            isLoading = true
            val response = RetrofitInstance.adminApi.getBuses(token)
            val newList = response.map {
                BusInfo(
                    busId = it.driverId ?: 0,
                    busNo = it.busNumber,
                    driverName = "",
                    driverPhone = "",
                    status = (it.latitude ?: 0.0 != 0.0 && it.longitude ?: 0.0 != 0.0),
                    latitude = it.latitude ?: 0.0,
                    longitude = it.longitude ?: 0.0,
                    speed = it.speed ?: 0.0
                )
            }
            busList = newList

            // Re-subscribe to rooms
            newList.forEach { bus ->
                val data = JSONObject().put("driverId", bus.busId)
                mSocket?.emit("subscribeToDriver", data)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Institute Admin Dashboard", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { coroutineScope.launch { refresh() } }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = Color.White
                        )
                    }
                    TextButton(onClick = {
                        sharedPreferences.edit().clear().apply()
                        val repo = PreferencesRepository(context)
                        repo.setLoggedIn(false)
                        repo.setUserName("")
                        repo.setUserPass("")
                        navController.navigate("role") {
                            popUpTo("adminDashboard") { inclusive = true }
                        }
                    }) {
                        Text("Logout", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        // Wrap content in a Box so the banner can align TopCenter (and stay fixed)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF6F6F6))
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF6F6F6))
                    .padding(horizontal = 10.dp, vertical = 10.dp)

            ) {
                Spacer(modifier = Modifier.height(20.dp))


                Text(
                    text = "Live Bus Status",
                    fontFamily = Poppins,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (busList.isEmpty()) {
                            item {
                                Text(
                                    text = "No buses available",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }
                        } else {
                            items(busList) { bus ->
                                BusCard(bus)
                            }
                        }
                    }
                }
            }

            TopAlertMarquee(
                text = ("Refresh for better location updates • Live GPS active • Tap to refresh •  ").repeat(2),
                modifier = Modifier
                    .align(Alignment.TopCenter)   // inside the same Box
                    .fillMaxWidth(),
                onClick = {
                    // optional: trigger refresh from the banner
                    coroutineScope.launch {
                        isRefreshing = true
                        refresh()
                        isRefreshing = false
                    }
                }
            )


        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopAlertMarquee(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}   // ← make it non-null with a default
) {
    Surface(
        modifier = modifier,
        color = Color(0xFFD32F2F),
        contentColor = Color.White,
        shadowElevation = 2.dp,
        onClick = onClick        // ← types now match
    ) {
        Text(
            text = text,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .basicMarquee(velocity = 60.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusCard(bus: BusInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    )
    {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        var activeQr by remember { mutableStateOf<DriverQrTokenResponse?>(null) }
        var countdown by remember { mutableStateOf("") }

        var showConfirmDialog by remember { mutableStateOf(false) }

        var showQrDialog by remember { mutableStateOf(false) }
        var qrPngBase64 by remember { mutableStateOf<String?>(null) }
        var isGenerating by remember { mutableStateOf(false) }
        var qrToken by remember { mutableStateOf("") }
        var qrExpiry by remember { mutableStateOf("") }

        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val token = "Bearer " + (sharedPref.getString("ACCESS_TOKEN", "") ?: "")

        LaunchedEffect(bus.busId) {
            val savedToken = sharedPref.getString("QR_TOKEN_${bus.busId}", null)
            val savedExpiry = sharedPref.getString("QR_EXPIRES_${bus.busId}", null)
            val savedPng = sharedPref.getString("QR_PNG_${bus.busId}", null)

            if (savedToken != null && savedExpiry != null && savedPng != null) {
                activeQr = DriverQrTokenResponse(
                    id = -1,
                    originalDriverId = bus.busId,
                    token = savedToken,
                    status = "active",
                    usedCount = 0,
                    createdBy = null,
                    createdAt = null,
                    expiresAt = savedExpiry,
                    durationHours = null,
                    png = savedPng
                )

                // start countdown
                startCountdown(savedExpiry) { countdown = it }
            }

            scope.launch {
                try {
//                    val resp = RetrofitInstance.adminApi.getDriverQrHistory(token)
                    val historyResponse = RetrofitInstance.adminApi.getDriverQrHistory(
                        token = token,
                        driverId = bus.busId
                    )


//                    if (resp.isSuccessful) {
//                        val history = resp.body() ?: emptyList()
//
//                        // Find ACTIVE QR for THIS driver
//                        activeQr = history.find { it.originalDriverId == bus.busId && it.status == "active" }
//
//                        // ⏳ Start countdown if active QR found
//                        if (activeQr != null) {
//                            startCountdown(activeQr!!.expiresAt) { updated ->
//                                countdown = updated
//                                if (updated == "Expired") activeQr = null
//                            }
//                        }
//                    }
                    val historyList = historyResponse.items

                    // ✅ Find ACTIVE QR for THIS driver
                    activeQr = historyList.find { it.originalDriverId == bus.busId && it.status == "active" }

                    if (activeQr != null && activeQr!!.expiresAt != null) {

                        startCountdown(activeQr!!.expiresAt!!) { updated ->
                            countdown = updated
                            if (updated == "Expired") {
                                sharedPref.edit().remove("QR_TOKEN_${bus.busId}").apply()
                                sharedPref.edit().remove("QR_EXPIRES_${bus.busId}").apply()
                                sharedPref.edit().remove("QR_PNG_${bus.busId}").apply()

                                activeQr = null

                            }
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Bus No: ${bus.busNo}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                StatusChip(bus.status)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text("Driver: ${bus.driverName}", fontSize = 16.sp, color = Color.DarkGray)
            Text("Phone: ${bus.driverPhone}", fontSize = 16.sp, color = Color.DarkGray)
            Text(
                "Speed: ${"%.2f".format(bus.speed)} km/h",
                fontSize = 16.sp,
                color = Color.DarkGray
            )
            Text(
                "Location: ${bus.latitude}, ${bus.longitude}",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Text(
//                text = "Upcoming Stop: ${if (bus.upcomingStop.isNotBlank()) bus.upcomingStop else "Fetching location..."}",
                text = "Nearby Stop: ${bus.upcomingStop?.takeIf { it.isNotBlank() } ?: "Fetching location..."}",
                fontSize = 16.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(10.dp))
            var selectedHours by remember { mutableStateOf(6) }
            val expiryOptions = listOf(1, 2, 3, 6, 12, 24)

            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = "$selectedHours hours",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("QR Expiry Time") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    expiryOptions.forEach { hour ->
                        DropdownMenuItem(
                            text = { Text("$hour hours") },
                            onClick = {
                                selectedHours = hour
                                expanded = false
                            }
                        )
                    }
                }
            }

//            Button(
//                onClick = {
//                    isGenerating = true
//
//                    val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
//                    val token = "Bearer " + (sharedPref.getString("ACCESS_TOKEN", "") ?: "")
//
//                    scope.launch {
//                        try {
//                            val resp = RetrofitInstance.adminApi.generateDriverQr(
//                                token,
//                                GenerateDriverQrRequest(driverId = bus.busId, durationHours = selectedHours)
//                            )
//
//                            if (resp.isSuccessful && resp.body()?.success == true) {
//                                qrPngBase64 = resp.body()!!.png
//                                qrToken = resp.body()!!.token
//                                qrExpiry = resp.body()!!.expiresAt
//                                showQrDialog = true
//                            }
//                        } catch (_: Exception) {
//                        } finally {
//                            isGenerating = false
//                        }
//                    }
//                },
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
//            ) {
//                Text(
//                    text = if (isGenerating) "Generating…" else "Generate Login QR",
//                    color = Color.White
//                )
//            }

            // If QR already generated → show Active badge + countdown + View QR button
            if (activeQr != null) {

                Text(
                    "QR Active",
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    "Expires in: $countdown",
                    color = Color.Red,
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = {
//                        qrPngBase64 = activeQr!!.png
//                        qrToken = activeQr!!.token
//                        qrExpiry = activeQr!!.expiresAt
//                        showQrDialog = true
                        qrPngBase64 = activeQr?.png ?: ""
                        qrToken = activeQr?.token ?: ""
                        qrExpiry = activeQr?.expiresAt ?: ""
                        showQrDialog = true

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text("View QR", color = Color.White)
                }
            } else {
                Button(
                    onClick = {
                        showConfirmDialog = true   // 👈 open confirmation popup
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text("Generate Login QR", color = Color.White)
                }
                if (showConfirmDialog) {
                    AlertDialog(
                        onDismissRequest = { showConfirmDialog = false },
                        title = { Text("Confirm QR Generation") },
                        text = {
                            Text("Are you sure you want to generate login QR for this driver?")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showConfirmDialog = false
                                    isGenerating = true

                                    // 🔥 ORIGINAL LOGIC — UNCHANGED
                                    scope.launch {
                                        try {
                                            val resp = RetrofitInstance.adminApi.generateDriverQr(
                                                token,
                                                GenerateDriverQrRequest(
                                                    driverId = bus.busId,
                                                    durationHours = selectedHours
                                                )
                                            )

                                            if (resp.isSuccessful && resp.body()?.success == true) {
                                                qrPngBase64 = resp.body()!!.png
                                                qrToken = resp.body()!!.token
                                                qrExpiry = resp.body()!!.expiresAt
                                                showQrDialog = true

                                                resp.body()?.let { body ->
                                                    sharedPref.edit().apply {
                                                        putString("QR_TOKEN_${bus.busId}", body.token)
                                                        putString("QR_EXPIRES_${bus.busId}", body.expiresAt)
                                                        putString("QR_PNG_${bus.busId}", body.png)
                                                    }.apply()

                                                    activeQr = DriverQrTokenResponse(
                                                        id = body.id,
                                                        originalDriverId = bus.busId,
                                                        token = body.token,
                                                        status = "active",
                                                        usedCount = 0,
                                                        createdBy = null,
                                                        createdAt = null,
                                                        expiresAt = body.expiresAt,
                                                        durationHours = selectedHours,
                                                        png = body.png
                                                    )

                                                    startCountdown(activeQr!!.expiresAt!!) { countdown = it }
                                                }
                                            }
                                        } finally {
                                            isGenerating = false
                                        }
                                    }
                                }
                            ) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showConfirmDialog = false }) {
                                Text("Cancel")
                            }
                        }
                    )
                }

                // Generate Button (only if no active QR)
//                Button(
//                    onClick = {
//                        isGenerating = true
//
//                        scope.launch {
//                            try {
//                                val resp = RetrofitInstance.adminApi.generateDriverQr(
//                                    token,
//                                    GenerateDriverQrRequest(driverId = bus.busId, durationHours = selectedHours)
//                                )
//
//                                if (resp.isSuccessful && resp.body()?.success == true) {
//                                    qrPngBase64 = resp.body()!!.png
//                                    qrToken = resp.body()!!.token
//                                    qrExpiry = resp.body()!!.expiresAt
//                                    showQrDialog = true
//
////                                    activeQr = resp.body()
////                                    startCountdown(activeQr!!.expiresAt) { countdown = it }
//                                    resp.body()?.let { body ->
//                                        // save qr
//                                        sharedPref.edit().apply {
//                                            putString("QR_TOKEN_${bus.busId}", body.token)
//                                            putString("QR_EXPIRES_${bus.busId}", body.expiresAt)
//                                            putString("QR_PNG_${bus.busId}", body.png)
//                                        }.apply()
//
//                                        activeQr = DriverQrTokenResponse(
//                                            id = body.id,
//                                            originalDriverId = bus.busId,
//                                            token = body.token,
//                                            status = "active",
//                                            usedCount = 0,
//                                            createdBy = null,
//                                            createdAt = null,
//                                            expiresAt = body.expiresAt,
//                                            durationHours = selectedHours,
//                                            png = body.png
//                                        )
//
//                                        startCountdown(activeQr!!.expiresAt!!) { countdown = it }
//                                    }
//
//                                }
//                            } finally {
//                                isGenerating = false
//                            }
//                        }
//                    },
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
//                ) {
//                    Text("Generate Login QR", color = Color.White)
//                }
            }


        }
        if (showQrDialog && qrPngBase64 != null) {
            DriverQrDialog(
                pngBase64 = qrPngBase64!!,
                token = qrToken,
                expiresAt = qrExpiry,
                driverName = bus.driverName,
                onClose = { showQrDialog = false }
            )
        }

    }

}
fun startCountdown(expiresAt: String, onTick: (String) -> Unit) {
    val expiryMillis = Instant.parse(expiresAt).toEpochMilli()

    kotlinx.coroutines.GlobalScope.launch {
        while (true) {
            val diff = (expiryMillis - System.currentTimeMillis()) / 1000
            if (diff <= 0) {
                onTick("Expired")
                break
            }
            val m = diff / 60
            val s = diff % 60
            onTick("$m min $s sec")
            delay(1000)
        }
    }
}

@Composable
fun AskNotificationPermission() {
    val context = LocalContext.current

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permission = android.Manifest.permission.POST_NOTIFICATIONS
        val granted = ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED

        if (!granted) {
            val launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) {}

            LaunchedEffect(true) {
                launcher.launch(permission)
            }
        }
    }
}

@Composable
fun StatusChip(isOnline: Boolean) {
    val bgColor = if (isOnline) Color(0xFF4CAF50) else Color(0xFFF44336)
    val text = if (isOnline) "Online" else "Offline"

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.White, fontWeight = FontWeight.SemiBold)
    }
}
//@Composable
//fun DriverQrDialog(pngBase64: String, onClose: () -> Unit) {
//
//    AlertDialog(
//        onDismissRequest = onClose,
//        title = { Text("Driver Login QR") },
//        text = {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                val base64Image = pngBase64.substringAfter("base64,", "")
//                val bytes = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT)
//
//                val bitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//
//                androidx.compose.foundation.Image(
//                    bitmap = bitmap.asImageBitmap(),
//                    contentDescription = "Driver QR",
//                    modifier = Modifier.size(220.dp)
//                )
//            }
//        },
////        confirmButton = {
////            TextButton(onClick = onClose) {
////                Text("Close")
////            }
////        }
//        confirmButton = {
//            val context = LocalContext.current
//            TextButton(onClick = onClose) { Text("Close") }
//
//            TextButton(onClick = {
//                val base64Image = pngBase64.substringAfter("base64,", "")
//                val bytes = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT)
//                val bitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//
//                saveQrToGallery(context, bitmap)
//            }) {
//                Text("Download QR")
//            }
//        }
//    )
//}

@Composable
fun DriverQrDialog(
    pngBase64: String,
    token: String,
    expiresAt: String,
    driverName: String,
    onClose: () -> Unit
) {
    val context = LocalContext.current

    val expiryTime = remember { mutableStateOf(expiresAt) }
    val remaining = remember { mutableStateOf("") }

    // ⏳ Count-down timer
    LaunchedEffect(true) {
        while (true) {
            val diff = (java.time.Instant.parse(expiresAt).toEpochMilli() - System.currentTimeMillis()) / 1000
            if (diff <= 0) {
                remaining.value = "Expired"
                break
            }
            val minutes = diff / 60
            val seconds = diff % 60
            remaining.value = "$minutes min $seconds sec"
            kotlinx.coroutines.delay(1000)
        }
    }
    val base64Image = pngBase64.substringAfter("base64,", "")
    val bytes = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT)
    val bitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Driver Login QR", fontWeight = FontWeight.Bold) },

        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // Decode QR
                val base64Image = pngBase64.substringAfter("base64,", "")
                val bytes = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT)
                val bitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                // Show QR
                androidx.compose.foundation.Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Driver Login QR",
                    modifier = Modifier.size(240.dp)
                )

                Spacer(Modifier.height(10.dp))
                Text("Driver: $driverName", fontWeight = FontWeight.Medium)
                Text("Token: ${token.take(12)}…", fontSize = 14.sp, color = Color.Gray)
                Text("Expires in: ${remaining.value}", color = Color.Red, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))

                // Buttons row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
//                    TextButton(onClick = {
//                        copyToClipboard(context, token)
//                    }) { Text("Copy") }

                    TextButton(onClick = {
                        shareQrImage(context, bitmap)
                    }) { Text("Share") }

                    TextButton(onClick = {
                        saveQrToGallery(context, bitmap)
                    }) {
                        Text("Download")
                    }

//                    TextButton(onClick = {
//                        saveQrToGallery(context, bitmap)
//                    }) { Text("Save") }
                }
            }
        },

//        confirmButton = {
//            TextButton(onClick = onClose) { Text("Close") }
//        }
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                TextButton(onClick = onClose) {
                    Text("Close")
                }


            }
        }

    )
}

//fun saveQrToGallery(context: Context, bitmap: android.graphics.Bitmap) {
//    val filename = "driver_qr_${System.currentTimeMillis()}.png"
//    val fos = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//        val contentValues = android.content.ContentValues().apply {
//            put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, filename)
//            put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "image/png")
//            put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/DriverQR")
//        }
//        val uri = context.contentResolver.insert(
//            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//            contentValues
//        )
//        context.contentResolver.openOutputStream(uri!!)
//    } else {
//        val imagesDir = android.os.Environment.getExternalStoragePublicDirectory(
//            android.os.Environment.DIRECTORY_DCIM
//        ).toString()
//        val file = java.io.File(imagesDir, filename)
//        java.io.FileOutputStream(file)
//    }
//
//    fos?.use {
//        bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, it)
//    }
//}

fun saveQrToGallery(context: Context, bitmap: Bitmap) {
    val filename = "driver_qr_${System.currentTimeMillis()}.png"

    val fos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/DriverQR")
        }
        val uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        context.contentResolver.openOutputStream(uri!!)
    } else {
        val imagesDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DCIM
        ).toString()
        val file = File(imagesDir, filename)
        FileOutputStream(file)
    }

    fos?.use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
    }

    showDownloadNotification(context, filename)
}

fun showDownloadNotification(context: Context, filename: String) {
    val channelId = "qr_download_channel"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Downloads",
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(context, channelId)
        .setContentTitle("QR Saved")
        .setContentText("$filename downloaded successfully")
        .setSmallIcon(R.drawable.bell)
        .setAutoCancel(true)
        .build()

    NotificationManagerCompat.from(context).notify(System.currentTimeMillis().toInt(), notification)
}

fun shareQrImage(context: Context, bitmap: android.graphics.Bitmap) {
    val file = java.io.File(context.cacheDir, "qr_${System.currentTimeMillis()}.png")
    val fos = java.io.FileOutputStream(file)
    bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, fos)
    fos.flush()
    fos.close()

    val uri = androidx.core.content.FileProvider.getUriForFile(
        context,
        context.packageName + ".provider",
        file
    )

    val intent = android.content.Intent().apply {
        action = android.content.Intent.ACTION_SEND
        putExtra(android.content.Intent.EXTRA_STREAM, uri)
        type = "image/png"
        addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(android.content.Intent.createChooser(intent, "Share QR"))
}

