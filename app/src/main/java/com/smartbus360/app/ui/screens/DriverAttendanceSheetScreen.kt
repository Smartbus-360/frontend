//package com.smartbus360.app.ui.screens
//
//
//import android.content.Context
//import android.widget.Toast
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import com.smartbus360.app.data.api.RetrofitInstance
//import com.smartbus360.app.data.api.AttendanceItem
//import kotlinx.coroutines.launch
//import android.content.Intent
//import androidx.compose.material.icons.filled.CameraAlt
//import androidx.compose.material.icons.Icons
//import androidx.compose.material3.Icon
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DriverAttendanceSheetScreen() {
//    val context = LocalContext.current
//    val scope = rememberCoroutineScope()
//    val prefs = context.getSharedPreferences("driver_prefs", Context.MODE_PRIVATE)
//    val driverId = prefs.getInt("driver_id", 0)
//
//    var attendanceList by remember { mutableStateOf<List<AttendanceItem>>(emptyList()) }
//
//    LaunchedEffect(Unit) {
//        if (driverId != 0) {
//            scope.launch {
//                try {
//                    val response = RetrofitInstance.api.getDriverAttendance(driverId)
//                    if (response.isSuccessful) {
//                        attendanceList = response.body() ?: emptyList()
//                    }
//                } catch (e: Exception) {
//                    Toast.makeText(context, "Error loading attendance", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//    Scaffold(
//        topBar = { TopAppBar(title = { Text("Today's Attendance") }) },
//        actions = {
//            TextButton(onClick = {
//                // Clear saved driver session
//                prefs.edit().clear().apply()
//
//                // Redirect to login screen
//                val intent = Intent(context, DriverQrLoginActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                context.startActivity(intent)
//            }) {
//                Text("Logout", color = MaterialTheme.colorScheme.onPrimary)
//            }
//        }
//    )
//},
//
//        floatingActionButton = {
//            FloatingActionButton(onClick = {
//                val intent = Intent(context, QrScannerActivity::class.java)
//                context.startActivity(intent)
//            }) {
//                Icon(Icons.Filled.CameraAlt, contentDescription = "Scan QR")
//            }
//        }
//    ) { padding ->
//        if (attendanceList.isEmpty()) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(padding),
//                contentAlignment = androidx.compose.ui.Alignment.Center
//            ) {
//                Text("No attendance yet")
//            }
//        } else {
//            LazyColumn(modifier = Modifier.padding(padding)) {
//                items(attendanceList) { item ->
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(8.dp),
//                        elevation = CardDefaults.cardElevation(4.dp)
//                    ) {
//                        Column(Modifier.padding(12.dp)) {
//                            Text("Name: ${item.username}")
//                            Text("Reg No: ${item.registrationNumber}")
//                            Text("Institute: ${item.instituteName}")
//                            Text("Bus: ${item.bus_id ?: "N/A"}")
//                            Text("Time: ${item.scan_time}")
//                        }
//                    }
//                }
//            }
//        }
//    }
//}

package com.smartbus360.app.ui.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.smartbus360.app.data.api.RetrofitInstance
import com.smartbus360.app.data.model.AttendanceItem
import kotlinx.coroutines.launch
import com.smartbus360.app.ui.screens.DriverQrLoginScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import com.smartbus360.app.data.repository.PreferencesRepository
import android.Manifest
import android.content.pm.PackageManager
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.smartbus360.app.data.api.AttendanceRequest
import com.smartbus360.app.data.api.AttendanceResponse
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import android.util.Log
import android.content.BroadcastReceiver
import android.content.IntentFilter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverAttendanceSheetScreen(navController:NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
//    val prefs = context.getSharedPreferences("driver_prefs", Context.MODE_PRIVATE)
//    val driverId = prefs.getInt("driver_id", 0)
    val prefs = context.getSharedPreferences("attendance_taker_prefs", Context.MODE_PRIVATE)
    val takerId = prefs.getInt("attendance_taker_id", 0)
    Log.d("ATTENDANCE_DEBUG", "Loaded attendance_taker_id = $takerId")


    var showLogoutDialog by remember { mutableStateOf(false) }

    var attendanceList by remember { mutableStateOf<List<AttendanceItem>>(emptyList()) }
    val lifecycleOwner = LocalLifecycleOwner.current

//    DisposableEffect(lifecycleOwner) {
//        val observer = LifecycleEventObserver { _, event ->
//            val receiver = object : BroadcastReceiver() {
//                override fun onReceive(context: Context?, intent: Intent?) {
//                    scope.launch {
//                        val response = RetrofitInstance.api.getTakerSheet(takerId)
//                        if (response.isSuccessful) {
//                            val body = response.body()
//                            if (body?.success == true) {
//                                attendanceList = body.attendance ?: emptyList()
//                            }
//                        }
//                    }
//                }
//            }
//
//            val filter = IntentFilter("ATTENDANCE_UPDATED")
//            context.registerReceiver(receiver, filter)
//
//            if (event == Lifecycle.Event.ON_RESUME) {
//                scope.launch {
//                    try {
//                        val response = RetrofitInstance.api.getTakerSheet(takerId)
//                        if (response.isSuccessful) {
//                            // ✅ Ensure driverId is saved in main repository for LocationService
//                            val repo = PreferencesRepository(context)
////                            if (driverId > 0) {
////                                repo.setDriverId(driverId)
////                                repo.setUserId(driverId)
////                                repo.setLoggedIn(true)
////                                repo.setUserRole("driver")
////                            }
//
////                            attendanceList = response.body() ?: emptyList()
//                            val body = response.body()
//                            if (response.isSuccessful && body?.success == true) {
//                                attendanceList = body.attendance ?: emptyList()
//                            } else {
//                                attendanceList = emptyList()
//                            }
//
//                        }
//                    } catch (e: Exception) {
//                        Toast.makeText(context, "Error refreshing attendance", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                }
//            }
//        }
//        lifecycleOwner.lifecycle.addObserver(observer)
//        onDispose {
//            lifecycleOwner.lifecycle.removeObserver(observer)
//        }
//    }
    DisposableEffect(lifecycleOwner) {

        // 1️⃣ Create receiver OUTSIDE lifecycle block
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                scope.launch {
                    val response = RetrofitInstance.api.getTakerSheet(takerId)
                    if (response.isSuccessful) {
                        val body = response.body()
                        attendanceList = body?.attendance ?: emptyList()
                    }
                }
            }
        }

        // 2️⃣ Register receiver
        val filter = IntentFilter("ATTENDANCE_UPDATED")
        context.registerReceiver(receiver, filter,Context.RECEIVER_NOT_EXPORTED)

        // 3️⃣ Lifecycle observer only refreshes on resume
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                scope.launch {
                    val response = RetrofitInstance.api.getTakerSheet(takerId)
                    if (response.isSuccessful) {
                        val body = response.body()
                        attendanceList = body?.attendance ?: emptyList()
                    }
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        // 4️⃣ Clean up on exit
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            context.unregisterReceiver(receiver)
        }
    }

//    LaunchedEffect(Unit) {
//        prefs.edit().putString("last_screen", "attendance").apply()
//        Log.d("ATTENDANCE_DEBUG", "Fetching attendance sheet for takerId = $takerId")
//        if (takerId != 0) {
//            scope.launch {
//                try {
//                    val response = RetrofitInstance.api.getTakerSheet(takerId)
//                    if (response.isSuccessful) {
////                        attendanceList = response.body() ?: emptyList()
//                        val body = response.body()
//                        if (response.isSuccessful && body?.success == true) {
//                            attendanceList = body.attendance ?: emptyList()
//                        } else {
//                            attendanceList = emptyList()
//                        }
//
//                    }
//                } catch (e: Exception) {
//                    Toast.makeText(context, "Error loading attendance", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
    LaunchedEffect(Unit) {
        prefs.edit()
            .putString("last_screen", "attendance")
            .putBoolean("is_logged_in", true)
            .apply()
        Log.d("ATTENDANCE_DEBUG", "Session saved: last_screen = attendance")

        Log.d("ATTENDANCE_DEBUG", "Fetching attendance sheet for takerId = $takerId")
        if (takerId != 0) {
            scope.launch {
                try {
                    val response = RetrofitInstance.api.getTakerSheet(takerId)
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (response.isSuccessful && body?.success == true) {
                            attendanceList = body.attendance ?: emptyList()
                        } else {
                            attendanceList = emptyList()
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error loading attendance", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

// 🔄 Auto-refresh attendance from backend every 10 seconds
    LaunchedEffect(takerId) {
        while (true) {
            if (takerId != 0) {
                try {
                    val response = RetrofitInstance.api.getTakerSheet(takerId)
                    if (response.isSuccessful) {
//                        attendanceList = response.body() ?: emptyList()
                        val body = response.body()
                        if (response.isSuccessful && body?.success == true) {
                            attendanceList = body.attendance ?: emptyList()
                        } else {
                            attendanceList = emptyList()
                        }

                    }
                } catch (e: Exception) {
                    // Optional: show only once if needed
                    println("Auto-refresh failed: ${e.message}")
                }
            }
            kotlinx.coroutines.delay(10000) // 10 seconds
        }
    }

    Scaffold(
        // ✅ Correct placement of TopAppBar inside Scaffold
        topBar = {
            TopAppBar(
                title = { Text("Today's Attendance") },
                actions = {
                    TextButton(onClick = {
                        // Clear saved driver session
//                        val prefs =
//                            context.getSharedPreferences("driver_prefs", Context.MODE_PRIVATE)
                        val prefs = context.getSharedPreferences("attendance_taker_prefs", Context.MODE_PRIVATE)
                        prefs.edit().clear().apply()

                        val repo = PreferencesRepository(context)
                        repo.logout() // ✅ clears main login flag

                        // Redirect to login screen
//                        val intent = Intent(context, DriverQrLoginActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        context.startActivity(intent)
                        navController.navigate("driver_qr_login") {
                            popUpTo(0) { inclusive = true }  // Clears backstack
                            launchSingleTop = true
                        }

                    }) {
                        Text("Logout", color = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val intent = Intent(context, QrScannerActivity::class.java)
                context.startActivity(intent)
            }) {
                Icon(Icons.Filled.CameraAlt, contentDescription = "Scan QR")
            }
        }
    ) { padding ->
        if (attendanceList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No attendance yet")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(attendanceList) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text("Name: ${item.username}")
                            Text("Reg No: ${item.registrationNumber}")
                            Text("Institute: ${item.instituteName}")
                            Text("Bus: ${item.bus_id ?: "N/A"}")
                            Text("Time: ${item.scan_time}")
                        }
                    }
                }
            }
        }
    }


    BackHandler {
        showLogoutDialog = true
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Confirm Logout") },
            text = { Text("Are you sure you want to logout and exit?") },
            confirmButton = {
                TextButton(onClick = {
//                    val prefs = context.getSharedPreferences("driver_prefs", Context.MODE_PRIVATE)
                    val prefs = context.getSharedPreferences("attendance_taker_prefs", Context.MODE_PRIVATE)
                    prefs.edit().remove("last_screen").apply()
                    prefs.edit().clear().apply()

                    val repo = PreferencesRepository(context)
                    repo.logout()

                    showLogoutDialog = false
                    navController.navigate("driver_qr_login") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }) {
                    Text("Yes, Logout")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )

    }
//    override fun onResume() {
//        super.onResume()
//        fetchAttendanceList()   // 👈 reload data after returning from QR scan
//    }

    // ✅ Auto logout when app is closed or minimized from Attendance screen
//    DisposableEffect(lifecycleOwner) {
//        val observer = LifecycleEventObserver { _, event ->
//            when (event) {
//                Lifecycle.Event.ON_DESTROY -> {
//                    // App or this screen is being destroyed → clear session
////                    val prefs = PreferencesRepository(context)
////                    prefs.clearSession()
////                    val driverPrefs = context.getSharedPreferences("driver_prefs", Context.MODE_PRIVATE)
//                    val prefs = context.getSharedPreferences("attendance_taker_prefs", Context.MODE_PRIVATE)
//                    prefs.edit().remove("last_screen").apply()
//                    prefs.edit().clear().apply()
//
//                    val repo = PreferencesRepository(context)
//                    repo.clearSession()
//
//
//                    Toast.makeText(
//                        context,
//                        "Attendance taker session ended. Please login again.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//                else -> {}
//            }
//        }
//        lifecycleOwner.lifecycle.addObserver(observer)
//        onDispose {
//            lifecycleOwner.lifecycle.removeObserver(observer)
//        }
//    }
}