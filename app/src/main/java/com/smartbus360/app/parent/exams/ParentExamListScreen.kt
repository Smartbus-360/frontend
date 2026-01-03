package com.smartbus360.app.parent.exams

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
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background

@Composable
fun ParentExamListScreen(
    studentId: Int,
    onExamClick: (Int) -> Unit
) {
    val viewModel: ParentExamViewModel = viewModel()
    val exams = viewModel.examList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadExams(studentId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text("Exams", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(exams.value.size) { index ->
                val exam = exams.value[index]

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onExamClick(exam.id) },
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            exam.examName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Date: ${exam.date}")

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Class ${exam.classId} • Section ${exam.sectionId}",
                            color = MaterialTheme.colorScheme.secondary
                        )

//                        Spacer(modifier = Modifier.height(6.dp))
//                        Text("Subjects: ${exam.subjects}")

//                        Spacer(modifier = Modifier.height(10.dp))

//                        Box(
//                            modifier = Modifier
//                                .background(
//                                    if (exam.status == "Completed") Color(0xFF4CAF50)
//                                    else Color(0xFFFF9800),
//                                    shape = MaterialTheme.shapes.small
//                                )
//                                .padding(horizontal = 10.dp, vertical = 4.dp)
//                        ) {
//                            Text(
//                                exam.status,
//                                color = Color.White,
//                                fontWeight = FontWeight.SemiBold
//                            )
//                        }
                    }
                }
            }
        }
    }
}
