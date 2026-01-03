package com.smartbus360.app.erpadmin.ui.classsection


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smartbus360.app.erpadmin.viewmodel.ClassSectionViewModel
import com.smartbus360.app.erpadmin.util.TokenManager
import com.smartbus360.app.erpadmin.data.model.SchoolClass
import com.smartbus360.app.erpadmin.viewmodel.ClassState

//@Composable
//fun AddSectionScreen(
//    classId: Int,
//    navController: NavController,
//    viewModel: ClassSectionViewModel,
//    tokenManager: TokenManager
//) {
//    var name by remember { mutableStateOf("") }
//    val token = "Bearer ${tokenManager.getToken()}"
//    val instituteId = tokenManager.getInstituteId() // or hardcode 7 for now
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp)
//    ) {
//        Text("Add Section", style = MaterialTheme.typography.headlineSmall)
//
//        OutlinedTextField(name, { name = it }, label = { Text("Section Name") })
//
//        Spacer(Modifier.height(16.dp))
//
//        Button(onClick = {
//
//            viewModel.addSection(
//                token = token,
//                sectionName = name,
//                classId = classId,
//                instituteId = instituteId // later fetch from preferences
//            ) {
//                navController.popBackStack()
//            }
//
//        }) {
//            Text("Save")
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSectionScreen(
    navController: NavController,
    viewModel: ClassSectionViewModel,
    tokenManager: TokenManager
) {
    val token = "Bearer ${tokenManager.getToken()}"
    val instituteId = tokenManager.getInstituteId()

    var sectionName by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedClass by remember { mutableStateOf<SchoolClass?>(null) }

    val classState by viewModel.classState.collectAsState()

    // Load classes once
    LaunchedEffect(Unit) {
        viewModel.loadClasses(token)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "Add Section",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(16.dp))

        /* ---------- CLASS DROPDOWN ---------- */

        when (classState) {

            is ClassState.Loading -> {
                CircularProgressIndicator()
            }

            is ClassState.Success -> {

                val classes = (classState as ClassState.Success).classes

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {

                    OutlinedTextField(
                        value = selectedClass?.className ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Class") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        classes.forEach { cls ->
                            DropdownMenuItem(
                                text = { Text(cls.className) },
                                onClick = {
                                    selectedClass = cls
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            is ClassState.Error -> {
                Text(
                    text = (classState as ClassState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        /* ---------- SECTION NAME ---------- */

        OutlinedTextField(
            value = sectionName,
            onValueChange = { sectionName = it },
            label = { Text("Section Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        /* ---------- SAVE BUTTON ---------- */

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedClass != null && sectionName.isNotBlank(),
            onClick = {

                viewModel.addSection(
                    token = token,
                    sectionName = sectionName,
                    classId = selectedClass!!.id,
                    instituteId = instituteId
                ) {
                    navController.popBackStack()
                }
            }
        ) {
            Text("Save Section")
        }
    }
}
