package com.smartbus360.app.teacher.ui.syllabus

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.smartbus360.app.teacher.viewModel.SyllabusViewModel
import com.smartbus360.app.teacher.viewModel.SyllabusState
import com.smartbus360.app.data.repository.PreferencesRepository
import com.smartbus360.app.teacher.data.model.AddSyllabusProgressRequest

@Composable
fun TeacherSyllabusScreen(
    viewModel: SyllabusViewModel = org.koin.androidx.compose.koinViewModel()
) {
    val context = LocalContext.current
    val token = PreferencesRepository(context).getTeacherToken() ?: return

    val state by viewModel.state.collectAsState()

    // TEMP: bind dynamically later
    val teacher = remember {
        PreferencesRepository(context).getTeacherProfile()
    }

    val classId = teacher?.classId ?: return
    val sectionId = teacher.sectionId ?: return
    val subjectId = 5


    LaunchedEffect(Unit) {
        viewModel.loadSyllabus(token, classId, sectionId, subjectId)
    }

    when (state) {

        is SyllabusState.Loading -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is SyllabusState.Loaded -> {
            val chapters = (state as SyllabusState.Loaded).chapters

            LazyColumn(Modifier.padding(16.dp)) {
                items(chapters) { chapter ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(chapter.chapterName, style = MaterialTheme.typography.titleMedium)
                            Text("Status: ${chapter.status}")

                            Spacer(Modifier.height(8.dp))

                            Row {
                                Button(
                                    onClick = {
                                        viewModel.updateChapter(
                                            token,
                                            AddSyllabusProgressRequest(
                                                classId,
                                                sectionId,
                                                subjectId,
                                                chapter.chapterName,
                                                "covered",
                                                "Completed"
                                            )
                                        )
                                    }
                                ) {
                                    Text("Mark Covered")
                                }

                                Spacer(Modifier.width(8.dp))

                                Button(
                                    onClick = {
                                        viewModel.updateChapter(
                                            token,
                                            AddSyllabusProgressRequest(
                                                classId,
                                                sectionId,
                                                subjectId,
                                                chapter.chapterName,
                                                "pending",
                                                "Pending"
                                            )
                                        )
                                    }
                                ) {
                                    Text("Mark Pending")
                                }
                            }
                        }
                    }
                }
            }
        }

        is SyllabusState.Updated -> {
            Text("Syllabus updated successfully")
        }

        is SyllabusState.Error -> {
            Text(
                (state as SyllabusState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
