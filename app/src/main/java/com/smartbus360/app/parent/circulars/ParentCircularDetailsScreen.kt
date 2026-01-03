//package com.smartbus360.app.parent.circulars
//
//import android.content.Intent
//import android.net.Uri
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.*
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.compose.ui.platform.LocalContext
//
//@Composable
//fun ParentCircularDetailsScreen(
//    circularId: Int
//) {
//    val viewModel: ParentCircularViewModel = viewModel()
//    val data = viewModel.details.collectAsState()
//
//    LaunchedEffect(Unit) {
//        viewModel.loadCircularDetails(circularId)
//    }
//
//    data.value?.let { c ->
//
//        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//
//            Text(c.title, style = MaterialTheme.typography.headlineMedium)
//
//            Spacer(Modifier.height(10.dp))
//
//            Text("Issued By: ${c.issuedBy}", fontWeight = FontWeight.Medium)
//
//            Spacer(Modifier.height(10.dp))
//
//            Text("Date: ${c.date}", color = MaterialTheme.colorScheme.secondary)
//
//            Spacer(Modifier.height(20.dp))
//
//            Text(c.description)
//
//            Spacer(Modifier.height(20.dp))
//
//            if (c.fileUrl != null) {
//                val context = LocalContext.current
//
//                Button(
//                    modifier = Modifier.fillMaxWidth(),
//                    onClick = {
//                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(c.fileUrl))
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        context.startActivity(intent)
//                    }
//                ) {
//                    Text("View Attachment")
//                }
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
