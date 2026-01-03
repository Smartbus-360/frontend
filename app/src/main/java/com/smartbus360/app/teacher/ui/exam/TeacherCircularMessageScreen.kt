package com.smartbus360.app.teacher.ui.exam


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.smartbus360.app.teacher.viewModel.ExamViewModel
import com.smartbus360.app.teacher.viewModel.ExamState
import com.smartbus360.app.data.repository.PreferencesRepository

@Composable
fun TeacherCircularMessageScreen(
    viewModel: ExamViewModel = org.koin.androidx.compose.koinViewModel()
) {
    val context = LocalContext.current
    val token = PreferencesRepository(context).getTeacherToken() ?: return
    val state by viewModel.state.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    var message by remember { mutableStateOf("") }

    // TEMP – bind from teacher profile later
    val teacherProfile = remember {
        PreferencesRepository(context).getTeacherProfile()
    }

    val classId = teacherProfile?.classId
    val sectionId = teacherProfile?.sectionId


    LaunchedEffect(selectedTab) {
        if (selectedTab == 0) {
            viewModel.loadCirculars(token)
        }
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()   // 🔥 THIS IS THE KEY
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {


            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Circulars") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Message") }
                )
            }

            when (selectedTab) {

                // ---------------- CIRCULARS ----------------
                0 -> {
                    when (state) {
                        is ExamState.CircularsLoaded -> {
                            val circulars =
                                (state as ExamState.CircularsLoaded).circulars

                            if (circulars.isEmpty()) {
                                Box(
                                    Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No circulars available")
                                }
                            } else {
                                LazyColumn(Modifier.padding(16.dp)) {
                                    items(circulars) { c ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp)
                                        ) {
                                            Column(Modifier.padding(12.dp)) {
                                                Text(
                                                    c.title ?: "Circular",
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                                Spacer(Modifier.height(4.dp))
                                                Text(c.message ?: "")
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        is ExamState.Loading -> {
                            Box(
                                Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        else -> {}
                    }
                }

                // ---------------- MESSAGE ----------------
                1 -> {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {

                        OutlinedTextField(
                            value = message,
                            onValueChange = { message = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Message") },
                            minLines = 4
                        )

                        Spacer(Modifier.height(12.dp))

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            enabled = message.isNotBlank() && classId != null && sectionId != null,
                            onClick = {
                                viewModel.sendBroadcast(
                                    token = token,
                                    classId = classId!!,
                                    sectionId = sectionId!!,
                                    message = message
                                )
                            }
                        ) {
                            Text("Send Message")
                        }


                        if (state is ExamState.MessageSent) {
                            Text(
                                "Message sent successfully",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}