package com.smartbus360.app.parent.homework


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbus360.app.parent.api.ParentApiClient
import com.smartbus360.app.parent.models.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ParentHomeworkViewModel : ViewModel() {

    private val _list = MutableStateFlow<List<HomeworkItem>>(emptyList())
    val list: StateFlow<List<HomeworkItem>> = _list

    private val _details = MutableStateFlow<HomeworkDetails?>(null)
    val details: StateFlow<HomeworkDetails?> = _details

    fun loadHomework(studentId: Int) {
        viewModelScope.launch {
            val res = ParentApiClient.api.homeworkList(studentId)
            if (res.isSuccessful && res.body()?.success == true) {
                _list.value = res.body()!!.homework
            }
        }
    }

    fun loadHomeworkDetails(id: Int) {
        viewModelScope.launch {
            val res = ParentApiClient.api.homeworkDetails(id)
            if (res.isSuccessful && res.body()?.success == true) {
                _details.value = res.body()!!.homework
            }
        }
    }


}
