//package com.smartbus360.app.ui.screens
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.os.Bundle
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import android.widget.Toast
//import com.google.zxing.integration.android.IntentIntegrator
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import com.smartbus360.app.data.api.AttendanceRequest
//import com.smartbus360.app.data.api.AttendanceResponse
//import com.smartbus360.app.data.api.RetrofitInstance
//import androidx.lifecycle.lifecycleScope
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//import android.content.pm.ActivityInfo
//import com.google.android.material.bottomsheet.BottomSheetDialog
//import android.widget.TextView
//import com.smartbus360.app.R
//import android.util.Log
//
//class QrScannerActivity : AppCompatActivity() {
//
//
//    private val requestPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted: Boolean ->
//        if (isGranted) startScan() else Toast.makeText(
//            this,
//            "Camera permission denied",
//            Toast.LENGTH_SHORT
//        ).show()
//    }
//    private var selectedNote: String? = null
//    private var selectedNoteType: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_qr_scanner)
//
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//
////        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
////            startScan()
////        } else {
////            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
////        }
//        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//            showPreScanNoteDialog()
//        } else {
//            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
//        }
//
//    }
//
////    private fun startScan() {
////        val integrator = IntentIntegrator(this)
////        integrator.setPrompt("Scan Student QR Code")
////        integrator.setBeepEnabled(true)
////        integrator.setOrientationLocked(false)
////        integrator.initiateScan()
////    }
//private fun startScan() {
//    val integrator = IntentIntegrator(this)
//    integrator.setPrompt("Scan Student QR Code")
//    integrator.setBeepEnabled(true)
//
//    // ✅ Lock orientation so scanner stays portrait
//    integrator.setOrientationLocked(true)
//    integrator.setCaptureActivity(PortraitCaptureActivity::class.java) // optional custom portrait capture
//    integrator.initiateScan()
//}
//
//    override fun onActivityResult(
//        requestCode: Int,
//        resultCode: Int,
//        data: android.content.Intent?
//    ) {
//        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
//        if (result != null) {
//            if (result.contents == null) {
//                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
//            } else {
//                handleQrResult(result.contents)
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data)
//        }
//    }
//
////    private fun handleQrResult(data: String) {
////        try {
////            val json = org.json.JSONObject(data)
////            val registrationNumber = json.getString("registrationNumber")
////            val token = json.getString("token")
////
////            val prefs = getSharedPreferences("driver_prefs", MODE_PRIVATE)
////            val driverId = prefs.getInt("driver_id", 0)
////
////            val request = AttendanceRequest(
////                registrationNumber = registrationNumber,
////                token = token,
////                driver_id = driverId,
////                bus_id = "BUS_001",
////                latitude = 20.67,
////                longitude = 81.60
////            )
////
////            RetrofitInstance.api.markAttendance(request).enqueue(object : Callback<AttendanceResponse> {
//////                override fun onResponse(call: Call<AttendanceResponse>, response: Response<AttendanceResponse>) {
//////                    if (response.isSuccessful && response.body()?.success == true) {
//////                        Toast.makeText(this@QrScannerActivity, "✅ ${response.body()!!.message}", Toast.LENGTH_LONG).show()
//////                    } else {
//////                        Toast.makeText(this@QrScannerActivity, "❌ Failed: ${response.body()?.message}", Toast.LENGTH_LONG).show()
//////                    }
//////                }
////
//////                override fun onResponse(call: Call<AttendanceResponse>, response: Response<AttendanceResponse>) {
//////                    android.util.Log.d("QR_API", "Status: ${response.code()}, URL: ${call.request().url}")
//////                    android.util.Log.d("QR_API", "Body: ${response.body()}, ErrorBody: ${response.errorBody()?.string()}")
//////                    if (response.isSuccessful && response.body()?.success == true) {
//////                        Toast.makeText(this@QrScannerActivity, "✅ ${response.body()!!.message}", Toast.LENGTH_LONG).show()
//////                    } else {
//////                        val errorMsg = try {
//////                            response.errorBody()?.string()?.let { org.json.JSONObject(it).optString("message", "Unknown error") }
//////                        } catch (e: Exception) {
//////                            "Unexpected error"
//////                        }
//////                        Toast.makeText(this@QrScannerActivity, "❌ Failed: $errorMsg", Toast.LENGTH_LONG).show()
//////                    }
//////                }
//
//    private fun handleQrResult(data: String) {
//        try {
//            val json = org.json.JSONObject(data)
//            val registrationNumber = json.getString("registrationNumber")
//            val token = json.getString("token")
//
//            // ✅ Load Attendance Taker ID
////            val prefs = getSharedPreferences("attendance_taker_prefs", MODE_PRIVATE)
////            val takerId = prefs.getInt("attendance_taker_id", 0)
//// ✅ Load saved login data from same SharedPreferences file
//            val prefs = getSharedPreferences("attendance_taker_prefs", MODE_PRIVATE)
//            val takerId = prefs.getInt("attendance_taker_id", 0)
//            val authToken = prefs.getString("token", null)
//
////            android.util.Log.d("QR_DEBUG", "Loaded Token: $authToken | Taker ID: $takerId")
//            if (takerId == 0 || authToken.isNullOrEmpty()) {
//                Toast.makeText(
//                    this,
//                    "⚠️ Please log in again to mark attendance.",
//                    Toast.LENGTH_LONG
//                ).show()
//                return
//            }
//
//            val request = AttendanceRequest(
//                registrationNumber = registrationNumber,
//                token = token,
//                attendance_taker_id = takerId,
////                bus_id = "BUS_001",
//                latitude = 20.67,
//                longitude = 81.60
//            )
//
//            // ✅ Load saved auth token (Bearer token)
////            val sharedPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE)
////            val authToken = sharedPrefs.getString("token", null)
//
//
//
//
//            // ✅ Run in coroutine
//            lifecycleScope.launch {
//                try {
//                    val response = RetrofitInstance.api.markAttendance("Bearer $authToken", request)
//
//                    withContext(Dispatchers.Main) {
////                        if (response.isSuccessful && response.body()?.success == true) {
////                            Toast.makeText(
////                                this@QrScannerActivity,
////                                "✅ Attendance marked successfully!",
////                                Toast.LENGTH_LONG
////                            ).show()
////                            android.os.Handler(mainLooper).postDelayed({ finish() }, 1500)
////                        }
//                        if (response.isSuccessful && response.body()?.success == true) {
//
//                            val attendanceId = response.body()?.attendance_id
//
//                            if (attendanceId != null) {
////                                showNoteBottomSheet(attendanceId)
//                                sendSelectedNote(attendanceId)
//
//                            } else {
//                                Toast.makeText(this@QrScannerActivity, "Marked but no ID returned", Toast.LENGTH_LONG).show()
//                                finish()
//                            }
//                        }
//
//                        else {
//                            val errorMsg = try {
//                                response.errorBody()?.string()?.let {
//                                    org.json.JSONObject(it).optString("message", "Unknown error")
//                                }
//                            } catch (e: Exception) {
//                                "Unexpected error"
//                            }
//                            Toast.makeText(
//                                this@QrScannerActivity,
//                                "❌ Failed: $errorMsg",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    }
//                } catch (e: Exception) {
//                    withContext(Dispatchers.Main) {
//                        Toast.makeText(
//                            this@QrScannerActivity,
//                            "⚠️ Error: ${e.message}",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            Toast.makeText(this, "Invalid QR format", Toast.LENGTH_LONG).show()
//        }
//    }
//
//    private fun showCustomNoteDialog(attendanceId: Int) {
//        val input = android.widget.EditText(this)
//        android.app.AlertDialog.Builder(this)
//            .setTitle("Enter Note")
//            .setView(input)
//            .setPositiveButton("Save") { _, _ ->
//                val note = input.text.toString()
//                lifecycleScope.launch(Dispatchers.IO) {
//                    try {
//                        val prefs = getSharedPreferences("attendance_taker_prefs", MODE_PRIVATE)
//                        val token = prefs.getString("token", "") ?: ""
//                        if (token.isEmpty()) {
//                            Log.e("NOTE_DEBUG", "Token missing — cannot send note")
//                        }
//
//
//                        val body = mapOf(
//                            "attendance_id" to attendanceId,
//                            "note" to note
//                        )
//
//                        RetrofitInstance.api.addAttendanceNote( body)
//
//                        withContext(Dispatchers.Main) {
//                            finish()
//                        }
//                    } catch (_: Exception) {}
//                }
//            }
//            .setNegativeButton("Cancel", null)
//            .show()
//    }
//    private fun showPreScanNoteDialog() {
//        val dialog = BottomSheetDialog(this)
//        val view = layoutInflater.inflate(R.layout.bottomsheet_attendance_note, null)
//        dialog.setContentView(view)
//
//        val half = view.findViewById<TextView>(R.id.txtHalfDay)
//        val full = view.findViewById<TextView>(R.id.txtFullDay)
//        val custom = view.findViewById<TextView>(R.id.txtCustom)
//        val skip = view.findViewById<TextView>(R.id.txtSkip)
//
//        half.setOnClickListener {
//            selectedNote = "Half-Day"
//            selectedNoteType = "HALF_DAY"
//            dialog.dismiss()
//            startScan()
//        }
//
//        full.setOnClickListener {
//            selectedNote = "Full-Day"
//            selectedNoteType = "FULL_DAY"
//            dialog.dismiss()
//            startScan()
//        }
//
//        custom.setOnClickListener {
//            dialog.dismiss()
//            showCustomNoteInput()
//        }
//
//        skip.setOnClickListener {
//            selectedNote = null
//            selectedNoteType = null
//            dialog.dismiss()
//            startScan()
//        }
//
//        dialog.show()
//    }
//    private fun showCustomNoteInput() {
//        val input = android.widget.EditText(this)
//
//        android.app.AlertDialog.Builder(this)
//            .setTitle("Enter Custom Note")
//            .setView(input)
//            .setPositiveButton("OK") { _, _ ->
//                selectedNote = input.text.toString()
//                selectedNoteType = "CUSTOM"
//                startScan()
//            }
//            .setNegativeButton("Cancel") { d, _ ->
//                d.dismiss()
//                showPreScanNoteDialog()
//            }
//            .show()
//    }
//    private fun sendSelectedNote(attendanceId: Int) {
//        val prefs = getSharedPreferences("attendance_taker_prefs", MODE_PRIVATE)
//        val takerId = prefs.getInt("attendance_taker_id", 0)
//
//        val body = mapOf(
//            "attendance_id" to attendanceId,
//            "note" to selectedNote,
//            "note_type" to selectedNoteType,
//            "added_by" to takerId
//        )
//
//        lifecycleScope.launch(Dispatchers.IO) {
//            try {
//                RetrofitInstance.api.addAttendanceNote(body)
//
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@QrScannerActivity, "Attendance + Note Saved!", Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@QrScannerActivity, "Note Error: ${e.message}", Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//            }
//        }
//    }
//
//    private fun showNoteBottomSheet(attendanceId: Int) {
//
//        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
//        val view = layoutInflater.inflate(R.layout.bottomsheet_attendance_note, null)
//        dialog.setContentView(view)
//
//        val half = view.findViewById<TextView>(R.id.txtHalfDay)
//        val full = view.findViewById<TextView>(R.id.txtFullDay)
//        val custom = view.findViewById<TextView>(R.id.txtCustom)
//        val skip = view.findViewById<TextView>(R.id.txtSkip)
//
////        fun submit(note: String?) {
////            val prefs = getSharedPreferences("attendance_taker_prefs", MODE_PRIVATE)
////            val takerId = prefs.getInt("attendance_taker_id", 0)
////
////            lifecycleScope.launch(Dispatchers.IO) {
////                try {
////                    val prefs = getSharedPreferences("attendance_taker_prefs", MODE_PRIVATE)
////                    val token = prefs.getString("token", "") ?: ""
////
//////                    val body = mapOf(
//////                        "attendance_id" to attendanceId,
//////                        "note" to note
//////                    )
////
////                    val body = mapOf(
////                        "attendance_id" to attendanceId,
////                        "note" to note,
////                        "note_type" to when (note) {
////                            "Half-Day" -> "HALF_DAY"
////                            "Full-Day" -> "FULL_DAY"
////                            null -> null
////                            else -> "CUSTOM"
////                        },
////                        "added_by" to takerId
////                    )
////                    Log.d("NOTE_DEBUG", "Sending: $body")
////
////
////                    RetrofitInstance.api.addAttendanceNote("Bearer $token", body)
////
////                    withContext(Dispatchers.Main) {
////                        dialog.dismiss()
////                        finish()
////                    }
////                } catch (_: Exception) {}
////            }
////        }
//
//        fun submit(note: String?) {
//            val prefs = getSharedPreferences("attendance_taker_prefs", MODE_PRIVATE)
//            val takerId = prefs.getInt("attendance_taker_id", 0)
//            val token = prefs.getString("token", "") ?: ""
//
//            val body = mapOf(
//                "attendance_id" to attendanceId,
//                "note" to note,
//                "note_type" to when (note) {
//                    "Half-Day" -> "HALF_DAY"
//                    "Full Day" -> "FULL_DAY"
//                    null -> null
//                    else -> "CUSTOM"
//                },
//                "added_by" to takerId
//            )
//
//            Log.d("NOTE_DEBUG", "Sending Note Body: $body")
//
//            lifecycleScope.launch(Dispatchers.IO) {
//
//                try {
//                    val response = RetrofitInstance.api.addAttendanceNote( body)
//                    if (response.isSuccessful) {
//                        Log.d("NOTE_DEBUG", "NOTE SAVED SUCCESSFULLY")
//                    } else {
//                        Log.e("NOTE_DEBUG", "NOTE SAVE FAILED: ${response.errorBody()?.string()}")
//                    }
//
//
//                    withContext(Dispatchers.Main) {
//
//                        if (response.isSuccessful) {
//                            Log.d("NOTE_DEBUG", "NOTE SAVED SUCCESSFULLY")
//                        } else {
//                            Log.e("NOTE_DEBUG", "NOTE SAVE FAILED: ${response.errorBody()?.string()}")
//                        }
//
//                        dialog.dismiss()
//                        finish()
//                    }
//
//                } catch (e: Exception) {
//                    withContext(Dispatchers.Main) {
//                        Log.e("NOTE_DEBUG", "EXCEPTION: ${e.message}")
//                        dialog.dismiss()
//                        finish()
//                    }
//                }
//            }
//        }
//
//        half.setOnClickListener { submit("Half-Day") }
//        full.setOnClickListener { submit("Full Day") }
//
//        custom.setOnClickListener {
//            dialog.dismiss()
//            showCustomNoteDialog(attendanceId)
//        }
//
//        skip.setOnClickListener { submit(null) }
//
//        dialog.show()
//    }
//
//}
//
////    override fun onResponse(call: Call<AttendanceResponse>, response: Response<AttendanceResponse>) {
////    android.util.Log.d("QR_API", "Status: ${response.code()}, URL: ${call.request().url}")
////    android.util.Log.d("QR_API", "Body: ${response.body()}, ErrorBody: ${response.errorBody()?.string()}")
////
////    if (response.isSuccessful && response.body()?.success == true) {
////        Toast.makeText(
////            this@QrScannerActivity,
////            "✅ Attendance marked successfully for ${response.body()!!.message}",
////            Toast.LENGTH_LONG
////        ).show()
////
////        // ✅ Automatically close QR scanner and return to attendance screen
////        android.os.Handler(mainLooper).postDelayed({
////            finish()
////        }, 1500)
////
////    } else {
////        val errorMsg = try {
////            response.errorBody()?.string()?.let {
////                org.json.JSONObject(it).optString("message", "Unknown error")
////            }
////        } catch (e: Exception) {
////            "Unexpected error"
////        }
////        Toast.makeText(
////            this@QrScannerActivity,
////            "❌ Failed: $errorMsg",
////            Toast.LENGTH_LONG
////        ).show()
////    }
////}
////
////
////                override fun onFailure(call: Call<AttendanceResponse>, t: Throwable) {
////                    Toast.makeText(this@QrScannerActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
////                }
////            })
////        } catch (e: Exception) {
////    Toast.makeText(this, "Invalid QR format", Toast.LENGTH_LONG).show()
////}
////}
////}
////
////

package com.smartbus360.app.ui.screens

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.zxing.integration.android.IntentIntegrator
import com.smartbus360.app.R
import com.smartbus360.app.data.api.AttendanceRequest
import com.smartbus360.app.data.api.AttendanceResponse
import com.smartbus360.app.data.api.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import com.smartbus360.app.data.api.AddNoteRequest
import android.content.SharedPreferences
import android.content.Intent
import android.util.Log

class QrScannerActivity : AppCompatActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) showPreScanNoteDialog()
        else Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
    }
//    val prefs = getSharedPreferences("attendance_taker_prefs", MODE_PRIVATE)
//    val role = prefs.getString("role", "attendance_taker")
//    val authToken = prefs.getString("token", "") ?: ""

    private lateinit var prefs: SharedPreferences
    private var role: String = ""
    private var authToken: String = ""

    private var selectedNote: String? = null
    private var selectedNoteType: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_qr_scanner)
        Log.d("ROLE_DEBUG", "Loaded Role: $role")
        prefs = getSharedPreferences("attendance_taker_prefs", MODE_PRIVATE)
//        role = prefs.getString("role", "taker") ?: "taker"
//        role = prefs.getString("role", "taker")!!.lowercase()
        role = prefs.getString("role", "")!!.lowercase()
        authToken = prefs.getString("token", "") ?: ""

        Log.d("ROLE_DEBUG", "Loaded Role: $role")

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (role.lowercase() == "teacher") {
                showPreScanNoteDialog()
            } else {
                // Attendance coordinator → skip notes
                selectedNote = null
                selectedNoteType = null
                startScan()
            }

        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startScan() {
        val integrator = IntentIntegrator(this)
        integrator.setPrompt("Scan Student QR Code")
        integrator.setBeepEnabled(true)
        integrator.setOrientationLocked(true)
        integrator.setCaptureActivity(PortraitCaptureActivity::class.java)
        integrator.initiateScan()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: android.content.Intent?
    ) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {

            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                overridePendingTransition(0, 0)  // removes white flash
                finish()
            } else {
                handleQrResult(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleQrResult(data: String) {
        try {
            val json = JSONObject(data)
            val registrationNumber = json.getString("registrationNumber")
            val token = json.getString("token")

            val prefs = getSharedPreferences("attendance_taker_prefs", MODE_PRIVATE)
            val takerId = prefs.getInt("attendance_taker_id", 0)
            val authToken = prefs.getString("token", null)
            val role = prefs.getString("role", "taker") ?: "taker"
            val finalRole = prefs.getString("role", "taker") ?: "taker"

            if (takerId == 0 || authToken.isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    "⚠️ Please log in again to mark attendance.",
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            val request = AttendanceRequest(
                registrationNumber = registrationNumber,
                token = token,
                attendance_taker_id = takerId,
                latitude = 20.67,
                longitude = 81.60
            )

            lifecycleScope.launch {
                try {
//                    val response = RetrofitInstance.api.markAttendance("Bearer $authToken", request)
//                    val response = RetrofitInstance.api.markAttendance(
//                        token = "Bearer $authToken",
//                        role = finalRole,    // <-- SEND ROLE HERE
//                        request = request
//                    )

                    val prefs = getSharedPreferences("attendance_taker_prefs", MODE_PRIVATE)
                    val role = prefs.getString("role", "attendance_taker")!!

//                    val response = RetrofitInstance.api.markAttendance(
//                        "Bearer $authToken",
//                        role,
//                        request
//                    )
//                    val body = mapOf(
//                        "registrationNumber" to registrationNumber,
//                        "token" to token,
//                        "attendance_taker_id" to takerId,
//                        "latitude" to 20.67,
//                        "longitude" to 81.60
//                    )
//
//                    val response = RetrofitInstance.api.markAttendance(
//                        "Bearer $authToken",
//                        role,
//                        body
//                    )

                    val request = AttendanceRequest(
                        registrationNumber = registrationNumber,
                        token = token,
                        attendance_taker_id = takerId,
                        latitude = 20.67,
                        longitude = 81.60
                    )

                    val response = RetrofitInstance.api.markAttendance(
                        "Bearer $authToken",
                        role,
                        request
                    )


                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body()?.success == true) {
                            val attendanceId = response.body()?.attendance_id
                            if (attendanceId != null) {
                                sendSelectedNote(attendanceId)
                            } else {
                                Toast.makeText(
                                    this@QrScannerActivity,
                                    "Marked but no ID returned",
                                    Toast.LENGTH_LONG
                                ).show()
                                finish()
                            }
                        } else {
                            val errorMsg = try {
                                response.errorBody()?.string()?.let {
                                    JSONObject(it).optString("message", "Unknown error")
                                }
                            } catch (e: Exception) {
                                "Unexpected error"
                            }
                            Toast.makeText(
                                this@QrScannerActivity,
                                "❌ Failed: $errorMsg",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@QrScannerActivity,
                            "⚠️ Error: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

        } catch (e: Exception) {
            Toast.makeText(this, "Invalid QR format", Toast.LENGTH_LONG).show()
        }
    }

    private fun simulateQrScan() {
        val sample = """
        {
          "registrationNumber": "joseph1",
          "token": "9c1a93b8-9d73-4498-a774-3b0176d13521"
        }
    """.trimIndent()

        handleQrResult(sample)
    }

    // ---------------------------------------------------------
    // PRE-SCAN NOTE SELECTION
    // ---------------------------------------------------------
    private fun showPreScanNoteDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottomsheet_attendance_note, null)
        dialog.setContentView(view)

        dialog.setOnCancelListener {
            finish()
        }


        val half = view.findViewById<TextView>(R.id.txtHalfDay)
        val full = view.findViewById<TextView>(R.id.txtFullDay)
        val custom = view.findViewById<TextView>(R.id.txtCustom)
        val skip = view.findViewById<TextView>(R.id.txtSkip)

        half.setOnClickListener {
            selectedNote = "Half-Day"
            selectedNoteType = "HALF_DAY"
            dialog.dismiss()
            startScan()
//            simulateQrScan()
        }

        full.setOnClickListener {
            selectedNote = "Full-Day"
            selectedNoteType = "FULL_DAY"
            dialog.dismiss()
            startScan()
//            simulateQrScan()
        }

        custom.setOnClickListener {
            dialog.dismiss()
            showCustomNoteInput()
        }

        skip.setOnClickListener {
            selectedNote = null
            selectedNoteType = null
            dialog.dismiss()
            startScan()
//            simulateQrScan()
        }

        dialog.show()
    }

    private fun showCustomNoteInput() {
        val input = EditText(this)

        AlertDialog.Builder(this)
            .setTitle("Enter Custom Note")
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                selectedNote = input.text.toString()
                selectedNoteType = "CUSTOM"
                startScan()
//                simulateQrScan()
            }
            .setNegativeButton("Cancel") { d, _ ->
                d.dismiss()
                showPreScanNoteDialog()
            }
            .show()
    }

    // ---------------------------------------------------------
    // SEND NOTE AUTOMATICALLY AFTER SCAN
    // ---------------------------------------------------------
//    private fun sendSelectedNote(attendanceId: Int) {
//        val prefs = getSharedPreferences("attendance_taker_prefs", MODE_PRIVATE)
//        val takerId = prefs.getInt("attendance_taker_id", 0)
//        val role = prefs.getString("role", "attendance_taker")
//        if (role != "teacher") return   // attendance coordinator must NOT send notes
//
////        val body = mapOf(
////            "attendance_id" to attendanceId,
////            "note" to selectedNote,
////            "note_type" to selectedNoteType,
////            "added_by" to takerId
////        )
//        val request = AddNoteRequest(
//            attendance_id = attendanceId,
//            note = selectedNote,
//            note_type = selectedNoteType,
//            added_by = takerId
//        )
//
//
//
//        lifecycleScope.launch(Dispatchers.IO) {
//            try {
////                RetrofitInstance.api.addAttendanceNote("Bearer $authToken",request)
//                val prefs = getSharedPreferences("attendance_taker_prefs", MODE_PRIVATE)
//                val token = prefs.getString("token", "")!!
//                val role = prefs.getString("role", "")!!
//
////                RetrofitInstance.api.addAttendanceNote(
////                    "Bearer $token",
////                    role,
////                    body
////                )
//
//                val body = mapOf(
//                    "registrationNumber" to registrationNumber,
//                    "token" to token,
//                    "attendance_taker_id" to takerId,
//                    "latitude" to 20.67,
//                    "longitude" to 81.60
//                )
//
//                val response = RetrofitInstance.api.markAttendance(
//                    "Bearer $authToken",
//                    role,
//                    body
//                )
//
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@QrScannerActivity, "Attendance + Note Saved!", Toast.LENGTH_SHORT).show()
////                    finish()
//                    val intent = Intent("ATTENDANCE_UPDATED")
//                    sendBroadcast(intent)
//                    finish()
//
//                }
//
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@QrScannerActivity, "Note Error: ${e.message}", Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//            }
//        }
//    }
//}
    private fun sendSelectedNote(attendanceId: Int) {

        val prefs = getSharedPreferences("attendance_taker_prefs", MODE_PRIVATE)
        val takerId = prefs.getInt("attendance_taker_id", 0)
        val token = prefs.getString("token", "") ?: ""
        val role = prefs.getString("role", "") ?: ""

        // ❌ Only teachers can add notes
        if (role.lowercase() != "teacher") {
            Log.d("NOTE_DEBUG", "Role=$role → Skipping note (attendance taker cannot send notes)")
            finish()
            return
        }

        val request = AddNoteRequest(
            attendance_id = attendanceId,
            note = selectedNote,
            note_type = selectedNoteType,
            added_by = takerId
        )

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.addAttendanceNote(
                    "Bearer $token",
                    request
                )

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@QrScannerActivity, "Note saved!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this@QrScannerActivity, "Note failed!", Toast.LENGTH_SHORT)
                            .show()
                    }

                    sendBroadcast(Intent("ATTENDANCE_UPDATED"))
                    finish()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@QrScannerActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
    }
}