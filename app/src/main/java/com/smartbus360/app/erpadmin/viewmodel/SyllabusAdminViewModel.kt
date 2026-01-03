package com.smartbus360.app.erpadmin.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbus360.app.erpadmin.data.api.RetrofitClient
import com.smartbus360.app.erpadmin.data.model.SyllabusProgress
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class SyllabusState {
    object Loading : SyllabusState()
    data class Success(val list: List<SyllabusProgress>) : SyllabusState()
    data class Error(val message: String) : SyllabusState()
}

class SyllabusAdminViewModel : ViewModel() {

    private val _state = MutableStateFlow<SyllabusState>(SyllabusState.Loading)
    val state: StateFlow<SyllabusState> = _state

    fun loadSyllabusProgress(token: String) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.syllabusAdminApi.getSyllabusProgress(token)
                if (res.isSuccessful) {
                    _state.value = SyllabusState.Success(res.body() ?: emptyList())
                } else {
                    _state.value = SyllabusState.Error("Failed to load syllabus progress")
                }
            } catch (e: Exception) {
                _state.value = SyllabusState.Error(e.message ?: "Error")
            }
        }
    }
}
