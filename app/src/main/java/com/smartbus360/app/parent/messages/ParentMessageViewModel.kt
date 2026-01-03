package com.smartbus360.app.parent.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbus360.app.parent.api.ParentApiClient
import com.smartbus360.app.parent.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.smartbus360.app.parent.api.ReplyMessageRequest

class ParentMessageViewModel : ViewModel() {

    private val _list = MutableStateFlow<List<MessageItem>>(emptyList())
    val list: StateFlow<List<MessageItem>> = _list

    fun loadMessages(studentId: Int) {
        viewModelScope.launch {
            val res = ParentApiClient.api.getMessages(studentId)
            if (res.isSuccessful && res.body()?.success == true) {
                _list.value = res.body()!!.messages
            }
        }
    }

    fun replyMessage(
        studentId: Int,
        receiverId: Int,
        message: String
    ) {
        viewModelScope.launch {
            val body = ReplyMessageRequest(
                receiverId = receiverId,
                message = message
            )
            ParentApiClient.api.replyMessage(studentId, body)
        }
    }
}
