package com.smartbus360.app.erpadmin.util


import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("admin_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("TOKEN", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("TOKEN", null)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
    fun saveInstituteId(id: Int) {
        prefs.edit().putInt("institute_id", id).apply()
    }

    // ✅ ADD THIS
    fun getInstituteId(): Int {
        return prefs.getInt("institute_id", 0)
    }

}
