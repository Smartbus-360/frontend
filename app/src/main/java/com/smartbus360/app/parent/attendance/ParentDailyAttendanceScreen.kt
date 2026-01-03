package com.smartbus360.app.parent.attendance


import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smartbus360.app.parent.attendance.ParentAttendanceViewModel

@Composable
fun ParentDailyAttendanceScreen(studentId: Int) {

    val viewModel: ParentAttendanceViewModel = viewModel()
    val data = viewModel.daily.collectAsState()
    var selectedDate by remember {
        mutableStateOf(java.time.LocalDate.now())
    }

    LaunchedEffect(selectedDate) {
        viewModel.loadDaily(studentId, selectedDate.toString())
    }


    Button(
        onClick = {
            selectedDate = selectedDate.minusDays(1)
        }
    ) {
        Text("◀ Previous Day")
    }

    Text(
        text = selectedDate.toString(),
        fontWeight = FontWeight.Bold
    )

    Button(
        onClick = {
            selectedDate = selectedDate.plusDays(1)
        }
    ) {
        Text("Next Day ▶")
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Text("Daily Attendance", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(20.dp))

        data.value?.records?.forEach { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Text("${item.date}  -  ", fontWeight = FontWeight.Bold)
                    Text(item.status)
                }
            }
        }
    }
}
