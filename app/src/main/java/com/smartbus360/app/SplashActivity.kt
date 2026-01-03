//
//package com.smartbus360.app
//
//import android.content.Intent
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import androidx.appcompat.app.AppCompatActivity
//import com.smartbus360.app.R
//
//class SplashActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash) // start with Goodscore
//
//        Handler(Looper.getMainLooper()).postDelayed({
//
//        }, 2000) // show Goodscore for 2 seconds
//    }
//}

package com.smartbus360.app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.smartbus360.app.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash) // start with Goodscore

        Handler(Looper.getMainLooper()).postDelayed({
            // Navigate to the next screen after 2 seconds
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // close SplashActivity so user can’t return to it
        }, 2000)
    }
}
