//package com.smartbus360.app.ui.screens
//
//
//import android.content.Context
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.lifecycleScope
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.smartbus360.app.R
//import com.smartbus360.app.data.api.RetrofitInstance
//import com.smartbus360.app.data.model.AttendanceItem
//import kotlinx.coroutines.launch
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout   // ✅ Correct import
//
//class StudentAttendanceActivity : AppCompatActivity() {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var adapter: AttendanceAdapter
//    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
//    private var token: String? = null    // ✅ Declare at class level
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_student_attendance)
//
//        recyclerView = findViewById(R.id.recyclerViewAttendance)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        adapter = AttendanceAdapter()
//        recyclerView.adapter = adapter
//
//        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
//
//        fun loadAttendance() {
//            if (token != null) {
//                lifecycleScope.launch {
//                    try {
//                        val response = RetrofitInstance.api.getMyAttendance("Bearer $token")
//                        adapter.setData(response)
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    } finally {
//                        swipeRefreshLayout.isRefreshing = false
//                    }
//                }
//            }
//        }
//
//        swipeRefreshLayout.setOnRefreshListener { loadAttendance() }
//
//        // initial load
//        swipeRefreshLayout.isRefreshing = true
//        loadAttendance()
//
//
//
//        // get saved token
//        val prefs = getSharedPreferences("student_prefs", Context.MODE_PRIVATE)
//        val token = prefs.getString("auth_token", null)
//
//        if (token != null) {
//            lifecycleScope.launch {
//                try {
//                    val response = RetrofitInstance.api.getMyAttendance("Bearer $token")
//                    adapter.setData(response)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
//}
package com.smartbus360.app.ui.screens

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout   // ✅ Correct import
import com.smartbus360.app.R
import com.smartbus360.app.data.api.RetrofitInstance
import kotlinx.coroutines.launch
import android.widget.Toast
import android.util.Log
import com.smartbus360.app.data.repository.PreferencesRepository
import android.content.Intent

class StudentAttendanceActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AttendanceAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var token: String? = null    // ✅ Declare at class level

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_attendance)
        val repo = PreferencesRepository(this)
        repo.setUnreadAttendanceCount(0)  // reset when viewing attendance
        val intent = Intent("ATTENDANCE_COUNT_UPDATED")
        sendBroadcast(intent)


        recyclerView = findViewById(R.id.recyclerViewAttendance)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AttendanceAdapter()
        recyclerView.adapter = adapter

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)

        // ✅ Get saved token once
        val prefs = getSharedPreferences("student_prefs", Context.MODE_PRIVATE)
        token = prefs.getString("auth_token", null)
        if (token.isNullOrEmpty()) {
            Toast.makeText( this@StudentAttendanceActivity, "Login again — Auth token missing", Toast.LENGTH_SHORT).show()
            return
        }


        // ✅ Define function properly
        fun loadAttendance() {
            if (token != null) {
                swipeRefreshLayout.isRefreshing = true
                lifecycleScope.launch {
                    try {
                        Log.d("AttendanceAPI", "Calling API with token: Bearer $token")
                        val response = RetrofitInstance.api.getMyAttendance("Bearer $token")
                        Log.d("AttendanceAPI", "Raw API Response: $response")
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body?.success == true && body.attendance != null) {
                                adapter.setData(body.attendance)
                            } else {
                                adapter.setData(emptyList())
                                Toast.makeText(this@StudentAttendanceActivity, "No attendance found", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@StudentAttendanceActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }

//                    } else {
//                            Log.d("AttendanceAPI", "No attendance found or success=false")
//                            adapter.setData(emptyList())
//                            Toast.makeText(this@StudentAttendanceActivity, "No attendance found", Toast.LENGTH_SHORT).show()
//                        }

                    } catch (e: Exception) {
                        Log.e("AttendanceAPI", "Error loading attendance", e)
                        Toast.makeText(
                            this@StudentAttendanceActivity,
                            "Error loading attendance: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } finally {
                        swipeRefreshLayout.isRefreshing = false
                    }
                }
            } else {
                Toast.makeText(this, "Missing auth token", Toast.LENGTH_SHORT).show()


        }
        }

        // ✅ Setup pull-to-refresh listener
        swipeRefreshLayout.setOnRefreshListener { loadAttendance() }

        // ✅ Initial load
        loadAttendance()
    }
}
