package com.smartbus360.app.parent.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.smartbus360.app.parent.dashboard.ParentDashboardScreen
import com.smartbus360.app.parent.attendance.ParentAttendanceScreen
import com.smartbus360.app.parent.homework.ParentHomeworkListScreen
import com.smartbus360.app.parent.homework.ParentHomeworkDetailsScreen
import com.smartbus360.app.parent.exams.ParentExamListScreen
import com.smartbus360.app.parent.exams.ParentExamResultsScreen
import com.smartbus360.app.parent.fees.ParentFeesScreen
import com.smartbus360.app.parent.messages.ParentMessageListScreen
import com.smartbus360.app.parent.messages.ParentMessageDetailsScreen
import com.smartbus360.app.parent.timetable.ParentTimetableScreen

@Composable
fun ParentNavGraph(studentId: Int) {

    val parentNavController = rememberNavController()

    NavHost(
        navController = parentNavController,
        startDestination = "dashboard"
    ) {

        composable("dashboard") {
            ParentDashboardScreen(
                studentId = studentId,
                onNavigateAttendance = {
                    parentNavController.navigate("attendance")
                },
                onNavigateHomework = {
                    parentNavController.navigate("homework")
                },
                onNavigateFees = {
                    parentNavController.navigate("fees")
                },
                onNavigateExams = {
                    parentNavController.navigate("exams")
                },
                onNavigateCirculars = {
                    parentNavController.navigate("messages")
                },
                onNavigateMessages = {
                    parentNavController.navigate("messages")
                },
                onNavigateTimetable = {
                    parentNavController.navigate("timetable")
                }
            )
        }

        composable("attendance") {
            ParentAttendanceScreen(
                onMonthly = {},
                onDaily = {},
                onSummary = {}
            )
        }

        composable("homework") {
            ParentHomeworkListScreen(
                studentId = studentId,
                onHomeworkClick = { hwId ->
                    parentNavController.navigate("homework_detail/$hwId")
                }
            )
        }

        composable(
            "homework_detail/{homeworkId}",
            arguments = listOf(navArgument("homeworkId") { type = NavType.IntType })
        ) {
            ParentHomeworkDetailsScreen(
                homeworkId = it.arguments!!.getInt("homeworkId")
            )
        }

        composable("exams") {
            ParentExamListScreen(
                studentId = studentId,
                onExamClick = { examId ->
                    parentNavController.navigate("exam_results/$examId")
                }
            )
        }

        composable(
            "exam_results/{examId}",
            arguments = listOf(navArgument("examId") { type = NavType.IntType })
        ) {
            ParentExamResultsScreen(studentId)
        }

        composable("fees") {
            ParentFeesScreen(studentId)
        }

        composable("messages") {
            ParentMessageListScreen(
                studentId = studentId,
                onMessageClick = { /* TODO */ }
            )
        }

        composable("timetable") {
            ParentTimetableScreen(studentId)
        }
    }
}
