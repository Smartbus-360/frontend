package com.smartbus360.app.erpadmin.ui.syllabus

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.smartbus360.app.erpadmin.viewmodel.*
import com.smartbus360.app.erpadmin.util.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyllabusProgressScreen(
    viewModel: SyllabusAdminViewModel,
    tokenManager: TokenManager
) {
    val state by viewModel.state.collectAsState()
    val token = "Bearer ${tokenManager.getToken()}"

    LaunchedEffect(Unit) {
        viewModel.loadSyllabusProgress(token)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Syllabus Progress (View Only)") })
        }
    ) { paddingValues ->

        when (state) {

            is SyllabusState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SyllabusState.Error -> {
                Text(
                    text = (state as SyllabusState.Error).message,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            is SyllabusState.Success -> {
                LazyColumn(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    items((state as SyllabusState.Success).list) { item ->
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Column(Modifier.padding(16.dp)) {

                                Text(
                                    "${item.subject} – Class ${item.classId}-${item.sectionId}",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text("Topic: ${item.topic}")
                                Text("Teacher: ${item.teacherName}")
                                Text("Updated: ${item.updatedAt}")

                                Spacer(Modifier.height(6.dp))

                                Text(
                                    text = item.status.uppercase(),
                                    color = if (item.status == "completed")
                                        Color(0xFF2E7D32)
                                    else
                                        Color(0xFFC62828)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
