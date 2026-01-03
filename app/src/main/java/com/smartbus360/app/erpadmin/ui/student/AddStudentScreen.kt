package com.smartbus360.app.erpadmin.ui.student


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smartbus360.app.erpadmin.data.model.AddStudentRequest
import com.smartbus360.app.erpadmin.viewmodel.StudentViewModel
import com.smartbus360.app.erpadmin.util.TokenManager

@Composable
fun AddStudentScreen(
    navController: NavController,
    viewModel: StudentViewModel,
    tokenManager: TokenManager
) {
    var name by remember { mutableStateOf("") }
    var regNo by remember { mutableStateOf("") }
    var classId by remember { mutableStateOf("") }
    var sectionId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val token = "Bearer ${tokenManager.getToken()}"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Add Student", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(name, { name = it }, label = { Text("Full Name") })
        OutlinedTextField(regNo, { regNo = it }, label = { Text("Registration No") })
        OutlinedTextField(classId, { classId = it }, label = { Text("Class ID") })
        OutlinedTextField(sectionId, { sectionId = it }, label = { Text("Section ID") })
        OutlinedTextField(password, { password = it }, label = { Text("Password") })

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            viewModel.addStudent(
                token,
                AddStudentRequest(
                    name,
                    regNo,
                    classId.toInt(),
                    sectionId.toInt(),
                    password
                )
            ) {
                navController.popBackStack()
            }
        }) {
            Text("Save")
        }
    }
}
