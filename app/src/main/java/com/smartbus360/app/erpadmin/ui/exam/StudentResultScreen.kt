package com.smartbus360.app.erpadmin.ui.exam


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smartbus360.app.erpadmin.viewmodel.*
import com.smartbus360.app.erpadmin.util.TokenManager
import com.smartbus360.app.erpadmin.data.model.SchoolClass
import com.smartbus360.app.erpadmin.data.model.Section
import com.smartbus360.app.erpadmin.viewmodel.ClassState
import com.smartbus360.app.erpadmin.viewmodel.SectionState
import androidx.navigation.NavController

@Composable
fun StudentResultScreen(
    tokenManager: TokenManager,
    navController: NavController,
    viewModel: ExamAdminViewModel,
    classSectionViewModel: ClassSectionViewModel,

) {
    var studentId by remember { mutableStateOf("") }
    val state by viewModel.resultState.collectAsState()
    val token = "Bearer ${tokenManager.getToken()}"

    val classState by classSectionViewModel.classState.collectAsState()
    val sectionState by classSectionViewModel.sectionState.collectAsState()

    var selectedClass by remember { mutableStateOf<SchoolClass?>(null) }
    var selectedSection by remember { mutableStateOf<Section?>(null) }

    Column(Modifier.padding(24.dp)) {
        Text("Student Results", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = studentId,
            onValueChange = { studentId = it },
            label = { Text("Student ID") }
        )

        LaunchedEffect(Unit) {
            classSectionViewModel.loadClasses(token)
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = {
//            viewModel.loadStudentResults(token, studentId.toInt())
        }) {
            Text("Fetch Results")
        }

        LaunchedEffect(selectedClass) {
            selectedClass?.let {
                classSectionViewModel.loadSections(token, it.id)
            }
        }


        Spacer(Modifier.height(16.dp))

        when (state) {
            is ResultState.Loading -> CircularProgressIndicator()
            is ResultState.Error -> Text((state as ResultState.Error).message)
            is ResultState.Success -> {
                LazyColumn {
                    items((state as ResultState.Success).results) { res ->
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(res.subject, style = MaterialTheme.typography.titleMedium)
                                Text("Marks: ${res.marks}/${res.totalMarks}")
                                Text("Grade: ${res.grade}")
                            }
                        }
                    }
                }
            }
        }
    }
}
