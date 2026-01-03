//package com.smartbus360.app.parent.circulars
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.*
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.ui.unit.sp
//
//@Composable
//fun ParentCircularListScreen(
//    onCircularClick: (Int) -> Unit
//) {
//    val viewModel: ParentCircularViewModel = viewModel()
//    val list = viewModel.list.collectAsState()
//
//    LaunchedEffect(Unit) {
//        val studentId = 1
//        viewModel.loadCircularList(studentId)
//    }
//
//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//
//        Text("Circulars", style = MaterialTheme.typography.headlineMedium)
//
//        Spacer(Modifier.height(20.dp))
//
//        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//            items(list.value.size) { index ->
//                val item = list.value[index]
//
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { onCircularClick(item.id) },
//                    elevation = CardDefaults.cardElevation(4.dp),
//                    shape = RoundedCornerShape(16.dp)
//                ) {
//
//                    Column(modifier = Modifier.padding(16.dp)) {
//
//                        Text(item.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
//
//                        Spacer(Modifier.height(6.dp))
//
//                        Text(item.shortDescription.take(80) + "...")
//
//                        Spacer(Modifier.height(10.dp))
//
//                        Row(
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
//                            Text(item.date, color = MaterialTheme.colorScheme.secondary)
//
//                            if (item.fileUrl != null) {
//                                Text(
//                                    "Attachment",
//                                    color = MaterialTheme.colorScheme.primary,
//                                    fontWeight = FontWeight.SemiBold
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
