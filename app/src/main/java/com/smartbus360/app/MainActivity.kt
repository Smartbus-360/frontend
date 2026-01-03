package com.smartbus360.app

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.smartbus360.app.data.repository.PreferencesRepository
import com.smartbus360.app.module.appModule
import com.smartbus360.app.module.databaseModule
import com.smartbus360.app.module.repositoryModule
import com.smartbus360.app.navigation.AppNavGraph
import com.smartbus360.app.ui.screens.setLocale
import com.smartbus360.app.ui.screens.startLocationService
import com.smartbus360.app.ui.screens.stopLocationService
import com.smartbus360.app.utility.LocationService
import com.smartbus360.app.utility.NetworkBroadcastReceiver
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.GlobalContext.startKoin
import org.osmdroid.config.Configuration
import java.io.File
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.smartbus360.app.viewModels.LoginViewModel
import android.net.Uri
import android.provider.Settings
import android.os.PowerManager
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.work.WorkManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import java.util.concurrent.TimeUnit
import androidx.work.NetworkType
import androidx.work.Constraints
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.smartbus360.app.parent.api.ParentApiClient


class MainActivity : ComponentActivity() {

    private val UPDATE_REQ = 7001
    private lateinit var appUpdateManager: AppUpdateManager
    private var keepSplashOn by mutableStateOf(true)
    private val loginViewModel: LoginViewModel by viewModel()
    private lateinit var networkReceiver: NetworkBroadcastReceiver
    private lateinit var preferencesRepository: PreferencesRepository


    // Listener for FLEXIBLE update: when download finishes, install
    private val updateListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            // Triggers the system install sheet and restarts app after install
            appUpdateManager.completeUpdate()
        }
    }
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()


    // For Android 14 and above: Request foreground location, background location, and notifications
//    @RequiresApi(Build.VERSION_CODES.O)
//    private val requestLocationAndNotificationPermissionsLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            val locationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
//            val backgroundLocationGranted = permissions[Manifest.permission.ACCESS_BACKGROUND_LOCATION] == true
//            val notificationGranted = permissions[Manifest.permission.POST_NOTIFICATIONS] == true
//
//            if (locationGranted && backgroundLocationGranted && notificationGranted && (GlobalContext.getOrNull() != null)) {
//                startLocationService()
//            } else {
////                Toast.makeText(this, "Required permissions are not granted.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    private lateinit var networkReceiver: NetworkBroadcastReceiver
//
//    @RequiresApi(Build.VERSION_CODES.O)
//
    @RequiresApi(Build.VERSION_CODES.O)
    private val requestForegroundPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            val notifGranted = permissions[Manifest.permission.POST_NOTIFICATIONS] == true

            if (fineGranted || coarseGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    requestBackgroundPermissionLauncher.launch(
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    )
                } else {
                    startLocationService()
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.Q)
    private val requestBackgroundPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) startLocationService()
            else {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }

//    private fun checkAndRequestPermissions() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            requestForegroundPermissionLauncher.launch(
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.POST_NOTIFICATIONS
//                )
//            )
//        }
//    }

//    private fun checkAndRequestPermissions() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            requestForegroundPermissionLauncher.launch(
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.POST_NOTIFICATIONS
//                )
//            )
//        }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {

        // Install the system splash screen
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { keepSplashOn }   // â¬…ï¸ ADD THIS
        super.onCreate(savedInstanceState)
        preferencesRepository = PreferencesRepository(applicationContext)

        ParentApiClient.init(applicationContext)

        // ✅ Auto reconnect student socket on app launch if logged in
        try {
            val repo = preferencesRepository
//            if (repo.isLoggedIn() && repo.getUserRole() == "student") {
//                val studentId = repo.getUserId()
//                if (studentId != 0) {
//                    com.smartbus360.app.utility.SocketManager.ensureConnected(this, studentId)
//                    println("✅ Auto reconnected socket for student ID: $studentId")
//                }
//            }
//            if (repo.isLoggedIn() && repo.getUserRole() == "student") {
//                val studentId = repo.getUserId()
//                if (studentId != 0) {
//                    val ctx = applicationContext
//                    com.smartbus360.app.utility.SocketManager.ensureConnected(ctx, studentId)
//                    com.smartbus360.app.utility.SocketManager.autoReconnect(ctx)
//                    println("✅ Persistent socket started for student ID: $studentId")
//                }
//            }

        } catch (e: Exception) {
            e.printStackTrace()
            println("⚠️ Failed to reconnect student socket: ${e.message}")
        }

        // âœ… One-time clear of any old OSM tiles from disk cache
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        if (!prefs.getBoolean("osmCacheCleared", false)) {
            val cacheDir = org.osmdroid.config.Configuration.getInstance().osmdroidTileCache
            try {
                cacheDir.deleteRecursively()
                println("OSM tile cache cleared")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            prefs.edit().putBoolean("osmCacheCleared", true).apply()
        }

        // Initialize Koin
//        if (GlobalContext.getOrNull() == null) {
//            startKoin {
//                androidContext(this@MainActivity)
//                modules(appModule, databaseModule, repositoryModule,)
//            }
//        }


        // Initialize and register the receiver
        networkReceiver = NetworkBroadcastReceiver { isConnected ->
            if (isConnected) {
                // Handle online state
                println("Internet is connected")
            } else {
                // Handle offline state
                println("Internet is disconnected")
            }
        }
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, filter)

//        checkAndRequestPermissions()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            checkAndRequestPermissions() // âœ… request FINE + BACKGROUND + NOTIFICATIONS
//            requestIgnoreBatteryOptimizations()
//
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Request only foreground location and notifications at startup
//            requestForegroundPermissionLauncher.launch(
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.POST_NOTIFICATIONS
//                )
//            )
//            requestIgnoreBatteryOptimizations()
//        }



        // Load the saved language from SharedPreferences or default to English
        val sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("language_code", "en") ?: "en"
        setLocale(this, savedLanguage)

        // Create notification channel for the service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "location_channel",
                "Location Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
        val ctx = applicationContext
        val basePath = File(ctx.cacheDir, "osmdroid").apply { mkdirs() }
        val tileCache = File(basePath, "tiles").apply { mkdirs() }

        Configuration.getInstance().osmdroidBasePath = basePath
        Configuration.getInstance().osmdroidTileCache = tileCache

        setContent {
            val context = LocalContext.current
            Configuration.getInstance()
                .load(context, context.getSharedPreferences("osmdroid", MODE_PRIVATE))
            org.osmdroid.config.Configuration.getInstance().userAgentValue = "SmartBus360App"

            var globalBanner by remember { mutableStateOf<Pair<String, String>?>(null) }

            // Wrap the entire app in a CompositionLocalProvider to lock font scaling
            CompositionLocalProvider(
                LocalDensity provides Density(
                    density = LocalDensity.current.density,
                    fontScale = 1.0f // Locks font scaling to 1x
                )


            ) {
                Box(Modifier.fillMaxSize()) {
                    AppNavGraph() // your full navigation flow
                    globalBanner?.let { (title, msg) ->
                        com.smartbus360.app.ui.component.GlobalPushBanner(
                            title = title,
                            message = msg
                        ) { globalBanner = null }
                    }
                }
            }

            LaunchedEffect(Unit) {
                keepSplashOn = false
            }



        }
        checkLastSession()
        checkForUpdate()
        handleQrDeepLink(intent)
// 🟩 Schedule Attendance Sync Worker
        try {
            val workRequest = PeriodicWorkRequestBuilder<com.smartbus360.app.utility.AttendanceSyncWorker>(
                15, TimeUnit.MINUTES
            ).build()

            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "attendanceSync",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )

            println("✅ AttendanceSyncWorker scheduled successfully")
        } catch (e: Exception) {
            e.printStackTrace()
            println("⚠️ Failed to schedule AttendanceSyncWorker: ${e.message}")
        }


        // requestPermissions() // Request location permissions on startup
    }
    private fun requestIgnoreBatteryOptimizations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val pm = getSystemService(PowerManager::class.java)
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }
    }



//    override fun onResume() {
//        super.onResume()
//        if (::appUpdateManager.isInitialized) {
//            appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
//                if (info.installStatus() == InstallStatus.DOWNLOADED) {
//                    appUpdateManager.completeUpdate()
//                }
//            }
//        }
//    }


    override fun onResume() {
        super.onResume()

        // ✅ Mark app as in foreground
        AppState.isForeground = true

        // ✅ Keep your existing app-update logic
        if (::appUpdateManager.isInitialized) {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
                if (info.installStatus() == InstallStatus.DOWNLOADED) {
                    appUpdateManager.completeUpdate()
                }
            }
        }
    }

    fun restartApp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }



//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun checkAndRequestPermissions() {
//        val permissions = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // TIRAMISU is API 33
//            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
//        }
//
//        requestLocationAndNotificationPermissionsLauncher.launch(permissions.toTypedArray())
//    }



    override fun onDestroy() {
        super.onDestroy()
        try { unregisterReceiver(networkReceiver) } catch (_: Exception) {}

        if (::appUpdateManager.isInitialized) {
            try { appUpdateManager.unregisterListener(updateListener) } catch (_: Exception) {}
        }

    }

    override fun onRestart() {
        super.onRestart()
        val repository = PreferencesRepository(this)
        val started = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
            .getBoolean("LOCATION_SHARING_STARTED", false)
        if (repository.getUserRole() == "driver" && started) {
            startLocationService(this)
        }
    }
    private fun checkForUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this)

        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val updateAvailable =
                info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                        info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)

            if (updateAvailable) {
                // Register listener so we can auto-install when download completes
                appUpdateManager.registerListener(updateListener)

                // Start FLEXIBLE update (downloads in background)
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    this, // activity
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build(),
                    UPDATE_REQ
                )
            }
        }
    }


//    override fun onResume(){
//        super.onResume()
//        val repository = PreferencesRepository(this)
////        repository.setStartedSwitch(false) // Update preference on activity destroy
////        stopLocationService(this)
////        repository.getUserRole()
//        when ( repository.getUserRole()) {
//            "driver" ->
//                if (repository.isLoggedIn())
//                startLocationService(this)
//        }
//
//    }




    @RequiresApi(Build.VERSION_CODES.O)
    private fun startLocationService() {
        if (GlobalContext.getOrNull() != null) {
            val intent = Intent(this, LocationService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleQrDeepLink(intent)
    }


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        handleQrDeepLink(intent)
//    }

    private fun handleQrDeepLink(intent: Intent) {
        val data = intent.data ?: return
        if (data.scheme == "smartbus360" &&
            (data.host.equals("qr-login", true) || data.path.equals("/qr-login", true))
        ) {
            val token = data.getQueryParameter("token")
            if (!token.isNullOrBlank()) {
                loginViewModel.exchangeQrLogin(token)
            }
        }
    }

//    private fun checkLastSession() {
//        val prefs = getSharedPreferences("attendance_taker_prefs", Context.MODE_PRIVATE)
//        val lastScreen = prefs.getString("last_screen", null)
//        val isLoggedIn = prefs.getBoolean("is_logged_in", false)
//
//        if (lastScreen == "attendance" && isLoggedIn) {
//            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
//                val builder = android.app.AlertDialog.Builder(this)
//                builder.setTitle("Last Session Detected")
//                builder.setMessage("Your attendance screen was open last time. Do you want to logout or continue?")
//                builder.setCancelable(false)
//
////                builder.setPositiveButton("Continue") { _, _ ->
////                    // keep session, just reopen app normally (Role Selection)
////                    prefs.edit().remove("last_screen").apply()
////                    val intent = Intent(this, MainActivity::class.java)
////                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
////                    startActivity(intent)
////                    finish()
////                }
//
//                builder.setNegativeButton("Logout") { _, _ ->
//                    prefs.edit().clear().apply()
//                    val repo = com.smartbus360.app.data.repository.PreferencesRepository(this)
//                    repo.logout()
//
//                    val intent = Intent(this, MainActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(intent)
//                    finish()
//                }
//
//                builder.show()
//            }, 800)
//        }

    private fun checkLastSession() {
        val prefs = getSharedPreferences("attendance_taker_prefs", Context.MODE_PRIVATE)
        val lastScreen = prefs.getString("last_screen", null)
        val isLoggedIn = prefs.getBoolean("is_logged_in", false)

        // ✅ Only show popup if no explicit resume request exists
        val shouldResume = intent?.getStringExtra("resume_screen")
        if (shouldResume == "attendanceSheet") {
            // Do nothing; NavGraph will open Attendance Sheet automatically
            return
        }

        if (lastScreen == "attendance" && isLoggedIn) {
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                val builder = android.app.AlertDialog.Builder(this)
                builder.setTitle("Last Session Detected")
                builder.setMessage("Your attendance screen was open last time. Do you want to logout or continue?")
                builder.setCancelable(false)

                builder.setNegativeButton("Logout") { _, _ ->
                    prefs.edit().clear().apply()
                    val repo = com.smartbus360.app.data.repository.PreferencesRepository(this)
                    repo.logout()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }

                builder.setPositiveButton("Continue") { _, _ ->
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra("resume_screen", "attendanceSheet")
                    startActivity(intent)
                    finish()
                }

                builder.show()
            }, 800)
        }
    }


    object AppState {
        var isForeground: Boolean = false
    }

    override fun onPause() {
        super.onPause()
        AppState.isForeground = false
    }

    fun showGlobalBanner(title: String, message: String) {
        runOnUiThread {
            val ctx = this
            val intent = Intent(ctx, MainActivity::class.java)
            intent.putExtra("BANNER_TITLE", title)
            intent.putExtra("BANNER_MESSAGE", message)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            ctx.startActivity(intent)
        }
    }

    }




