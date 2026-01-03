package com.smartbus360.app.parent.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smartbus360.app.parent.models.DashboardResponse
import androidx.compose.foundation.shape.RoundedCornerShape
import org.koin.androidx.compose.koinViewModel

@Composable
fun ParentDashboardScreen(
    studentId: Int,
    onNavigateAttendance: () -> Unit,
    onNavigateHomework: () -> Unit,
    onNavigateFees: () -> Unit,
    onNavigateExams: () -> Unit,
    onNavigateCirculars: () -> Unit,
    onNavigateMessages: () -> Unit,
    onNavigateTimetable: () -> Unit,
) {
//    val viewModel: ParentDashboardViewModel = viewModel()
    val viewModel: ParentDashboardViewModel = koinViewModel()
    val state = viewModel.dashboardState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDashboard(studentId)
    }
//    LaunchedEffect(studentId) {
//        viewModel.loadDashboard(studentId)
//    }


    when (val uiState = state.value) {
        is DashboardUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        }
        is DashboardUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { Text(uiState.message, color = Color.Red) }
        }
        is DashboardUiState.Success -> {
            DashboardContent(
                data = uiState.data,
                onNavigateAttendance,
                onNavigateHomework,
                onNavigateFees,
                onNavigateExams,
                onNavigateCirculars,
                onNavigateMessages,
                onNavigateTimetable
            )
        }
    }
}

@Composable
fun DashboardContent(
    data: DashboardResponse,
    onAttendance: () -> Unit,
    onHomework: () -> Unit,
    onFees: () -> Unit,
    onExams: () -> Unit,
    onCirculars: () -> Unit,
    onMessages: () -> Unit,
    onTimetable: () -> Unit
) {
    // ✅ Compute values from curl response
    val attendancePercent = if (data.attendance.isNotEmpty()) {
        val present = data.attendance.count { it.status == "P" }
        (present * 100) / data.attendance.size
    } else 0

    val homeworkCount = data.homework.size

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Student Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(Color(0xFF1A73E8)),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = data.student.full_name,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Class ${data.student.classId}",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Summary Row (Attendance, Homework, Fees)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

//            SummaryCard(title = "Attendance", value = data.attendancePercentage + "%") {
            SummaryCard(title = "Attendance", value = "$attendancePercent%") {
            onAttendance()
            }

            SummaryCard(title = "Homework", value = homeworkCount.toString()) {
                onHomework()
            }

//            SummaryCard(title = "Fees Due", value = "₹${data.feesDue}") {
//                onFees()
//            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        // Navigation Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxHeight()
        ) {
            item {
                GridItem("Exams") { onExams() }
            }
            item {
                GridItem("Circulars") { onCirculars() }
            }
            item {
                GridItem("Messages") { onMessages() }
            }
            item {
                GridItem("Timetable") { onTimetable() }
            }
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(width = 110.dp, height = 110.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
    }
}

@Composable
fun GridItem(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
