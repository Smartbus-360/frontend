//package com.smartbus360.app.parent.profile
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.smartbus360.app.parent.api.ParentApiClient
//import com.smartbus360.app.parent.models.*
//import kotlinx.coroutines.flow.*
//import kotlinx.coroutines.launch
//import com.smartbus360.app.parent.models.ParentProfileResponse
//import com.smartbus360.app.parent.models.ChildInfo
//
//class ParentProfileViewModel : ViewModel() {
//
//    private val _profile = MutableStateFlow<ParentProfileResponse?>(null)
//    val profile: StateFlow<ParentProfileResponse?> = _profile
//
//    fun loadProfile() {
//        viewModelScope.launch {
//            val res = ParentApiClient.api.getParentProfile()
//            if (res.isSuccessful && res.body() != null) {
//                _profile.value = res.body()
//            }
//        }
//    }
//}
