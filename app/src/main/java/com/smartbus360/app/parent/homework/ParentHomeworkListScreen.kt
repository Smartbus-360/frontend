package com.smartbus360.app.parent.homework

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp

@Composable
fun ParentHomeworkListScreen(
    studentId: Int,
    onHomeworkClick: (Int) -> Unit
) {
    val viewModel: ParentHomeworkViewModel = viewModel()
    val list = viewModel.list.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadHomework(studentId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text("Homework", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(list.value.size) { index ->
                val hw = list.value[index]

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onHomeworkClick(hw.id) },
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(hw.subject, fontWeight = FontWeight.Bold, fontSize = 18.sp)

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(hw.title, fontWeight = FontWeight.Medium)

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            hw.description.take(80) + "...",
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(hw.dueDate, color = MaterialTheme.colorScheme.secondary)

                            if (hw.fileUrl != null) {
                                Text(
                                    "Attachment",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
