package com.smartbus360.app.teacher.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.smartbus360.app.teacher.data.repository.TeacherRepository
import com.smartbus360.app.teacher.data.model.TeacherLoginResponse

sealed class TeacherAuthState {
    object Idle : TeacherAuthState()
    object Loading : TeacherAuthState()
    data class Success(val data: TeacherLoginResponse) : TeacherAuthState()
    data class Error(val message: String) : TeacherAuthState()
}

class TeacherAuthViewModel : ViewModel() {

    private val repository = TeacherRepository()

    private val _authState = MutableStateFlow<TeacherAuthState>(TeacherAuthState.Idle)
    val authState: StateFlow<TeacherAuthState> = _authState

    fun login(email: String, password: String) {
        _authState.value = TeacherAuthState.Loading

        viewModelScope.launch {
            val result = repository.loginTeacher(email, password)

            _authState.value = result.fold(
                onSuccess = { TeacherAuthState.Success(it) },
                onFailure = { TeacherAuthState.Error(it.message ?: "Login error") }
            )
        }
    }
}
