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

@Composable
fun TeacherResultScreen(
    studentId: Int,
    viewModel: ExamViewModel = org.koin.androidx.compose.koinViewModel()
) {
    val context = LocalContext.current
    val token = PreferencesRepository(context).getTeacherToken() ?: return
    val state by viewModel.state.collectAsState()

    LaunchedEffect(studentId) {
        viewModel.loadResults(token, studentId)
    }

    when (state) {

        is ExamState.Loading -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is ExamState.ResultsLoaded -> {
            val results = (state as ExamState.ResultsLoaded).results

            LazyColumn(Modifier.padding(16.dp)) {
                items(results) { result ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text("Subject: ${result.subject}")
                            Text("Marks: ${result.marksObtained} / ${result.maxMarks}")
                        }
                    }
                }
            }
        }

        is ExamState.Error -> {
            Text(
                (state as ExamState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
        }

        else -> {}
    }
}
