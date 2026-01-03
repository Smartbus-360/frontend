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
import com.smartbus360.app.teacher.data.model.StudentMarkItem
import com.smartbus360.app.teacher.data.model.SubjectItem
import com.smartbus360.app.data.repository.PreferencesRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherMarksEntryScreen(
    examId: Int,
    examName: String,
    viewModel: ExamViewModel = org.koin.androidx.compose.koinViewModel()
) {
    val context = LocalContext.current
    val token = PreferencesRepository(context).getTeacherToken() ?: return

    // 🔹 Subject state
    var selectedSubject by remember { mutableStateOf<SubjectItem?>(null) }
    var subjectExpanded by remember { mutableStateOf(false) }
    var subjects by remember { mutableStateOf<List<SubjectItem>>(emptyList()) }

    val state by viewModel.state.collectAsState()

    // 🔹 Listen for subjects loaded
    LaunchedEffect(state) {
        if (state is ExamState.SubjectsLoaded) {
            subjects = (state as ExamState.SubjectsLoaded).subjects
            if (subjects.isNotEmpty() && selectedSubject == null) {
                selectedSubject = subjects.first()
            }
        }
    }

    // 🔹 Load teacher subjects
    LaunchedEffect(Unit) {
        viewModel.loadSubjects(token)
    }

    // 🔹 TEMP students (replace with API later)
    val students = remember {
        mutableStateListOf(
            StudentMarkItem(30, "Rahul", 0, 100),
            StudentMarkItem(31, "Ankit", 0, 100),
            StudentMarkItem(32, "Suman", 0, 100)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Enter Marks", style = MaterialTheme.typography.titleLarge)
        Text(
            text = examName.ifBlank { "Exam" },
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // ✅ SUBJECT DROPDOWN (IMPORTANT)
        ExposedDropdownMenuBox(
            expanded = subjectExpanded,
            onExpandedChange = { subjectExpanded = !subjectExpanded }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                value = selectedSubject?.name?.ifBlank { "Select Subject" } ?: "Select Subject",
                onValueChange = {},
                readOnly = true,
                label = { Text("Subject") }
            )

            ExposedDropdownMenu(
                expanded = subjectExpanded,
                onDismissRequest = { subjectExpanded = false }
            ) {
                subjects.forEach { subject ->
                    DropdownMenuItem(
                        text = { Text(subject.name?: "Subject") },
                        onClick = {
                            selectedSubject = subject
                            subjectExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // 🔹 Students list
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(students) { student ->
                MarkRow(student)
            }
        }

        // ✅ SUBMIT BUTTON (subject-aware)
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !selectedSubject?.name.isNullOrBlank(),
            onClick = {
                viewModel.submitMarks(
                    token = token,
                    students = students,
                    examId = examId,
                    subject = selectedSubject?.name ?: return@Button
                )
            }
        ) {
            Text("Submit Marks")
        }
    }
}
