package com.smartbus360.app.parent.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ParentMonthlyAttendanceScreen(studentId: Int) {

    val viewModel: ParentAttendanceViewModel = viewModel()
    val data = viewModel.monthly.collectAsState()

    var selectedMonth by remember { mutableStateOf(java.time.LocalDate.now().monthValue) }
    var selectedYear by remember { mutableStateOf(java.time.LocalDate.now().year) }

    LaunchedEffect(selectedMonth, selectedYear) {
        viewModel.loadMonthly(studentId, selectedMonth, selectedYear)
    }



    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            "Monthly Attendance",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        data.value?.let { monthly ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(onClick = {
                    if (selectedMonth > 1) selectedMonth--
                }) {
                    Text("◀ Month")
                }

                Text(
                    text = "${java.time.Month.of(selectedMonth)} $selectedYear",
                    fontWeight = FontWeight.Bold
                )

                OutlinedButton(onClick = {
                    if (selectedMonth < 12) selectedMonth++
                }) {
                    Text("Month ▶")
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                monthly.records.forEach { record ->

                    item {
                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .background(statusColor(record.status)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(record.date.takeLast(2)) // extract DD
                        }
                    }
                }
            }
        }
    }
}

fun statusColor(status: String): Color {
    return when (status) {
        "P" -> Color(0xFF4CAF50)
        "A" -> Color(0xFFF44336)
        "L" -> Color(0xFFFF9800)
        "E" -> Color(0xFF2196F3)
        else -> Color.LightGray
    }
}
