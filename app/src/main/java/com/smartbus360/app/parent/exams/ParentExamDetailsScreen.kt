package com.smartbus360.app.parent.exams

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background

@Composable
fun ParentExamResultsScreen(studentId: Int) {

    val viewModel: ParentExamViewModel = viewModel()
    val results by viewModel.results.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadResults(studentId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Exam Results", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        if (results.isEmpty()) {
            Text("No results published yet")
        } else {
            results.forEach { item ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            item.subject,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text("Max Marks: ${item.maxMarks}")
                        Text("Obtained: ${item.obtainedMarks}")
                        Text("Grade: ${item.grade}")
                    }
                }
            }
        }
    }
}
