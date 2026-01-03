package com.smartbus360.app.utility


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.smartbus360.app.MainActivity
import com.smartbus360.app.R
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager

object NotificationHelper {

    private const val CHANNEL_ID = "smartbus360_attendance_channel"
    private const val CHANNEL_NAME = "Attendance Notifications"

    fun showSystemNotification(context: Context, title: String, message: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.bell)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setAutoCancel(true)
            .setSound(soundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

//        manager.notify(System.currentTimeMillis().toInt(), builder.build())
        manager.notify(1001, builder.build())

        // Increment unread attendance counter
//        try {
//            com.smartbus360.app.data.repository.PreferencesRepository(context)
//                .incrementUnreadAttendance()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
        try {
            val repo = com.smartbus360.app.data.repository.PreferencesRepository(context)
            repo.incrementUnreadAttendance()

            // 🔊 Broadcast the update to UI (even if background)
            val intent = Intent("ATTENDANCE_COUNT_UPDATED")
            context.sendBroadcast(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
