//package com.smartbus360.app.data.model
//
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import androidx.core.content.ContextCompat
//import androidx.localbroadcastmanager.content.LocalBroadcastManager
//import com.google.android.gms.location.LocationResult
//import com.smartbus360.app.utility.LocationService
//// LocationReceiver.kt
//class LocationReceiver : BroadcastReceiver() {
//    override fun onReceive(ctx: Context, intent: Intent) {
//        val result = LocationResult.extractResult(intent) ?: return
//        val svcIntent = Intent(ctx, LocationService::class.java)
//            .setAction(LocationService.ACTION_START_SERVICE)
//        // Wake the service if needed
//        ContextCompat.startForegroundService(ctx, svcIntent)
//        // Forward the latest fix to the running service via a local broadcast or repository
//        result.locations.forEach { loc ->
//            LocalBroadcastManager.getInstance(ctx).sendBroadcast(
//                Intent("BG_LOCATION").apply {
//                    putExtra("lat", loc.latitude)
//                    putExtra("lng", loc.longitude)
//                    putExtra("speed", loc.speed)
//                }
//            )
//        }
//    }
//}
