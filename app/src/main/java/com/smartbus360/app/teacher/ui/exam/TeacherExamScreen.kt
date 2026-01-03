package com.smartbus360.app.teacher.ui.exam

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.smartbus360.app.teacher.viewModel.ExamViewModel
import com.smartbus360.app.teacher.viewModel.ExamState
import com.smartbus360.app.data.repository.PreferencesRepository
import com.smartbus360.app.teacher.data.model.StudentMarkItem
import androidx.navigation.NavController
import com.smartbus360.app.teacher.navigation.TeacherRoutes
import com.smartbus360.app.teacher.data.model.ExamItem
import com.smartbus360.app.teacher.data.model.CreateExamRequest


@Composable
fun TeacherExamScreen(
    navController: NavController,
    viewModel: ExamViewModel = org.koin.androidx.compose.koinViewModel()
) {
    val context = LocalContext.current
    val token = PreferencesRepository(context).getTeacherToken() ?: return

    val state by viewModel.state.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var selectedExam by remember { mutableStateOf<ExamItem?>(null) }
    var selectedClassId by remember { mutableStateOf<Int?>(null) }
    var selectedSectionId by remember { mutableStateOf<Int?>(null) }

    var classExpanded by remember { mutableStateOf(false) }
    var sectionExpanded by remember { mutableStateOf(false) }

    var navigateToCircular by remember { mutableStateOf(false) }

    // TEMP: bind dynamically later
//    val teacher = remember {
//        PreferencesRepository(context).getTeacherProfile()
//    }

//    val classId = teacher?.classId ?: return
//    val sectionId = teacher.sectionId ?: return
//


    LaunchedEffect(Unit) {
        viewModel.loadExams(token)
    }
    LaunchedEffect(navigateToCircular) {
        if (navigateToCircular) {
            navController.navigate(TeacherRoutes.CIRCULAR_MESSAGE)
            navigateToCircular = false
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state) {
        when (state) {
            is ExamState.ExamUpdated -> {
                snackbarHostState.showSnackbar("Exam updated successfully")
                viewModel.loadExams(token)
            }

            is ExamState.ExamDeleted -> {
                snackbarHostState.showSnackbar("Exam deleted successfully")
                viewModel.loadExams(token)
            }

            is ExamState.ExamCreated -> {
                snackbarHostState.showSnackbar("Exam created successfully")
                viewModel.loadExams(token)
            }

            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Box(modifier = Modifier.padding(padding)) {

            when (state) {

                is ExamState.Loading -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is ExamState.ExamListLoaded -> {
                    val exams = (state as ExamState.ExamListLoaded).exams

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {


                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { navigateToCircular = true }
                        ) {
                            Text("Circulars & Messages")
                        }

                        Spacer(Modifier.height(8.dp))

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { showCreateDialog = true }
                        ) {
                            Text("Create Exam")
                        }


                        Spacer(Modifier.height(12.dp))

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            items(exams) { exam ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Column(Modifier.padding(12.dp)) {
                                        Text(
                                            exam.examName,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text("Date: ${exam.date}")

                                        Spacer(Modifier.height(8.dp))

//                            Button(
//                                onClick = {
//                                    navController.navigate(
//                                        "${TeacherRoutes.MARKS_ENTRY}/${exam.id}/${exam.examName}"
//                                    )
//                                }
//                            ) {
//                                Text("Enter Marks")
//                            }
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {

                                            // 🔹 ENTER MARKS
                                            Button(
                                                modifier = Modifier.weight(1f),
                                                onClick = {
                                                    navController.navigate(
                                                        "${TeacherRoutes.MARKS_ENTRY}/${exam.id}/${exam.examName}"
                                                    )
                                                }
                                            ) {
                                                Text("Marks")
                                            }

                                            // 🔹 EDIT EXAM (PUT API)
                                            OutlinedButton(
                                                modifier = Modifier.weight(1f),
                                                onClick = {
                                                    selectedExam = exam
                                                    showEditDialog = true
                                                }
                                            ) {
                                                Text("Edit")
                                            }

                                            if (showCreateDialog) {

                                                var examName by remember { mutableStateOf("") }
                                                var classId by remember { mutableStateOf("") }
                                                var sectionId by remember { mutableStateOf("") }
                                                var date by remember { mutableStateOf("") }

                                                AlertDialog(
                                                    onDismissRequest = { showCreateDialog = false },
                                                    title = { Text("Create Exam") },
                                                    text = {
                                                        Column {
                                                            OutlinedTextField(examName, { examName = it }, label = { Text("Exam Name") })
                                                            OutlinedTextField(classId, { classId = it }, label = { Text("Class ID") })
                                                            OutlinedTextField(sectionId, { sectionId = it }, label = { Text("Section ID") })
                                                            OutlinedTextField(date, { date = it }, label = { Text("Date (YYYY-MM-DD)") })
                                                        }
                                                    },
                                                    confirmButton = {
                                                        Button(onClick = {
                                                            viewModel.createExam(
                                                                token,
                                                                CreateExamRequest(
                                                                    examName = examName,
                                                                    classId = classId.toInt(),
                                                                    sectionId = sectionId.toInt(),
                                                                    date = date
                                                                )
                                                            )
                                                            showCreateDialog = false
                                                        }) {
                                                            Text("Create")
                                                        }
                                                    },
                                                    dismissButton = {
                                                        TextButton(onClick = { showCreateDialog = false }) {
                                                            Text("Cancel")
                                                        }
                                                    }
                                                )
                                            }

                                            // 🔹 DELETE EXAM (DELETE API)
                                            OutlinedButton(
                                                modifier = Modifier.weight(1f),
                                                colors = ButtonDefaults.outlinedButtonColors(
                                                    contentColor = MaterialTheme.colorScheme.error
                                                ),
                                                onClick = {
                                                    viewModel.deleteExam(token, exam.id)
                                                }
                                            ) {
                                                Text("Delete")
                                            }

                                            Button(
                                                modifier = Modifier.weight(1f),
                                                onClick = {
                                                    // TEMP studentId – replace with real student selector later
                                                    navController.navigate(
                                                        "${TeacherRoutes.RESULTS}/30"
                                                    )
                                                }
                                            ) {
                                                Text("View Result")
                                            }

                                        }

                                    }
                                }
                            }
                        }
                    }
                }

                is ExamState.MarksSubmitted -> {
                    Text("Marks submitted successfully")
                }

                is ExamState.Error -> {
                    Text(
                        (state as ExamState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> {
                }
            }
        }
        if (showEditDialog && selectedExam != null) {

            var examName by remember { mutableStateOf(selectedExam!!.examName) }
            var examDate by remember { mutableStateOf(selectedExam!!.date) }

            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text("Edit Exam") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = examName,
                            onValueChange = { examName = it },
                            label = { Text("Exam Name") }
                        )

                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = examDate,
                            onValueChange = { examDate = it },
                            label = { Text("Date (YYYY-MM-DD)") }
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        viewModel.updateExam(
                            token = token,
                            examId = selectedExam!!.id,
                            examName = examName,
                            date = examDate
                        )
                        showEditDialog = false
                    }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEditDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

    }

}

