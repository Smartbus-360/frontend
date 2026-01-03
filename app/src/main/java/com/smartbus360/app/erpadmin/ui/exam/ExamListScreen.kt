package com.smartbus360.app.erpadmin.ui.exam

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smartbus360.app.erpadmin.viewmodel.*
import com.smartbus360.app.erpadmin.util.TokenManager
import androidx.compose.ui.Alignment
import com.smartbus360.app.erpadmin.data.model.SchoolClass
import com.smartbus360.app.erpadmin.data.model.Section
import com.smartbus360.app.erpadmin.viewmodel.ClassState
import com.smartbus360.app.erpadmin.viewmodel.SectionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamListScreen(
    navController: NavController,
    viewModel: ExamAdminViewModel,
    classSectionViewModel: ClassSectionViewModel,
    tokenManager: TokenManager
) {
    val state by viewModel.examState.collectAsState()
    val token = "Bearer ${tokenManager.getToken()}"
    val classState by classSectionViewModel.classState.collectAsState()
    val sectionState by classSectionViewModel.sectionState.collectAsState()

    var selectedClass by remember { mutableStateOf<SchoolClass?>(null) }
    var selectedSection by remember { mutableStateOf<Section?>(null) }


//    LaunchedEffect(Unit) {
//        viewModel.loadExams(token)
//    }
    LaunchedEffect(Unit) {
        classSectionViewModel.loadClasses(token)
    }

    LaunchedEffect(Unit) {
        viewModel.loadExams(token)
    }



    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Exams (View Only)") })
        }
    ) { paddingValues ->

        when (state) {
            is ExamAdminState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ExamAdminState.Error -> {
                Text(
                    text = (state as ExamAdminState.Error).message,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            is ExamAdminState.Success -> {
                LazyColumn(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    items((state as ExamAdminState.Success).exams) { exam ->
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(
                                    exam.examName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text("Class: ${exam.classId} | Section: ${exam.sectionId}")
                                Text("Date: ${exam.date}")

                                Spacer(Modifier.height(8.dp))

                                Button(onClick = {
                                    navController.navigate("exam_results")
                                }) {
                                    Text("View Results")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
