package com.smartbus360.app.erpadmin.ui.classsection


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassListScreen(
    navController: NavController,
    viewModel: ClassSectionViewModel,
    tokenManager: TokenManager
) {
    val state by viewModel.classState.collectAsState()
    val token = "Bearer ${tokenManager.getToken()}"

    LaunchedEffect(Unit) {
        viewModel.loadClasses(token)
    }
//    Text(cls.className)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_class") }) {
                Text("+")
            }
        },
        topBar = { TopAppBar(title = { Text("Classes") }) }
//    ) { padding ->
//        when (state) {
//            is ClassState.Loading -> CircularProgressIndicator()
//            is ClassState.Error -> Text((state as ClassState.Error).message)
//            is ClassState.Success -> {
//                LazyColumn(modifier = Modifier.padding(padding)) {
//                    items((state as ClassState.Success).classes) { cls ->
//                        Card(
//                            modifier = Modifier
//                                .padding(8.dp)
//                                .fillMaxWidth()
//                        ) {
//                            Row(
//                                modifier = Modifier.padding(16.dp),
//                                horizontalArrangement = Arrangement.SpaceBetween
//                            ) {
//                                Text(cls.name)
//                                Row {
//                                    Button(onClick = {
//                                        navController.navigate("sections/${cls.id}")
//                                    }) {
//                                        Text("Sections")
//                                    }
//                                    Spacer(Modifier.width(8.dp))
//                                    IconButton(onClick = {
//                                        viewModel.deleteClass(token, cls.id) {
//                                            viewModel.loadClasses(token)
//                                        }
//                                    }) {
//                                        Text("🗑")
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {

            when (state) {

                is ClassState.Loading -> {
                    CircularProgressIndicator()
                }

                is ClassState.Error -> {
                    Text(
                        text = (state as ClassState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is ClassState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items((state as ClassState.Success).classes) { cls ->
                            Card(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(cls.className)

                                    Row {
                                        Button(onClick = {
                                            navController.navigate("sections/${cls.id}")
                                        }) {
                                            Text("Sections")
                                        }

                                        Spacer(Modifier.width(8.dp))

                                        IconButton(onClick = {
                                            viewModel.deleteClass(token, cls.id) {
                                                viewModel.loadClasses(token)
                                            }
                                        }) {
                                            Text("🗑")
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
}
