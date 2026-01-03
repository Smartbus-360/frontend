package com.smartbus360.app.erpadmin.ui.classsection

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageClassesScreen(navController: NavController) {

    Scaffold(
        topBar = { TopAppBar(title = { Text("Manage Classes") }) }
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
                onClick = { navController.navigate("add_class") }
            ) { Text("Add Class") }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate("classes") }
            ) { Text("Class List") }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate("sections/1") }
            ) { Text("Section List") }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate("add_section/1") }
            ) { Text("Add Section") }
        }
    }
}
