package com.smartbus360.app.ui.screens


import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smartbus360.app.data.model.request.LoginRequest
import com.smartbus360.app.viewModels.LoginViewModel
import org.koin.androidx.compose.koinViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.smartbus360.app.data.repository.PreferencesRepository
import androidx.compose.ui.graphics.Color




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverQrLoginScreen(navController: NavController, viewModel: LoginViewModel = koinViewModel()) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
//    val state by viewModel.state.collectAsState()
    val state by viewModel.state.collectAsState()
    val attendanceState by viewModel.attendanceTakerState.collectAsState()
    val error by viewModel.stateException.collectAsState()
    val errorStatus by viewModel.stateExceptionStatus.collectAsState()

    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Attendance Coordinator Login", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("role") {
                            popUpTo("driver_qr_login") { inclusive = true }
                            launchSingleTop = true
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    )
    { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Attendance Coordinator Email / ID") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (username.isNotEmpty() && password.isNotEmpty()) {
                        isLoading = true
                        // viewModel.login(LoginRequest(username, password))
                        viewModel.loginAttendanceTaker(LoginRequest(username, password))
                    } else {
                        Toast.makeText(context, "Please enter credentials", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading)
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                else
                    Text("Login")
            }
            Spacer(modifier = Modifier.height(12.dp))

// ✅ NEW BUTTON: "Login by QR"
            Button(
                onClick = {
                    val intent = Intent(context, AttendanceTakerQrScannerActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Login by QR", color = Color.White)
            }

// ✅ New Scan QR Attendance button
//            Button(
//                onClick = {
//                    val intent = Intent(context, QrScannerActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                    context.startActivity(intent)
//                },
//                modifier = Modifier.fillMaxWidth(),
//                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
//            ) {
//                Text("Scan QR Attendance")
//            }

            Spacer(modifier = Modifier.height(24.dp))

//            if (state.success == true) {
//                isLoading = false
//                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
//
//                // ✅ Launch QR Scanner after login success
//                val intent = Intent(context, QrScannerActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                context.startActivity(intent)
//            }

//            if (state.success == true) {
//                isLoading = false
//                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
//
//                val driverId = state.driverId  // directly from response
//                val prefs = context.getSharedPreferences("driver_prefs", android.content.Context.MODE_PRIVATE)
//                prefs.edit().putInt("driver_id", driverId).apply()
//
//                // Navigate to new AttendanceSheet screen
////                navController.navigate("attendanceSheet")
//                val intent = Intent(context, DriverAttendanceSheetActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                context.startActivity(intent)
//
//            }
//            if (state.success == true) {
//                isLoading = false
//                Toast.makeText(context, " Attendance Taker Login Successful!", Toast.LENGTH_SHORT).show()
//            }
//
//            LaunchedEffect(state.success) {
//                if (state.success == true) {
//                    val takerId = state.driverId
//                    val qrPrefs = context.getSharedPreferences("attendance_taker_prefs", Context.MODE_PRIVATE)
//                    qrPrefs.edit()
//                        .putInt("attendance_taker_id", takerId)
//                        .putBoolean("is_logged_in", true)
//                        .apply()
//                    val repo = PreferencesRepository(context)
//                    repo.setUserId(takerId)
//                    repo.setLoggedIn(true)
//                    repo.setUserRole("attendance_taker")
//
//
//
////                    val intent = Intent(context, DriverAttendanceSheetActivity::class.java)
////                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
////                    context.startActivity(intent)
//                    navController.navigate("attendanceSheet") {
//                        popUpTo("driver_qr_login") { inclusive = true }
//                        launchSingleTop = true
//                    }
//
//                }
//            LaunchedEffect(state.success) {
//                if (state.success == true) {
//                    val takerId = state.driverId
//                    val token = state.token  // ✅ capture JWT token from backend
//
//                    // ✅ Save both token and taker ID to the same SharedPrefs
//                    val prefs = context.getSharedPreferences("attendance_taker_prefs", Context.MODE_PRIVATE)
//                    prefs.edit()
//                        .putInt("attendance_taker_id", takerId)
//                        .putString("token", token)
//                        .putString("role", state.role)   // because this screen is ONLY for attendance coordinator
//                        .apply()
//                    // Save role using repository NOT local prefs
//                    val repo = PreferencesRepository(context)
//                    repo.setUserRole(state.role ?: "attendance_taker")
//
//
//
//                    Toast.makeText(
//                        context,
//                        "Attendance Coordinator Login Successful!",
//                        Toast.LENGTH_SHORT
//                    ).show()
//
//                    navController.navigate("attendanceSheet") {
//                        popUpTo("driver_qr_login") { inclusive = true }
//                        launchSingleTop = true
//                    }
//                }
//            }

            LaunchedEffect(attendanceState) {
                if (attendanceState?.success == true) {

                    val takerId = attendanceState?.attendanceTakerId ?: 0
                    val token = attendanceState?.token ?: ""
                    val role = attendanceState?.role ?: ""

                    val prefs = context.getSharedPreferences("attendance_taker_prefs", Context.MODE_PRIVATE)
                    prefs.edit()
                        .putInt("attendance_taker_id", takerId)
                        .putString("token", token)
                        .putString("role", role)
                        .apply()

                    navController.navigate("attendanceSheet") {
                        popUpTo("driver_qr_login") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

        }

            if (errorStatus) {
                isLoading = false
                Toast.makeText(context, "Login failed: ${error?.message}", Toast.LENGTH_SHORT)
                    .show()
                viewModel.resetExceptionState()
            }
        }
    }
