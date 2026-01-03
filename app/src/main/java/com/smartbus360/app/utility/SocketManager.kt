//package com.smartbus360.app.utility
//
//
//import android.content.Context
//import android.util.Log
//import io.socket.client.IO
//import io.socket.client.Socket
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import org.json.JSONObject
//import com.smartbus360.app.data.repository.PreferencesRepository
//import android.content.Intent
//
//
//object SocketManager {
//    private var socket: Socket? = null
//    private var studentId: Int? = null
//    private var isConnected = false
//    private var isConnecting = false
//
//    fun connectStudentSocket(context: Context, studentId: Int) {
//        this.studentId = studentId
//
//        if (socket == null) socket = IO.socket("https://api.smartbus360.com/students")
//
//        socket?.on(Socket.EVENT_CONNECT) {
//            Log.d("SocketIO", "✅ Connected to /students namespace")
//            isConnected = true
//            this.studentId?.let {
//                socket?.emit("registerStudent", it)
//                Log.d("SocketIO", "🎓 Registered student_$it after connect")
//            }
//        }
//// 🔔 Listen for attendance sheet updates
//        socket?.on("attendance_updated") { args ->
//            Log.d("SocketIO", "📊 Attendance sheet updated: ${args.joinToString()}")
//            val repo = PreferencesRepository(context)
//            repo.incrementUnreadAttendance()
//            val intent = Intent("ATTENDANCE_COUNT_UPDATED")
//            context.sendBroadcast(intent)
//        }
//
//        socket?.on("attendance_notification") { args ->
//            Log.d("SocketIO", "📩 Received attendance_notification: ${args.joinToString()}")
//            if (args.isNotEmpty()) {
//                val data = args[0] as JSONObject
//                val title = data.optString("title", "SMART BUS 360")
//                val message = data.optString("message", "Attendance notification received")
//                val time = data.optString("time", "")
//                val date = data.optString("date", "")
//
//                // 🟢 Always show system notification even if app is closed
//                CoroutineScope(Dispatchers.Main).launch {
//                    NotificationHelper.showSystemNotification(context, title, message)
//                    Log.d("SocketIO", "🔔 Attendance notification displayed")
//                }
//            }
//        }
//
//
//        socket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
//            Log.e("SocketIO", "❌ Connection error: ${args.joinToString()}")
//            isConnected = false
//        }
//
//        socket?.on(Socket.EVENT_DISCONNECT) {
//            Log.w("SocketIO", "⚠️ Disconnected from /students namespace")
//            isConnected = false
//        }
//
//        socket?.connect()
//    }
//
//    fun ensureConnected(context: Context, studentId: Int) {
//        if (!isConnected) connectStudentSocket(context, studentId)
//    }
//
//    fun disconnectStudentSocket() {
//        socket?.disconnect()
//        socket?.off()
//        socket = null
//        isConnected = false
//        Log.d("SocketIO", "❌ Student socket disconnected manually")
//    }
//
//    fun autoReconnect(context: Context) {
//        CoroutineScope(Dispatchers.IO).launch {
//            while (true) {
//                try {
//                    if (!isConnected && studentId != null) {
//                        Log.d("SocketIO", "♻️ Auto-reconnecting socket...")
//                        connectStudentSocket(context, studentId!!)
//                    }
//                } catch (e: Exception) {
//                    Log.e("SocketIO", "Reconnect error: ${e.message}")
//                }
//                kotlinx.coroutines.delay(30000) // check every 30 seconds
//            }
//        }
//    }
//
//}
