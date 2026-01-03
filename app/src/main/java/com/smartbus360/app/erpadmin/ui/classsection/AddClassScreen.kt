package com.smartbus360.app.erpadmin.ui.classsection


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smartbus360.app.erpadmin.viewmodel.ClassSectionViewModel
import com.smartbus360.app.erpadmin.util.TokenManager

@Composable
fun AddClassScreen(
    navController: NavController,
    viewModel: ClassSectionViewModel,
    tokenManager: TokenManager
) {
    var name by remember { mutableStateOf("") }
    val token = "Bearer ${tokenManager.getToken()}"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Add Class", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(name, { name = it }, label = { Text("Class Name") })

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            viewModel.addClass(token, name) {
                navController.popBackStack()
            }
        }) {
            Text("Save")
        }
    }
}
