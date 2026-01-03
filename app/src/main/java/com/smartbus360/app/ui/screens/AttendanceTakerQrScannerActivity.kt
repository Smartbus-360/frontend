package com.smartbus360.app.ui.screens


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.smartbus360.app.data.api.RetrofitInstance
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.smartbus360.app.data.repository.PreferencesRepository
import com.smartbus360.app.ui.screens.DriverAttendanceSheetScreen
import com.smartbus360.app.MainActivity
import com.journeyapps.barcodescanner.CaptureActivity


//class PortraitCaptureActivity : CaptureActivity()
class AttendanceTakerQrScannerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startQrScanner()
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // ✅ For emulator testing: directly test with a valid token
//        val testToken = "62e05877-b5b2-4fe5-8a9c-98e839a4ca99" // your working token
//        verifyQr(testToken)
//
//        // ❌ Comment this line during emulator testing
//        // startQrScanner()
//    }

//    private fun startQrScanner() {
//        val integrator = IntentIntegrator(this)
//        integrator.setPrompt("Scan Attendance Taker QR Code")
//        integrator.setBeepEnabled(true)
//        integrator.setOrientationLocked(true)
//        integrator.initiateScan()
//    }
private fun startQrScanner() {
    val integrator = IntentIntegrator(this)
    integrator.setPrompt("Scan Attendance Taker QR Code")
    integrator.setBeepEnabled(true)
    integrator.setOrientationLocked(true)
    integrator.setCaptureActivity(PortraitCaptureActivity::class.java) // 👈 Add this line
    integrator.initiateScan()
}


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                val qrToken = result.contents
                verifyQr(qrToken)
            } else {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun verifyQr(qrToken: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                println("🔹 Raw scanned QR: '${qrToken}'")
                val apiService = RetrofitInstance.api
//                val response = apiService.qrLoginAttendanceTaker(mapOf("token" to qrToken))
//                val cleanToken = qrToken.trim().replace("\n", "").replace("\r", "")
//                println("📤 Clean QR token being sent: '$cleanToken'")
//                val response = apiService.qrLoginAttendanceTaker(mapOf("token" to cleanToken))
                // ✅ Extract only the token from the QR URL
                val token = if (qrToken.contains("token=")) {
                    qrToken.substringAfter("token=").trim()
                } else {
                    qrToken.trim()
                }

                val cleanToken = token.replace("\n", "").replace("\r", "")
                println("📤 Clean QR token being sent: '$cleanToken'")
                val response = apiService.qrLoginAttendanceTaker(mapOf("token" to cleanToken))

//                if (response.isSuccessful && response.body()?.success == true) {
                val body = response.body()
                println("🔍 QR Login Response: $body")

                if (body != null && body.success) {
                    val taker = response.body()!!
//                    val prefs = getSharedPreferences("attendance_taker_prefs", MODE_PRIVATE)
//                    prefs.edit()
//                        .putInt("attendance_taker_id", taker.attendanceTakerId ?: -1)
//                        .putString("token", taker.token)
//                        .apply()
                    val prefs = getSharedPreferences("attendance_taker_prefs", MODE_PRIVATE)
                    prefs.edit()
                        .putString("role", taker.role ?: "")
                        .putInt("attendance_taker_id", taker.attendanceTakerId ?: -1)
                        .putString("token", taker.token)
                        .apply()

// ✅ Update global repository so MainActivity knows user is logged in
                    val repo = PreferencesRepository(this@AttendanceTakerQrScannerActivity)
                    repo.setLoggedIn(true)
//                    repo.setUserRole("attendance_taker")
                    repo.setUserRole(taker.role ?: "")
                    repo.setUserId(taker.attendanceTakerId ?: -1)
                    repo.setAuthToken(taker.token ?: "")

                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@AttendanceTakerQrScannerActivity,
                            "QR Login Successful!",
                            Toast.LENGTH_SHORT
                        ).show()
//                        val intent = Intent(this@AttendanceTakerQrScannerActivity, MainActivity::class.java)
//                        intent.putExtra("navigate_to", "attendanceSheet")
//                        startActivity(intent)
//                        val intent = Intent(this@AttendanceTakerQrScannerActivity, DriverAttendanceSheetActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        startActivity(intent)
//                        finish()
                        val intent = Intent(this@AttendanceTakerQrScannerActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra("resume_screen", "attendanceSheet")
                        startActivity(intent)
                        finish()

                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@AttendanceTakerQrScannerActivity,
                            "Invalid QR or Expired Session",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@AttendanceTakerQrScannerActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }
        }
    }
}
