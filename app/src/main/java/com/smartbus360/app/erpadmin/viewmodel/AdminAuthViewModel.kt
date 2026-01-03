package com.smartbus360.app.erpadmin.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbus360.app.erpadmin.data.api.RetrofitClient
import com.smartbus360.app.erpadmin.data.model.AdminLoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AdminAuthViewModel : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String, onTokenReceived: (String) -> Unit) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = RetrofitClient.api.adminLogin(
                    AdminLoginRequest(
                        email = email,
                        password = password,
                        client_id = "90ddb74bd2b84b7870f5b693e8553ad3",
                        client_secret = "c566e750e7456ef3bf73b2446c3b84dd45f28ec8c64b25eb603d16c1d46a71fa"
                    )
                )

                if (response.isSuccessful && response.body() != null) {
                    val token = response.body()!!.accessToken
                    onTokenReceived(token)
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error("Invalid credentials")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Network error")
            }
        }
    }
}
