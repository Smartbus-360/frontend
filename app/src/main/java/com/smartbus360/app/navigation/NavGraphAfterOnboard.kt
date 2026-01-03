//package com.smartbus360.app.navigation
//
//import android.annotation.SuppressLint
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.animation.ExperimentalAnimationApi
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.animation.slideInHorizontally
//import androidx.compose.animation.slideOutHorizontally
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.platform.LocalContext
//import androidx.navigation.NavHostController
//import androidx.navigation.NavType
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import androidx.navigation.navArgument
//import com.google.accompanist.navigation.animation.AnimatedNavHost
//import com.smartbus360.app.data.repository.PreferencesRepository
//import com.smartbus360.app.ui.component.LocationPermissionScreen
//import com.smartbus360.app.ui.screens.DateTimeLogScreen
//import com.smartbus360.app.ui.screens.LanguageSelectionScreen
//import com.smartbus360.app.ui.screens.LoginScreen
//import com.smartbus360.app.ui.screens.ReachDateLogGroup
//import com.smartbus360.app.ui.screens.RoleSelectionScreen
//import com.smartbus360.app.ui.screens.StopLog
//import com.smartbus360.app.utility.isInternetAvailable
//import com.smartbus360.app.viewModels.LanguageViewModel
//import com.smartbus360.app.viewModels.LoginViewModel
//import com.smartbus360.app.viewModels.RoleSelectionViewModel
//import org.koin.androidx.compose.get
//import org.koin.androidx.compose.koinViewModel
//import com.smartbus360.app.ui.screens.AdminDashboardScreen
//import com.smartbus360.app.ui.screens.BusInfo
//// add this import at the top with other imports
//import com.smartbus360.app.ui.screens.DriverScreen
//import androidx.compose.ui.platform.LocalContext
//import android.Manifest
//import android.content.pm.PackageManager
//import androidx.core.content.ContextCompat
//import androidx.compose.runtime.collectAsState
//import android.util.Log
//
//
//@OptIn(ExperimentalAnimationApi::class)
//@SuppressLint("StateFlowValueCalledInComposition")
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun NavGraphAfterOnboard(navController: NavHostController = rememberNavController(), preferencesRepository: PreferencesRepository = get()) {
//
//    val role = preferencesRepository.getUserRole()
//    val context = LocalContext.current
////    val networkViewModel: NetworkViewModel = koinViewModel()
//
////    NavHost(
////        navController = navController,
////        startDestination = if (isLoggedIn()
////          //  && isInternetAvailable(context)
////            ) {
//////            if(role == "student"){"student"}else{"driver"}
////            "login"
////        } else if (preferencesRepository.isLangSelectCompleted()){
////            "role"
////        }
////        else
////            "language"
////    )
//    val dummyBuses = listOf(
//        BusInfo(1, "CG 04 AB 1234", "Dummy Driver 1", "9999999999", true, 20.298, 81.667, 45.0, "Raipur"),
//        BusInfo(2, "CG 04 XY 5678", "Dummy Driver 2", "8888888888", false, 20.312, 81.701, 0.0, "Bhilai")
//    )
//
//
//
//    AnimatedNavHost(
//        navController = navController,
//        startDestination = when {
//            isLoggedIn() -> {
//                when (role) {
//                    "admin" -> "adminDashboard"
//                    "driver" -> "driver"
//                    "student" -> "student"
//                    else -> "login" // fallback
//                }
//            }
//            preferencesRepository.isLangSelectCompleted() -> "role"
//            else -> "language"
//        },
//
//        enterTransition = {
//            slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
//        },
//        exitTransition = {
//            slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
//        },
//        popEnterTransition = {
//            slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn()
//        },
//        popExitTransition = {
//            slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut()
//        }
//    )
//
//    {
//        composable("language") {
//            val languageViewModel: LanguageViewModel = koinViewModel()
//            LanguageSelectionScreen(navController, languageViewModel)
//        }
//        composable("role") {
//            val roleSelectionViewModel: RoleSelectionViewModel = koinViewModel()
//            val languageViewModel: LanguageViewModel = koinViewModel()
//            RoleSelectionScreen(navController, roleSelectionViewModel,languageViewModel)
//        }
//        composable("login") {
//            val loginViewModel: LoginViewModel = koinViewModel()
//            val languageViewModel: LanguageViewModel = koinViewModel()
//            LoginScreen(navController, loginViewModel,languageViewModel)
//        }
//        composable("main") {
//            val languageViewModel: LanguageViewModel = koinViewModel()
//            MainScreenNavigation(navController)
//
//        }
////        composable( "driver")
////        {
////            val languageViewModel: LanguageViewModel = koinViewModel()
////            LocationPermissionScreen(navController)
////        }
//        composable("driver") {
//            val ctx = LocalContext.current
//            val loginViewModel: LoginViewModel = koinViewModel()  // ✅ bring the same ViewModel
//            val state = loginViewModel.state.collectAsState()
//
//            if (!hasLocationPermission(ctx)) {
//                LocationPermissionScreen(navController)
//            } else {
//                DriverScreen(
//                    preferencesRepository = PreferencesRepository(ctx),
//                    driverState = state.value,   // ✅ pass driver info
//                    onForceLogout = {
//                        navController.navigate("login") {
//                            popUpTo(0) { inclusive = true } // clear back stack
//                            launchSingleTop = true
//                        }
//                    }
//                        )
//            }
//            Log.d("NAV", "Composed: driver")
//
//        }
//
//        composable("student")
//        {
//            val languageViewModel: LanguageViewModel = koinViewModel()
//            StudentMainScreenNav(navController)
//        }
//        composable("adminDashboard") {
//            AdminDashboardScreen(navController)
//        }
//
//
//
////        composable(
////            route = "student?refresh={refresh}",
////            arguments = listOf(navArgument("refresh") {
////                type = NavType.StringType
////                nullable = true
////                defaultValue = null
////            })
////        ) {
////            val refresh = it.arguments?.getString("refresh")
////            StudentMainScreenNav(navController, refresh)
////        }
//
//
//    }
//}
//
//
//
//@Composable
//fun isLoggedIn(): Boolean {
//    val preferencesRepository = PreferencesRepository(context = LocalContext.current)
//    return preferencesRepository.isLoggedIn()
//}
//
//
//@Composable
//fun getUserRole(): String? {
//    val preferencesRepository = PreferencesRepository(context = LocalContext.current)
//    return preferencesRepository.getUserRole()
//}
//
//private fun hasLocationPermission(context: android.content.Context): Boolean {
//    val fineGranted = ContextCompat.checkSelfPermission(
//        context, Manifest.permission.ACCESS_FINE_LOCATION
//    ) == PackageManager.PERMISSION_GRANTED
//
//    val backgroundGranted =
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            ContextCompat.checkSelfPermission(
//                context, Manifest.permission.ACCESS_BACKGROUND_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        } else {
//            true
//        }
//
//    return fineGranted && backgroundGranted
//}


package com.smartbus360.app.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.smartbus360.app.data.repository.PreferencesRepository
import com.smartbus360.app.ui.component.LocationPermissionScreen
import com.smartbus360.app.ui.screens.DateTimeLogScreen
import com.smartbus360.app.ui.screens.LanguageSelectionScreen
import com.smartbus360.app.ui.screens.LoginScreen
import com.smartbus360.app.ui.screens.ReachDateLogGroup
import com.smartbus360.app.ui.screens.RoleSelectionScreen
import com.smartbus360.app.ui.screens.StopLog
import com.smartbus360.app.utility.isInternetAvailable
import com.smartbus360.app.viewModels.LanguageViewModel
import com.smartbus360.app.viewModels.LoginViewModel
import com.smartbus360.app.viewModels.RoleSelectionViewModel
import org.koin.androidx.compose.get
import org.koin.androidx.compose.koinViewModel
import com.smartbus360.app.ui.screens.AdminDashboardScreen
import com.smartbus360.app.ui.screens.BusInfo
// add this import at the top with other imports
import com.smartbus360.app.ui.screens.DriverScreen
import androidx.compose.ui.platform.LocalContext
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.compose.runtime.collectAsState
import android.util.Log
import com.smartbus360.app.ui.screens.DriverQrLoginScreen
import com.smartbus360.app.ui.screens.DriverAttendanceSheetScreen
import android.content.Context
import com.smartbus360.app.parent.auth.ParentLoginScreen
import com.smartbus360.app.ui.screens.ErpRoleSelectionScreen
import com.smartbus360.app.teacher.navigation.TeacherNavGraph
import com.smartbus360.app.erpadmin.ui.dashboard.AdminErpDashboardScreen
import com.smartbus360.app.erpadmin.ui.login.AdminErpLoginScreen
import com.smartbus360.app.erpadmin.util.TokenManager
import com.smartbus360.app.parent.dashboard.ParentDashboardScreen
import com.smartbus360.app.parent.navigation.ParentNavGraph


@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraphAfterOnboard(navController: NavHostController = rememberNavController(), preferencesRepository: PreferencesRepository = get()) {

    val role = preferencesRepository.getUserRole()
    val context = LocalContext.current
    val startNav = (context as? android.app.Activity)
        ?.intent?.getStringExtra("resume_screen")

//    val networkViewModel: NetworkViewModel = koinViewModel()

//    NavHost(
//        navController = navController,
//        startDestination = if (isLoggedIn()
//          //  && isInternetAvailable(context)
//            ) {
////            if(role == "student"){"student"}else{"driver"}
//            "login"
//        } else if (preferencesRepository.isLangSelectCompleted()){
//            "role"
//        }
//        else
//            "language"
//    )
//    val dummyBuses = listOf(
//        BusInfo(1, "CG 04 AB 1234", "Dummy Driver 1", "9999999999", true, 20.298, 81.667, 45.0, "Raipur"),
//        BusInfo(2, "CG 04 XY 5678", "Dummy Driver 2", "8888888888", false, 20.312, 81.701, 0.0, "Bhilai")
//    )

    val attendancePrefs = context.getSharedPreferences("attendance_taker_prefs", Context.MODE_PRIVATE)
    val lastScreen = attendancePrefs.getString("last_screen", null)
    val takerLoggedIn = attendancePrefs.getBoolean("is_logged_in", false)

//    val teacherProfileVM: TeacherProfileViewModel = koinViewModel()
    val dataStore = PreferencesRepository(context)

    val token = dataStore.getTeacherToken() ?: ""
    val teacherId = dataStore.getTeacherId()



    AnimatedNavHost(
        navController = navController,

        startDestination = when {
            startNav == "attendanceSheet" -> "attendanceSheet"
            takerLoggedIn && lastScreen == "attendance" -> "attendanceSheet"
            isLoggedIn() -> {
                    when (role) {
                        "admin" -> "adminDashboard"
                        "driver" -> "main"
                        "student" -> "student"
                        "attendance_taker" -> "attendanceSheet"
                        else -> "role" // fallback
                    }
                }

                preferencesRepository.isLangSelectCompleted() -> "role"
                else -> "role"

        },

        enterTransition = {
            slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn()
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut()
        }
    )

    {
        composable("language") {
            val languageViewModel: LanguageViewModel = koinViewModel()
            LanguageSelectionScreen(navController, languageViewModel)
        }
        composable("role") {
            val roleSelectionViewModel: RoleSelectionViewModel = koinViewModel()
            val languageViewModel: LanguageViewModel = koinViewModel()
            RoleSelectionScreen(navController, roleSelectionViewModel,languageViewModel)
        }
        composable("login") {
            val loginViewModel: LoginViewModel = koinViewModel()
            val languageViewModel: LanguageViewModel = koinViewModel()
            LoginScreen(navController, loginViewModel,languageViewModel)
        }
        composable("main") {
            val languageViewModel: LanguageViewModel = koinViewModel()
            MainScreenNavigation(navController)

        }
        composable("teacher") {
            TeacherNavGraph()
        }

        composable("attendanceTakerLogin") {
            DriverQrLoginScreen(navController)
        }

        composable("attendanceSheet") {
            DriverAttendanceSheetScreen(navController)
        }


        composable(
            route = "parent_module/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) { backStackEntry ->
            ParentNavGraph(
                studentId = backStackEntry.arguments!!.getInt("studentId")
            )
        }

        composable("parent_login") {
            ParentLoginScreen(
                onLoginSuccess = { studentId ->
                    navController.navigate("parent_module/$studentId") {
                        popUpTo("parent_login") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }



//        composable("parent_login") {
//            ParentLoginScreen(navController)
//        }

//        composable("teacher_login") {
//            TeacherLoginScreen(navController)
//        }

//        composable( "driver")
//        {
//            val languageViewModel: LanguageViewModel = koinViewModel()
//            LocationPermissionScreen(navController)
//        }
//        composable("driver") {
//            val ctx = LocalContext.current
//            val loginViewModel: LoginViewModel = koinViewModel()  // ✅ bring the same ViewModel
//            val state = loginViewModel.state.collectAsState()
//
//            if (!hasLocationPermission(ctx)) {
//                LocationPermissionScreen(navController)
//            } else {
//                DriverScreen(
//                    preferencesRepository = PreferencesRepository(ctx),
//                    driverState = state.value,   // ✅ pass driver info
//                    onForceLogout = {
//                        navController.navigate("login") {
//                            popUpTo(0) { inclusive = true } // clear back stack
//                            launchSingleTop = true
//                        }
//                    }
//                )
//            }
//            Log.d("NAV", "Composed: driver")
//
//        }

        composable("driver_qr_login") {
            DriverQrLoginScreen(navController)
        }

        composable("student")
        {
            val languageViewModel: LanguageViewModel = koinViewModel()
            StudentMainScreenNav(navController)
        }
        composable("adminDashboard") {
            AdminDashboardScreen(navController)
        }





        composable("erp_role_selection") {
            ErpRoleSelectionScreen(navController)
        }





//        composable("teacher_profile") {
//            TeacherProfileScreen(navController, teacherProfileVM, token)
//        }

//        composable(
//            route = "student?refresh={refresh}",
//            arguments = listOf(navArgument("refresh") {
//                type = NavType.StringType
//                nullable = true
//                defaultValue = null
//            })
//        ) {
//            val refresh = it.arguments?.getString("refresh")
//            StudentMainScreenNav(navController, refresh)
//        }


    }
}



//@Composable
//fun isLoggedIn(): Boolean {
//    val preferencesRepository = PreferencesRepository(context = LocalContext.current)
//    return preferencesRepository.isLoggedIn()
//}

@Composable
fun isLoggedIn(): Boolean {
    val context = LocalContext.current
    val preferencesRepository = PreferencesRepository(context)
    val normalLogin = preferencesRepository.isLoggedIn()

    // ✅ Also check if a QR driver session is active
    val qrPrefs = context.getSharedPreferences("driver_prefs", Context.MODE_PRIVATE)
    val driverId = qrPrefs.getInt("driver_id", 0)

    // If driver QR prefs exist but normal login is false → treat as logged out globally
    return normalLogin && driverId ==0
}

fun logout(context: Context) {
    val prefs = context.getSharedPreferences("com.smartbus360.app_preferences", Context.MODE_PRIVATE)
    prefs.edit().clear().apply()
}

@Composable
fun getUserRole(): String? {
    val preferencesRepository = PreferencesRepository(context = LocalContext.current)
    return preferencesRepository.getUserRole()
}

private fun hasLocationPermission(context: android.content.Context): Boolean {
    val fineGranted = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val backgroundGranted =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

    return fineGranted && backgroundGranted
}