//package com.smartbus360.app.parent.leave
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.*
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//
//@Composable
//fun ParentLeaveDetailsScreen(
//    leaveId: Int
//) {
//    val viewModel: ParentLeaveViewModel = viewModel()
//    val data = viewModel.leaveDetails.collectAsState()
//
//    LaunchedEffect(Unit) {
//        viewModel.loadLeaveDetails(leaveId)
//    }
//
//    data.value?.let { leave ->
//
//        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//
//            Text("Leave Details", style = MaterialTheme.typography.headlineMedium)
//
//            Spacer(Modifier.height(20.dp))
//
//            Text("From: ${leave.fromDate}", fontWeight = FontWeight.SemiBold)
//            Text("To: ${leave.toDate}", fontWeight = FontWeight.SemiBold)
//
//            Spacer(Modifier.height(10.dp))
//
//            Text("Reason: ${leave.reason}")
//
//            Spacer(Modifier.height(10.dp))
//
//            Text("Applied On: ${leave.appliedOn}")
//
//            leave.teacherRemark?.let {
//                Spacer(Modifier.height(10.dp))
//                Text("Teacher Remark: $it")
//            }
//
//            Spacer(Modifier.height(20.dp))
//
//            Box(
//                modifier = Modifier
//                    .background(
//                        when (leave.status) {
//                            "Approved" -> Color(0xFF4CAF50)
//                            "Rejected" -> Color(0xFFF44336)
//                            else -> Color(0xFFFF9800)
//                        },
//                        shape = MaterialTheme.shapes.small
//                    )
//                    .padding(horizontal = 10.dp, vertical = 6.dp)
//            ) {
//                Text(leave.status, color = Color.White, fontWeight = FontWeight.Bold)
//            }
//        }
//
//    } ?: run {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) { CircularProgressIndicator() }
//    }
//}
