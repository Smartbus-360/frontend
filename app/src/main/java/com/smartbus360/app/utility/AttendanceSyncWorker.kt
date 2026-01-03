package com.smartbus360.app.utility


import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.smartbus360.app.data.api.RetrofitInstance
import com.smartbus360.app.data.repository.PreferencesRepository
import kotlinx.coroutines.runBlocking

class AttendanceSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    override fun doWork(): Result = runBlocking {
        try {
            val repo = PreferencesRepository(applicationContext)
            val token = repo.getAuthToken()

            if (token.isNullOrEmpty()) {
                Log.w("AttendanceWorker", "No token found — skipping sync")
                return@runBlocking Result.success()
            }

            val response = RetrofitInstance.api.getUnreadAttendanceCount("Bearer $token")

//            if (response.isSuccessful) {
//                val unreadCount = response.body()?.get("count") ?: 0
//                val oldCount = repo.getUnreadAttendanceCount()
//
//                // Save only if changed
//                if (unreadCount != oldCount) {
//                    repo.setUnreadAttendanceCount(unreadCount)
//
//                    // Notify UI (if app open)
//                    val intent = android.content.Intent("ATTENDANCE_COUNT_UPDATED")
//                    applicationContext.sendBroadcast(intent)
//
//                    // Optional: Trigger a small notification when count increases
//                    if (unreadCount > oldCount) {
//                        showAttendanceNotification(unreadCount)
//                    }
//                }
//
//                Log.d("AttendanceWorker", "Unread count updated: $unreadCount")
//            } else {
//                Log.e("AttendanceWorker", "API failed with code ${response.code()}")
//            }
            if (response.isSuccessful) {
                val unreadCount = response.body()?.get("count") ?: 0
                val oldCount = repo.getUnreadAttendanceCount()

                Log.d("AttendanceWorker", "📊 Old=$oldCount New=$unreadCount")

                if (unreadCount > oldCount) {
                    repo.setUnreadAttendanceCount(unreadCount)

                    // 🔔 Show system notification when new attendance appears
                    com.smartbus360.app.utility.NotificationHelper.showSystemNotification(
                        applicationContext,
                        "Attendance Update",
                        "You have $unreadCount unread attendance records."
                    )

                    // 🔁 Update red dot on UI instantly
                    val intent = android.content.Intent("ATTENDANCE_COUNT_UPDATED")
                    applicationContext.sendBroadcast(intent)
                } else if (unreadCount < oldCount) {
                    // If count decreases (e.g., user opened attendance)
                    repo.setUnreadAttendanceCount(unreadCount)
                    val intent = android.content.Intent("ATTENDANCE_COUNT_UPDATED")
                    applicationContext.sendBroadcast(intent)
                }
            }
            Result.success()
        } catch (e: Exception) {
            Log.e("AttendanceWorker", "Error syncing unread count", e)
            Result.retry()
        }
    }

//    private fun showAttendanceNotification(count: Int) {
//        try {
//            val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
//                    as android.app.NotificationManager
//            val channelId = "attendance_channel"
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                val channel = android.app.NotificationChannel(
//                    channelId,
//                    "Attendance Updates",
//                    android.app.NotificationManager.IMPORTANCE_DEFAULT
//                )
//                manager.createNotificationChannel(channel)
//            }
//
//            val builder = android.app.Notification.Builder(applicationContext, channelId)
//                .setSmallIcon(android.R.drawable.ic_dialog_info)
//                .setContentTitle("New Attendance Update")
//                .setContentText("You have $count unread attendance records.")
//                .setAutoCancel(true)
//
//            manager.notify(1001, builder.build())
//        } catch (e: Exception) {
//            Log.e("AttendanceWorker", "Notification error", e)
//        }
//    }
}
