package com.smartbus360.app.erpadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.smartbus360.app.erpadmin.ui.dashboard.AdminErpDashboardScreen
import com.smartbus360.app.erpadmin.ui.login.AdminErpLoginScreen
import com.smartbus360.app.erpadmin.util.TokenManager

// ViewModels
import com.smartbus360.app.erpadmin.viewmodel.*
import com.smartbus360.app.erpadmin.ui.teacher.*
import com.smartbus360.app.erpadmin.ui.student.*
import com.smartbus360.app.erpadmin.ui.classsection.*
import com.smartbus360.app.erpadmin.ui.exam.*
import com.smartbus360.app.erpadmin.ui.syllabus.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tokenManager = TokenManager(this)

        setContent {

            val navController = rememberNavController()

            // ✅ CREATE ALL VIEWMODELS HERE
            val authViewModel: AdminAuthViewModel = viewModel()
            val teacherViewModel: TeacherViewModel = viewModel()
            val studentViewModel: StudentViewModel = viewModel()
            val classSectionViewModel: ClassSectionViewModel = viewModel()
            val examAdminViewModel: ExamAdminViewModel = viewModel()
            val syllabusAdminViewModel: SyllabusAdminViewModel = viewModel()

            NavHost(
                navController = navController,
                startDestination = "login"
            ) {

                composable("login") {
                    AdminErpLoginScreen(navController, authViewModel, tokenManager)
                }

                composable("dashboard") {
                    AdminErpDashboardScreen(navController)
                }

                // 👩‍🏫 Teachers
                composable("teachers") {
                    TeacherListScreen(navController, teacherViewModel, tokenManager)
                }
                composable("add_teacher") {
                    AddTeacherScreen(
                        navController = navController,
                        teacherViewModel = teacherViewModel,
                        classSectionViewModel = classSectionViewModel,
                        tokenManager = tokenManager
                    )
                }

                // 👨‍🎓 Students
                composable("students") {
                    StudentListScreen(navController, studentViewModel, tokenManager)
                }
                composable("add_student") {
                    AddStudentScreen(navController, studentViewModel, tokenManager)
                }

                // 🏫 Class & Section
                composable("classes") {
                    ClassListScreen(navController, classSectionViewModel, tokenManager)
                }
                composable("add_class") {
                    AddClassScreen(navController, classSectionViewModel, tokenManager)
                }
                composable("sections/{classId}") {
                    val classId = it.arguments!!.getString("classId")!!.toInt()
                    SectionListScreen(classId, navController, classSectionViewModel, tokenManager)
                }
                composable("add_section/{classId}") {
                    val classId = it.arguments!!.getString("classId")!!.toInt()
                    AddSectionScreen( navController, classSectionViewModel, tokenManager)
                }

                // 📘 Exams (READ ONLY)
//                composable("exams") {
//                    ExamListScreen(navController, examAdminViewModel, tokenManager)
//                }
                composable("exams") {
                    ExamListScreen(
                        navController,
                        examAdminViewModel,
                        classSectionViewModel,
                        tokenManager
                    )
                }

//                composable("exam_results") {
//                    StudentResultScreen(examAdminViewModel, tokenManager)
//                }

                composable("exam_results") {
                    StudentResultScreen(
                        tokenManager,
                        navController,
                        examAdminViewModel,
                        classSectionViewModel
                    )
                }

                // 📚 Syllabus Progress (READ ONLY)
                composable("syllabus_progress") {
                    SyllabusProgressScreen(syllabusAdminViewModel, tokenManager)
                }

                composable("manage_classes") {
                    ManageClassesScreen(navController)
                }

                composable("manage_teachers") {
                    ManageTeacherScreen(
                        navController,
                        teacherViewModel,
                        tokenManager
                    )
                }

            }
        }
    }
}
