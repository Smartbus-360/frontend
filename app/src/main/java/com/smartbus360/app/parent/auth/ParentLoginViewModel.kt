package com.smartbus360.app.parent.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbus360.app.parent.models.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ParentLoginViewModel(
    private val repository: ParentAuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading
            try {
                val response = repository.login(username, password)
                if (response.isSuccessful && response.body() != null) {

                    val data = response.body()!!

                    repository.saveSession(
                        token = data.token,
                        userId = data.userId,
                        userName = data.userName
                    )

                    _loginState.value = LoginUiState.Success(data)
                } else {
                    _loginState.value = LoginUiState.Error("Invalid credentials")
                }
            } catch (e: Exception) {
                _loginState.value = LoginUiState.Error(e.message ?: "Network error")
            }
        }
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val data: LoginResponse) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
