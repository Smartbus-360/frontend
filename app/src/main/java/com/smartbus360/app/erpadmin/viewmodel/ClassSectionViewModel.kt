package com.smartbus360.app.erpadmin.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbus360.app.erpadmin.data.api.RetrofitClient
import com.smartbus360.app.erpadmin.data.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class ClassState {
    object Loading : ClassState()
    data class Success(val classes: List<SchoolClass>) : ClassState()
    data class Error(val message: String) : ClassState()
}

sealed class SectionState {
    object Loading : SectionState()
    data class Success(val sections: List<Section>) : SectionState()
    data class Error(val message: String) : SectionState()
}

//class ClassSectionViewModel : ViewModel() {
//
//    private val _classState = MutableStateFlow<ClassState>(ClassState.Loading)
//    val classState: StateFlow<ClassState> = _classState
//
//    private val _sectionState = MutableStateFlow<SectionState>(SectionState.Loading)
//    val sectionState: StateFlow<SectionState> = _sectionState
//
//    class ClassSectionViewModel : ViewModel() {
//
//        private val _classState = MutableStateFlow<ClassState>(ClassState.Loading)
//        val classState: StateFlow<ClassState> = _classState
//
//        fun loadClasses(token: String) {
//            viewModelScope.launch {
//                _classState.value = ClassState.Loading
//                try {
//                    val res = RetrofitClient.classSectionApi.getClasses(token)
//                    if (res.isSuccessful) {
//                        _classState.value =
//                            ClassState.Success(res.body()?.classes ?: emptyList())
//                    } else {
//                        _classState.value = ClassState.Error("Failed to load classes")
//                    }
//                } catch (e: Exception) {
//                    _classState.value = ClassState.Error(e.message ?: "Error")
//                }
//            }
//        }
//
//        fun addClass(token: String, className: String, onDone: () -> Unit) {
//            viewModelScope.launch {
//                RetrofitClient.classSectionApi.addClass(
//                    token,
//                    mapOf("className" to className)
//                )
//                onDone()
//            }
//        }
//
//        fun addSection(
//            token: String,
//            sectionName: String,
//            classId: Int,
//            instituteId: Int,
//            onDone: () -> Unit
//        ) {
//            viewModelScope.launch {
//                RetrofitClient.classSectionApi.addSection(
//                    token,
//                    AddSectionRequest(sectionName, classId, instituteId)
//                )
//                onDone()
//            }
//        }
//
//        fun deleteClass(token: String, id: Int, onDone: () -> Unit) {
//            viewModelScope.launch {
//                RetrofitClient.classSectionApi.deleteClass(token, id)
//                onDone()
//            }
//        }
//    }
//}

class ClassSectionViewModel : ViewModel() {

    private val _classState = MutableStateFlow<ClassState>(ClassState.Loading)
    val classState: StateFlow<ClassState> = _classState

    private val _sectionState = MutableStateFlow<SectionState>(SectionState.Loading)
    val sectionState: StateFlow<SectionState> = _sectionState

    fun loadClasses(token: String) {
        viewModelScope.launch {
            val res = RetrofitClient.classSectionApi.getClasses(token)
            if (res.isSuccessful) {
                _classState.value =
                    ClassState.Success(res.body()?.classes ?: emptyList())
            } else {
                _classState.value = ClassState.Error("Failed to load classes")
            }
        }
    }

    fun addClass(token: String, className: String, onDone: () -> Unit) {
        viewModelScope.launch {
            RetrofitClient.classSectionApi.addClass(
                token,
                mapOf("className" to className)
            )
            onDone()
        }
    }

    fun deleteClass(token: String, id: Int, onDone: () -> Unit) {
        viewModelScope.launch {
            RetrofitClient.classSectionApi.deleteClass(token, id)
            onDone()
        }
    }

    fun loadSections(token: String, classId: Int) {
        viewModelScope.launch {
            val res = RetrofitClient.classSectionApi.getSections(token, classId)
            if (res.isSuccessful) {
                _sectionState.value =
                    SectionState.Success(res.body()?.sections ?: emptyList())
            } else {
                _sectionState.value = SectionState.Error("Failed to load sections")
            }
        }
    }

    fun addSection(
        token: String,
        sectionName: String,
        classId: Int,
        instituteId: Int,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            RetrofitClient.classSectionApi.addSection(
                token,
                AddSectionRequest(sectionName, classId, instituteId)
            )
            onDone()
        }
    }

    fun deleteSection(token: String, id: Int, onDone: () -> Unit) {
        viewModelScope.launch {
            RetrofitClient.classSectionApi.deleteSection(token, id)
            onDone()
        }
    }
}
