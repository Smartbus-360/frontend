package com.smartbus360.app.erpadmin.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.smartbus360.app.erpadmin.viewmodel.*
import com.smartbus360.app.erpadmin.util.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen(
    navController: NavController,
    viewModel: StudentViewModel,
    tokenManager: TokenManager
) {
    val state by viewModel.state.collectAsState()
    val token = "Bearer ${tokenManager.getToken()}"

    LaunchedEffect(Unit) {
        viewModel.loadStudents(token)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("add_student")
            }) {
                Text("+")
            }
        },
        topBar = {
            TopAppBar(title = { Text("Students") })
        }
    ) { paddingValues ->

        when (state) {

            is StudentState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is StudentState.Error -> {
                Text(
                    text = (state as StudentState.Error).message,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            is StudentState.Success -> {
                LazyColumn(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    items((state as StudentState.Success).students) { student ->
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(
                                    student.full_name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text("Reg: ${student.registrationNumber}")

                                Spacer(Modifier.height(8.dp))

                                Row {
                                    Button(onClick = {
                                        viewModel.generateQr(token, student.id) {
                                            viewModel.loadStudents(token)
                                        }
                                    }) {
                                        Text("Generate QR")
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Button(onClick = {
                                        viewModel.revokeQr(token, student.id) {
                                            viewModel.loadStudents(token)
                                        }
                                    }) {
                                        Text("Revoke QR")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
