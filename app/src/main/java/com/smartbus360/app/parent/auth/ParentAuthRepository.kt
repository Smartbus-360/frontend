package com.smartbus360.app.parent.auth

import com.smartbus360.app.parent.api.ParentApiClient
import com.smartbus360.app.parent.models.LoginRequest
import com.smartbus360.app.data.repository.PreferencesRepository

class ParentAuthRepository(
    private val prefs: PreferencesRepository
) {

    suspend fun login(username: String, password: String) =
        ParentApiClient.api.login(
            LoginRequest(username, password)
        )

    fun saveSession(token: String, userId: Int, userName: String) {
        prefs.setAuthToken(token)
        prefs.setUserId(userId)
        prefs.setUserName(userName)
        prefs.setLoggedIn(true)
    }
}
