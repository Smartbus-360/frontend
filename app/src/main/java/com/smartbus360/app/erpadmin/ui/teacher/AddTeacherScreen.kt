package com.smartbus360.app.erpadmin.ui.teacher


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smartbus360.app.erpadmin.data.model.AddTeacherRequest
import com.smartbus360.app.erpadmin.viewmodel.TeacherViewModel
import com.smartbus360.app.erpadmin.util.TokenManager
import com.smartbus360.app.erpadmin.data.model.SchoolClass
import com.smartbus360.app.erpadmin.data.model.Section
import com.smartbus360.app.erpadmin.viewmodel.ClassSectionViewModel
import com.smartbus360.app.erpadmin.viewmodel.ClassState
import com.smartbus360.app.erpadmin.viewmodel.SectionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTeacherScreen(
    navController: NavController,
    teacherViewModel: TeacherViewModel,
    classSectionViewModel: ClassSectionViewModel,
    tokenManager: TokenManager
) {
    val token = "Bearer ${tokenManager.getToken()}"

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var selectedClass by remember { mutableStateOf<SchoolClass?>(null) }
    var selectedSection by remember { mutableStateOf<Section?>(null) }

    var classExpanded by remember { mutableStateOf(false) }
    var sectionExpanded by remember { mutableStateOf(false) }

    val classState by classSectionViewModel.classState.collectAsState()
    val sectionState by classSectionViewModel.sectionState.collectAsState()

    // Load classes once
    LaunchedEffect(Unit) {
        classSectionViewModel.loadClasses(token)
    }

    // Load sections when class changes
    LaunchedEffect(selectedClass) {
        selectedClass?.let {
            classSectionViewModel.loadSections(token, it.id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text("Add Teacher", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(name, { name = it }, label = { Text("Full Name") })
        OutlinedTextField(email, { email = it }, label = { Text("Email") })
        OutlinedTextField(phone, { phone = it }, label = { Text("Phone") })
        OutlinedTextField(password, { password = it }, label = { Text("Password") })

        Spacer(Modifier.height(16.dp))

        /* -------- CLASS DROPDOWN -------- */
        ExposedDropdownMenuBox(
            expanded = classExpanded,
            onExpandedChange = { classExpanded = !classExpanded }
        ) {
            OutlinedTextField(
                value = selectedClass?.className ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Class") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(classExpanded)
                },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = classExpanded,
                onDismissRequest = { classExpanded = false }
            ) {
                (classState as? ClassState.Success)?.classes?.forEach { cls ->
                    DropdownMenuItem(
                        text = { Text(cls.className) },
                        onClick = {
                            selectedClass = cls
                            selectedSection = null
                            classExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        /* -------- SECTION DROPDOWN -------- */
        ExposedDropdownMenuBox(
            expanded = sectionExpanded,
            onExpandedChange = { sectionExpanded = !sectionExpanded }
        ) {
            OutlinedTextField(
                value = selectedSection?.sectionName ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Section") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(sectionExpanded)
                },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = sectionExpanded,
                onDismissRequest = { sectionExpanded = false }
            ) {
                (sectionState as? SectionState.Success)?.sections?.forEach { sec ->
                    DropdownMenuItem(
                        text = { Text(sec.sectionName) },
                        onClick = {
                            selectedSection = sec
                            sectionExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            enabled = selectedClass != null && selectedSection != null,
            onClick = {
                teacherViewModel.addTeacher(
                    token,
                    AddTeacherRequest(
                        full_name = name,
                        email = email,
                        phone = phone,
                        password = password,
                        classId = selectedClass!!.id,
                        sectionId = selectedSection!!.id
                    )
                ) {
                    navController.popBackStack()
                }
            }
        ) {
            Text("Save Teacher")
        }
    }
}
