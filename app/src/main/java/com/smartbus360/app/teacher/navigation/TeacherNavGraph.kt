package com.smartbus360.app.teacher.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.smartbus360.app.teacher.ui.auth.TeacherLoginScreen
import com.smartbus360.app.teacher.ui.dashboard.TeacherDashboardScreen
import com.smartbus360.app.teacher.ui.attendance.TeacherAttendanceScreen
import com.smartbus360.app.teacher.ui.homework.TeacherHomeworkScreen
import com.smartbus360.app.teacher.ui.exam.TeacherExamScreen
import com.smartbus360.app.teacher.ui.syllabus.TeacherSyllabusScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.smartbus360.app.teacher.ui.exam.TeacherMarksEntryScreen
import androidx.navigation.navArgument
import com.smartbus360.app.teacher.ui.exam.TeacherResultScreen
import com.smartbus360.app.teacher.ui.exam.TeacherCircularMessageScreen

@Composable
fun TeacherNavGraph() {

    // ✅ Teacher module owns its own NavController
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = TeacherRoutes.LOGIN
    ) {

        composable(TeacherRoutes.LOGIN) {
            TeacherLoginScreen(navController)
        }

        composable(TeacherRoutes.DASHBOARD) {
            TeacherDashboardScreen(navController)
        }

        composable(TeacherRoutes.ATTENDANCE) {
            TeacherAttendanceScreen()
        }

        composable(TeacherRoutes.HOMEWORK) {
            TeacherHomeworkScreen()
        }

        composable(TeacherRoutes.EXAMS) {
            TeacherExamScreen(navController)
        }

        composable(TeacherRoutes.SYLLABUS) {
            TeacherSyllabusScreen()
        }

        composable(
            route = "${TeacherRoutes.MARKS_ENTRY}/{examId}/{examName}",
            arguments = listOf(
                navArgument("examId") { type = NavType.IntType },
                navArgument("examName") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val examId = backStackEntry.arguments?.getInt("examId") ?: return@composable
            val examName = backStackEntry.arguments?.getString("examName") ?: ""

            TeacherMarksEntryScreen(
                examId = examId,
                examName = examName
            )
        }

        composable(
            route = "${TeacherRoutes.RESULTS}/{studentId}",
            arguments = listOf(
                navArgument("studentId") { type = NavType.IntType }
            )
        ) { backStackEntry ->

            val studentId = backStackEntry.arguments?.getInt("studentId") ?: return@composable

            TeacherResultScreen(studentId = studentId)
        }


        composable(TeacherRoutes.CIRCULAR_MESSAGE) {
            TeacherCircularMessageScreen()
        }

    }

}
