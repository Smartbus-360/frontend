package com.smartbus360.app.teacher.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.smartbus360.app.teacher.data.repository.TeacherRepository
import com.smartbus360.app.teacher.data.model.TeacherDashboardResponse
import com.smartbus360.app.data.repository.PreferencesRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.smartbus360.app.teacher.data.model.StoredTeacherProfile

sealed class TeacherDashboardState {
    object Loading : TeacherDashboardState()
    data class Success(val data: TeacherDashboardResponse) : TeacherDashboardState()
    data class Error(val message: String) : TeacherDashboardState()
}

//class TeacherDashboardViewModel : ViewModel() {

class TeacherDashboardViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = TeacherRepository()
    private val prefs = PreferencesRepository(getApplication())

    private val _state = MutableStateFlow<TeacherDashboardState>(TeacherDashboardState.Loading)
    val state: StateFlow<TeacherDashboardState> = _state

    fun loadDashboard(token: String) {
        _state.value = TeacherDashboardState.Loading

        viewModelScope.launch {
            val result = repository.getDashboard(token)

            _state.value = result.fold(
                onSuccess = { response ->

                    val teacher = response.teacher

                    // ✅ SAVE TEACHER PROFILE LOCALLY
                    prefs.saveTeacherProfile(
                        StoredTeacherProfile(
                            classId = teacher.classId,
                            sectionId = teacher.sectionId,
                            fullName = teacher.full_name,
                            username = teacher.username,
                            email = teacher.email,
                            phone = teacher.phone
                        )
                    )

                    TeacherDashboardState.Success(response)
                },
                onFailure = { TeacherDashboardState.Error(it.message ?: "Dashboard error") }
            )
        }
    }
}
