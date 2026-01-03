package com.smartbus360.app.erpadmin.ui.teacher

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smartbus360.app.erpadmin.viewmodel.TeacherViewModel
import com.smartbus360.app.erpadmin.util.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageTeacherScreen(
    navController: NavController,
    viewModel: TeacherViewModel,
    tokenManager: TokenManager
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Teachers") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_teacher") }
            ) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // 🔍 Search (UI only for now)
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search teacher...") }
            )

            // 📋 Teacher list
            TeacherListScreen(
                navController = navController,
                viewModel = viewModel,
                tokenManager = tokenManager
            )
        }
    }
}
