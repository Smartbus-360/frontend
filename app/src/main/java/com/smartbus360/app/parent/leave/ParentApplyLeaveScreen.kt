package com.smartbus360.app.parent.leave

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ParentApplyLeaveScreen(
    studentId: Int,
    onBack: () -> Unit
) {
    val viewModel: ParentLeaveViewModel = viewModel()
    val applyStatus = viewModel.applyStatus.collectAsState()
    val context = LocalContext.current

    var fromDate by remember { mutableStateOf("") }
    var toDate by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }

    if (applyStatus.value == true) {
        Toast.makeText(context, "Leave Applied Successfully", Toast.LENGTH_LONG).show()
        onBack()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text("Apply Leave", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = fromDate,
            onValueChange = { fromDate = it },
            label = { Text("From Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = toDate,
            onValueChange = { toDate = it },
            label = { Text("To Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = reason,
            onValueChange = { reason = it },
            label = { Text("Reason") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                viewModel.applyLeave(studentId, fromDate, toDate, reason)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}
