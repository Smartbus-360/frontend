package com.smartbus360.app.parent.leave

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

@Composable
fun ParentLeaveListScreen(
    studentId: Int,
    onApplyLeave: () -> Unit,
) {
    val viewModel: ParentLeaveViewModel = viewModel()
    val list = viewModel.leaveList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadLeaveHistory(studentId)
    }


    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Leave Requests", style = MaterialTheme.typography.headlineMedium)
            Button(onClick = onApplyLeave) { Text("Apply") }
        }

        Spacer(Modifier.height(20.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(list.value.size) { index ->
                val item = list.value[index]

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
////                        .clickable { onLeaveClick(item.id) },
//                    elevation = CardDefaults.cardElevation(4.dp),
//                    shape = RoundedCornerShape(16.dp)
                ) {


                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            "${item.fromDate} → ${item.toDate}",
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(item.reason)

                        Spacer(Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .background(
                                    when (item.status.lowercase()) {
                                        "approved" -> Color(0xFF4CAF50)
                                        "rejected" -> Color(0xFFF44336)
                                        else -> Color(0xFFFF9800) // pending
                                    },
                                            shape = MaterialTheme.shapes.small
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                item.status,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
