//package com.smartbus360.app.erpadmin.ui.dashboard
//
//
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.Box
//import androidx.compose.ui.Alignment
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AdminErpDashboardScreen() {
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Admin Dashboard") }
//            )
//        }
//    ) { paddingValues ->
//
//        Box(
//            modifier = Modifier.padding(paddingValues),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(text = "Welcome Admin")
//        }
//    }
//}


package com.smartbus360.app.erpadmin.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminErpDashboardScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Admin Dashboard") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate("manage_classes") }
            ) {
                Text("Manage Classes")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate("manage_teachers") }
            ) {
                Text("Manage Teachers")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate("students") }
            ) {
                Text("Manage Students")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate("exams") }
            ) {
                Text("View Exams")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate("syllabus_progress") }
            ) {
                Text("Syllabus Progress")
            }
        }
    }
}
