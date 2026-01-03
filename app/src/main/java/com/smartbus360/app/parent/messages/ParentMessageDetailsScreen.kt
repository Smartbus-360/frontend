package com.smartbus360.app.parent.messages

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smartbus360.app.parent.models.MessageItem

@Composable
fun ParentMessageDetailsScreen(
    studentId: Int,
    message: MessageItem
) {
    val viewModel: ParentMessageViewModel = viewModel()
    val context = LocalContext.current
    var replyText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // 🔹 Date
        Text(
            "Date: ${message.createdAt.substring(0, 10)}",
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Message body
        Text(
            message.message,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Attachment
        if (message.fileUrl != null) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(message.fileUrl))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            ) {
                Text("View Attachment")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // 🔹 Reply box
        OutlinedTextField(
            value = replyText,
            onValueChange = { replyText = it },
            label = { Text("Reply") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 Send reply (MATCHES CURL)
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = replyText.isNotBlank(),
            onClick = {
                viewModel.replyMessage(
                    studentId = studentId,
                    receiverId = message.senderId, // ✅ REQUIRED BY BACKEND
                    message = replyText
                )
                replyText = ""
            }
        ) {
            Text("Send")
        }
    }
}
