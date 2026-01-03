package com.smartbus360.app.erpadmin.ui.teacher

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
//@Composable
//fun TeacherListScreen(
//    navController: NavController,
//    viewModel: TeacherViewModel,
//    tokenManager: TokenManager
//) {
//    val state by viewModel.teacherState.collectAsState()
//    val token = "Bearer ${tokenManager.getToken()}"
//
//    LaunchedEffect(Unit) {
//        viewModel.loadTeachers(token)
//    }
//
//    Scaffold(
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = { navController.navigate("add_teacher") }
//            ) {
//                Text("+")
//            }
//        },
//        topBar = {
//            TopAppBar(title = { Text("Teachers") })
//        }
//    ) { paddingValues ->
//
//        when (state) {
//
//            is TeacherState.Loading -> {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator()
//                }
//            }
//
//            is TeacherState.Error -> {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = (state as TeacherState.Error).message,
//                        color = MaterialTheme.colorScheme.error
//                    )
//                }
//
//            }
//
//            is TeacherState.Success -> {
//                LazyColumn(
//                    modifier = Modifier.padding(paddingValues)
//                ) {
//                    items((state as TeacherState.Success).teachers) { teacher ->
//                        Card(
//                            modifier = Modifier
//                                .padding(8.dp)
//                                .fillMaxWidth()
//                        ) {
//                            Row(
//                                modifier = Modifier.padding(16.dp),
//                                horizontalArrangement = Arrangement.SpaceBetween
//                            ) {
//                                Column {
//                                    Text(
//                                        teacher.full_name,
//                                        style = MaterialTheme.typography.titleMedium
//                                    )
//                                    Text(teacher.email)
//                                }
//                                IconButton(onClick = {
//                                    viewModel.deleteTeacher(token, teacher.id) {
//                                        viewModel.loadTeachers(token)
//                                    }
//                                }) {
//                                    Text("🗑")
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}

@Composable
fun TeacherListScreen(
    navController: NavController,
    viewModel: TeacherViewModel,
    tokenManager: TokenManager
) {
    val state by viewModel.teacherState.collectAsState()
    val token = "Bearer ${tokenManager.getToken()}"

    LaunchedEffect(Unit) {
        viewModel.loadTeachers(token)
    }

    when (state) {

        is TeacherState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is TeacherState.Error -> {
            Text(
                text = (state as TeacherState.Error).message,
                modifier = Modifier.padding(16.dp)
            )
        }

        is TeacherState.Success -> {
            LazyColumn {
                items((state as TeacherState.Success).teachers) { teacher ->
                    Card(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    teacher.full_name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    teacher.email,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            IconButton(
                                onClick = {
                                    viewModel.deleteTeacher(token, teacher.id) {
                                        viewModel.loadTeachers(token)
                                    }
                                }
                            ) {
                                Text("🗑")
                            }
                        }
                    }
                }
            }
        }
    }
}
