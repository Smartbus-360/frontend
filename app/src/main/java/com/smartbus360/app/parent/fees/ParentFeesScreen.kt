package com.smartbus360.app.parent.fees

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import com.smartbus360.app.parent.models.FeeItem
import androidx.compose.ui.unit.sp

@Composable
fun ParentFeesScreen(studentId: Int) {

    val viewModel: ParentFeesViewModel = viewModel()
    val pending by viewModel.pending.collectAsState()
    val history by viewModel.history.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFees(studentId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text("Fees", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Pending Fees", fontWeight = FontWeight.Bold)

        if (pending.isEmpty()) {
            Text("No pending fees", color = Color.Gray)
        } else {
            pending.forEach {
                FeeRow(it)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Payment History", fontWeight = FontWeight.Bold)

        if (history.isEmpty()) {
            Text("No payment history", color = Color.Gray)
        } else {
            history.forEach {
                FeeRow(it)
            }
        }
    }
}
@Composable
fun FeeRow(item: FeeItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(item.title ?: "Fee", fontWeight = FontWeight.Bold)
            Text("Amount: ₹${item.amount ?: 0}")
            item.dueDate?.let { Text("Due: $it") }
            item.status?.let { Text("Status: $it") }
        }
    }
}


