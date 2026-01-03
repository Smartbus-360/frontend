package com.smartbus360.app.parent.homework

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.smartbus360.app.parent.models.HomeworkItem
import com.smartbus360.app.parent.models.HomeworkListResponse

@Composable
fun ParentHomeworkDetailsScreen(
    homeworkId: Int
) {
    val viewModel: ParentHomeworkViewModel = viewModel()
    val details = viewModel.details.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadHomeworkDetails(homeworkId)
    }

    details.value?.let { hw ->

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            Text(hw.subject, style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(10.dp))

            Text(hw.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)

            Spacer(modifier = Modifier.height(15.dp))

            Text(hw.description)

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Due Date: ${hw.dueDate}",
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(30.dp))
            val context = LocalContext.current   // ✅ FIXED

            if (hw.fileUrl != null) {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(hw.fileUrl))
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)   // ✅ USE CONTEXT SAFELY
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Attachment")
                }
            }
        }
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
