package com.smartbus360.app.teacher.ui.dashboard


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smartbus360.app.teacher.viewModel.TeacherDashboardViewModel
import com.smartbus360.app.teacher.viewModel.TeacherDashboardState
import com.smartbus360.app.data.repository.PreferencesRepository
import androidx.compose.ui.platform.LocalContext
import com.smartbus360.app.teacher.navigation.TeacherRoutes

@Composable
fun TeacherDashboardScreen(
    navController: NavController,
    viewModel: TeacherDashboardViewModel = org.koin.androidx.compose.koinViewModel()
) {
    val context = LocalContext.current
    val token = PreferencesRepository(context).getTeacherToken()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        token?.let { viewModel.loadDashboard(it) }
    }

    when (state) {

        is TeacherDashboardState.Loading -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is TeacherDashboardState.Error -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text((state as TeacherDashboardState.Error).message)
            }
        }

        is TeacherDashboardState.Success -> {
            val data = (state as TeacherDashboardState.Success).data
            val teacher = data.teacher
            val dashboard = data.dashboard

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                /* ---------------- HEADER ---------------- */
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                    Column {
                        Text(
                            text = "Hello ${teacher.full_name} 👋",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = "Teacher Dashboard",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    TextButton(
                        onClick = {
                            PreferencesRepository(context).clearTeacherSession()

                            navController.navigate(TeacherRoutes.LOGIN) {
                                popUpTo(TeacherRoutes.DASHBOARD) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                            ) {
                        Text(
                            "Logout",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                }

                /* ---------------- PROFILE CARD ---------------- */
                item {
                    Card {
                        Column(Modifier.padding(16.dp)) {
                            Text("Profile", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))

                            Text("Username: ${teacher.username ?: "-"}")
                            Text("Email: ${teacher.email ?: "-"}")
                            Text("Phone: ${teacher.phone ?: "-"}")
                            Text("Class: ${teacher.classId ?: "-"} | Section: ${teacher.sectionId ?: "-"}")
                        }
                    }
                }

                /* ---------------- QUICK STATS ---------------- */
                item {
                    Text("Overview", style = MaterialTheme.typography.titleMedium)
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard("Students", dashboard.totalStudents.toString())
                        StatCard("Messages", dashboard.unreadMessages.toString())
                    }
                }

                /* ---------------- QUICK ACTIONS ---------------- */
                item {
                    Text("Quick Actions", style = MaterialTheme.typography.titleMedium)
                }

                item {
                    DashboardActionCard("Mark Attendance") {
                        navController.navigate(TeacherRoutes.ATTENDANCE)
                    }
                }

                item {
                    DashboardActionCard("Homework / Assignment") {
                        navController.navigate(TeacherRoutes.HOMEWORK)
                    }
                }

                item {
                    DashboardActionCard("Exams & Marks") {
                        navController.navigate(TeacherRoutes.EXAMS)
                    }
                }

                item {
                    DashboardActionCard("Syllabus Progress") {
                        navController.navigate(TeacherRoutes.SYLLABUS)
                    }
                }

                /* ---------------- ATTENDANCE SUMMARY ---------------- */
                item {
                    Card {
                        Column(Modifier.padding(16.dp)) {
                            Text("Attendance Summary", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))
                            Text("Present: ${dashboard.attendanceSummary.present}")
                            Text("Absent: ${dashboard.attendanceSummary.absent}")
                            Text("Late: ${dashboard.attendanceSummary.late}")
                        }
                    }
                }

                /* ---------------- TODAY'S CLASSES ---------------- */
                if (dashboard.todaysPeriods.isNotEmpty()) {
                    item {
                        Text("Today's Classes", style = MaterialTheme.typography.titleMedium)
                    }

                    items(dashboard.todaysPeriods) { period ->
                        Card {
                            Column(Modifier.padding(12.dp)) {
                                Text("Class ${period.classId} | Section ${period.sectionId}")
                                Text("Time: ${period.startTime} - ${period.endTime}")
                            }
                        }
                    }
                }

                /* ---------------- UPCOMING EXAMS ---------------- */
                if (dashboard.upcomingExams.isNotEmpty()) {
                    item {
                        Text("Upcoming Exams", style = MaterialTheme.typography.titleMedium)
                    }

                    items(dashboard.upcomingExams) { exam ->
                        Card {
                            Column(Modifier.padding(12.dp)) {
                                Text(exam.examName, style = MaterialTheme.typography.titleSmall)
                                Text("Date: ${exam.date}")
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
private fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.headlineSmall)
            Text(title, style = MaterialTheme.typography.bodySmall)
        }
    }
}


@Composable
private fun DashboardActionCard(
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
        }
    }
}
