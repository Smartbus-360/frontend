package com.smartbus360.app.parent.attendance


import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smartbus360.app.parent.attendance.ParentAttendanceViewModel

@Composable
fun ParentAttendanceSummaryScreen(studentId: Int) {

    val viewModel: ParentAttendanceViewModel = viewModel()
    val monthly = viewModel.monthly.collectAsState()


    LaunchedEffect(Unit) {
        val month = java.time.LocalDate.now().monthValue
        val year = java.time.LocalDate.now().year
        viewModel.loadMonthly(studentId, month, year)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Attendance Summary", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(20.dp))

        monthly.value?.let { m ->
            val summary = viewModel.computeSummary(m)

            SummaryRow("Total Present", summary.totalPresent)
            SummaryRow("Total Absent", summary.totalAbsent)
            SummaryRow("Total Late", summary.totalLate)
            SummaryRow("Total Excused", summary.totalExcused)

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Attendance: ${summary.percentage.toInt()}%",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

    }
}

@Composable
fun SummaryRow(label: String, value: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(value.toString(), fontWeight = FontWeight.Bold)
    }
}
