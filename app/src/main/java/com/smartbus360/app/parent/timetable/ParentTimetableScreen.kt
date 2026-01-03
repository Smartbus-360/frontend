package com.smartbus360.app.parent.timetable

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
import com.smartbus360.app.parent.models.TimetablePeriod
import androidx.compose.ui.unit.sp
@Composable
fun ParentTimetableScreen(studentId: Int) {

    val viewModel: ParentTimetableViewModel = viewModel()
    val timetable by viewModel.timetable.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTimetable(studentId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            "Timetable",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        timetable.forEach { (day, periods) ->

            Text(
                text = day,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            if (periods.isEmpty()) {
                Text(
                    "No classes",
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
                )
            } else {
                periods.forEach { period ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(3.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(period.subject, fontWeight = FontWeight.Bold)
                            Text("${period.startTime} - ${period.endTime}")
                            period.teacher?.let {
                                Text("Teacher: $it")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun DaySelector(days: List<String>, selected: String, onDayClick: (String) -> Unit) {

    LazyColumn { }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach { day ->
            Box(
                modifier = Modifier
                    .background(
                        if (day == selected) Color(0xFF1A73E8)
                        else Color(0xFFE0E0E0),
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable { onDayClick(day) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    day.take(3),       // Mon, Tue, Wed etc.
                    color = if (day == selected) Color.White else Color.Black,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun PeriodCard(period: TimetablePeriod) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                period.subject,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )

            Spacer(modifier = Modifier.height(6.dp))

            period.teacher?.let {
                Text("Teacher: $it", color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "${period.startTime} - ${period.endTime}",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
