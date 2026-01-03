//package com.smartbus360.app.parent.circulars
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.smartbus360.app.parent.api.ParentApiClient
//import com.smartbus360.app.parent.models.*
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import com.smartbus360.app.parent.models.CircularDetailsResponse
//
//class ParentCircularViewModel : ViewModel() {
//
//    private val _list = MutableStateFlow<List<CircularItem>>(emptyList())
//    val list: StateFlow<List<CircularItem>> = _list
//
//    private val _details = MutableStateFlow<CircularDetailsResponse?>(null)
//    val details: StateFlow<CircularDetailsResponse?> = _details
//
//    fun loadCircularList(studentId:Int) {
//        viewModelScope.launch {
//            val res = ParentApiClient.api.getCirculars(studentId)
//            if (res.isSuccessful ) {
//                _list.value = res.body()?.circulars?: emptyList()
//            }
//        }
//    }
//
//    fun loadCircularDetails(id: Int) {
//        viewModelScope.launch {
//            val res = ParentApiClient.api.getCircularDetails(id)
//            if (res.isSuccessful) {
//                _details.value = res.body()
//            }
//        }
//    }
//}
