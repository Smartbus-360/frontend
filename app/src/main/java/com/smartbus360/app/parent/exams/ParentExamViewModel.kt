//package com.smartbus360.app.parent.exams
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.smartbus360.app.parent.api.ParentApiClient
//import com.smartbus360.app.parent.models.*
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import com.smartbus360.app.parent.models.ExamItem
//class ParentExamViewModel : ViewModel() {
//
//    private val _examList = MutableStateFlow<List<ExamItem>>(emptyList())
//    val examList: StateFlow<List<ExamItem>> = _examList
//
//    private val _results = MutableStateFlow<List<ExamSubject>>(emptyList())
//    val results: StateFlow<List<ExamSubject>> = _results
//
//    private val _examDetails = MutableStateFlow<ExamDetailsResponse?>(null)
//    val examDetails: StateFlow<ExamDetailsResponse?> = _examDetails
//
//    fun loadExams(studentId: Int) {
//        viewModelScope.launch {
//            val res = ParentApiClient.api.getExamList(studentId)
//            if (res.isSuccessful && res.body() != null) {
//                _examList.value = res.body()!!.exams
//            }
//        }
//    }
//
////
//fun loadResults(studentId: Int) {
//    viewModelScope.launch {
//        val res = ParentApiClient.api.getExamResults(studentId)
//        if (res.isSuccessful) {
//            _results.value = res.body()?.results ?: emptyList()
//        }
//    }
//}
//
//    fun loadExamDetails(examId: Int) {
//        viewModelScope.launch {
//            val res = ParentApiClient.api.getExamDetails(examId)
//            if (res.isSuccessful && res.body() != null) {
//                _examDetails.value = res.body()
//            }
//        }
//    }
//
//}


package com.smartbus360.app.parent.exams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbus360.app.parent.api.ParentApiClient
import com.smartbus360.app.parent.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ParentExamViewModel : ViewModel() {

    private val _examList = MutableStateFlow<List<ExamItem>>(emptyList())
    val examList: StateFlow<List<ExamItem>> = _examList

    private val _results = MutableStateFlow<List<ExamResultItem>>(emptyList())
    val results: StateFlow<List<ExamResultItem>> = _results

    fun loadExams(studentId: Int) {
        viewModelScope.launch {
            val res = ParentApiClient.api.examSchedule(studentId)
            if (res.isSuccessful && res.body()?.success == true) {
                _examList.value = res.body()!!.schedule
            }
        }
    }

    fun loadResults(studentId: Int) {
        viewModelScope.launch {
            val res = ParentApiClient.api.examResults(studentId)
            if (res.isSuccessful && res.body()?.success == true) {
                _results.value = res.body()!!.results
            }
        }
    }
}
