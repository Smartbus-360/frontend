//package com.smartbus360.app.parent.fees
//
//
//import androidx.compose.runtime.*
//import androidx.compose.material3.*
//import androidx.compose.foundation.layout.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.text.font.FontWeight
//import androidx.lifecycle.viewmodel.compose.viewModel
//import android.content.Intent
//import android.net.Uri
//import androidx.compose.ui.platform.LocalContext
//import com.smartbus360.app.parent.fees.ParentFeesViewModel
//import androidx.compose.ui.Alignment
//
//@Composable
//fun ParentFeeDetailsScreen(
//    installmentId: Int
//) {
//    val viewModel: ParentFeesViewModel = viewModel()
//    val details = viewModel.details.collectAsState()
//
//    LaunchedEffect(Unit) {
//        viewModel.loadFeeDetails(installmentId)
//    }
//
//    details.value?.let { f ->
//
//        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//
//            Text(f.title, style = MaterialTheme.typography.headlineSmall)
//
//            Spacer(modifier = Modifier.height(15.dp))
//
//            Text("Amount: ₹${f.amount}", fontWeight = FontWeight.Bold)
//            Text("Due Date: ${f.dueDate}")
//            Text("Status: ${f.status}")
//
//            f.paidDate?.let {
//                Text("Paid Date: $it")
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            Text(f.description ?: "")
//
//            Spacer(modifier = Modifier.height(30.dp))
//
//            val context = LocalContext.current
//
//            if (f.receiptUrl != null) {
//                Button(
//                    onClick = {
//                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(f.receiptUrl))
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        context.startActivity(intent)                    },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("Download Receipt")
//                }
//            }
//        }
//
//    } ?: run {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            CircularProgressIndicator()
//        }
//    }
//}
