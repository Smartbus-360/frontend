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
fun SectionListScreen(
    classId: Int,
    navController: NavController,
    viewModel: ClassSectionViewModel,
    tokenManager: TokenManager
) {
    val state by viewModel.sectionState.collectAsState()
    val token = "Bearer ${tokenManager.getToken()}"

    LaunchedEffect(classId) {
        viewModel.loadSections(token, classId)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("add_section/$classId")
            }) { Text("+") }
        },
        topBar = { TopAppBar(title = { Text("Sections") }) }
//    ) { padding ->
//        when (state) {
//            is SectionState.Loading -> CircularProgressIndicator()
//            is SectionState.Error -> Text((state as SectionState.Error).message)
//            is SectionState.Success -> {
//                LazyColumn(modifier = Modifier.padding(padding)) {
//                    items((state as SectionState.Success).sections) { sec ->
//                        Card(
//                            modifier = Modifier
//                                .padding(8.dp)
//                                .fillMaxWidth()
//                        ) {
//                            Row(
//                                modifier = Modifier.padding(16.dp),
//                                horizontalArrangement = Arrangement.SpaceBetween
//                            ) {
//                                Text(sec.name)
//                                IconButton(onClick = {
//                                    viewModel.deleteSection(token, sec.id) {
//                                        viewModel.loadSections(token, classId)
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

    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {

            when (state) {

                is SectionState.Loading -> {
                    CircularProgressIndicator()
                }

                is SectionState.Error -> {
                    Text(
                        text = (state as SectionState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is SectionState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items((state as SectionState.Success).sections) { sec ->
                            Card(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(sec.sectionName ?: "")

                                    IconButton(onClick = {
                                        viewModel.deleteSection(token, sec.id) {
                                            viewModel.loadSections(token, classId)
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