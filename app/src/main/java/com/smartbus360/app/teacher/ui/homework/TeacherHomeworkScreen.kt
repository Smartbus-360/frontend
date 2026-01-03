//package com.smartbus360.app.teacher.ui.homework
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import com.smartbus360.app.teacher.viewModel.HomeworkViewModel
//import com.smartbus360.app.teacher.viewModel.HomeworkState
//import com.smartbus360.app.data.repository.PreferencesRepository
//import com.smartbus360.app.teacher.data.model.CreateHomeworkRequest
//import org.koin.androidx.compose.koinViewModel
//
//@Composable
//fun TeacherHomeworkScreen(
//    viewModel: HomeworkViewModel = koinViewModel()
//) {
//    val context = LocalContext.current
//    val token = PreferencesRepository(context).getTeacherToken() ?: return
//    val teacher = PreferencesRepository(context).getTeacherProfile() ?: return
//
//    val state by viewModel.state.collectAsState()
//
//    // Create form state
//    var title by remember { mutableStateOf("") }
//    var description by remember { mutableStateOf("") }
//    var dueDate by remember { mutableStateOf("") }
//
//    // Edit dialog state
//    var showEditDialog by remember { mutableStateOf(false) }
//    var selectedHomework by remember { mutableStateOf<com.smartbus360.app.teacher.data.model.Homework?>(null) }
//    var editTitle by remember { mutableStateOf("") }
//    var editDescription by remember { mutableStateOf("") }
//
//    LaunchedEffect(Unit) {
//        viewModel.loadHomework(token, teacher.id)
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//
//        Text("Homework", style = MaterialTheme.typography.headlineMedium)
//        Spacer(Modifier.height(12.dp))
//
//        // -------- CREATE HOMEWORK --------
//        OutlinedTextField(
//            value = title,
//            onValueChange = { title = it },
//            label = { Text("Title") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        OutlinedTextField(
//            value = description,
//            onValueChange = { description = it },
//            label = { Text("Description") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        OutlinedTextField(
//            value = dueDate,
//            onValueChange = { dueDate = it },
//            label = { Text("Due Date (YYYY-MM-DD)") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(Modifier.height(8.dp))
//
//        Button(
//            modifier = Modifier.fillMaxWidth(),
//            onClick = {
//                if (teacher.classId == null || teacher.sectionId == null) return@Button
//
//                viewModel.createHomework(
//                    token = token,
//                    teacherId = teacher.id,
//                    request = CreateHomeworkRequest(
//                        classId = teacher.classId!!,
//                        sectionId = teacher.sectionId!!,
//                        subjectId = 5, // TODO: replace with subject dropdown later
//                        title = title,
//                        description = description,
//                        dueDate = dueDate
//                    )
//                )
//
//                title = ""
//                description = ""
//                dueDate = ""
//            }
//        ) {
//            Text("Create Homework")
//        }
//
//        Spacer(Modifier.height(16.dp))
//
//        // -------- HOMEWORK LIST --------
//        when (state) {
//            is HomeworkState.Loading -> {
//                CircularProgressIndicator()
//            }
//
//            is HomeworkState.ListLoaded -> {
//                val homeworkList = (state as HomeworkState.ListLoaded).homework
//
//                LazyColumn {
//                    items(homeworkList) { hw ->
//                        Card(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 6.dp),
//                            elevation = CardDefaults.cardElevation(4.dp)
//                        ) {
//                            Column(Modifier.padding(12.dp)) {
//
//                                Text(hw.title ?: "-", fontWeight = FontWeight.Bold)
//                                Text(hw.description ?: "-")
//                                Text(
//                                    "Due: ${hw.dueDate ?: "-"}",
//                                    style = MaterialTheme.typography.bodySmall
//                                )
//
//                                Spacer(Modifier.height(8.dp))
//
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.End
//                                ) {
//                                    TextButton(onClick = {
//                                        selectedHomework = hw
//                                        editTitle = hw.title ?: ""
//                                        editDescription = hw.description ?: ""
//                                        showEditDialog = true
//                                    }) {
//                                        Text("Edit")
//                                    }
//
//                                    TextButton(onClick = {
//                                        viewModel.deleteHomework(
//                                            token = token,
//                                            teacherId = teacher.id,
//                                            homeworkId = hw.id
//                                        )
//                                    }) {
//                                        Text("Delete", color = Color.Red)
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            is HomeworkState.Error -> {
//                Text(
//                    text = (state as HomeworkState.Error).message,
//                    color = Color.Red
//                )
//            }
//
//            HomeworkState.Created -> {
//                // no UI needed, list auto-refreshes
//            }
//        }
//    }
//
//    // -------- EDIT DIALOG --------
//    if (showEditDialog && selectedHomework != null) {
//        AlertDialog(
//            onDismissRequest = { showEditDialog = false },
//            title = { Text("Edit Homework") },
//            text = {
//                Column {
//                    OutlinedTextField(
//                        value = editTitle,
//                        onValueChange = { editTitle = it },
//                        label = { Text("Title") }
//                    )
//                    OutlinedTextField(
//                        value = editDescription,
//                        onValueChange = { editDescription = it },
//                        label = { Text("Description") }
//                    )
//                }
//            },
//            confirmButton = {
//                Button(onClick = {
//                    viewModel.updateHomework(
//                        token = token,
//                        teacherId = teacher.id,
//                        homeworkId = selectedHomework!!.id,
//                        title = editTitle,
//                        description = editDescription
//                    )
//                    showEditDialog = false
//                }) {
//                    Text("Update")
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = { showEditDialog = false }) {
//                    Text("Cancel")
//                }
//            }
//        )
//    }
//}

package com.smartbus360.app.teacher.ui.homework

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.smartbus360.app.teacher.viewModel.HomeworkViewModel
import com.smartbus360.app.teacher.viewModel.HomeworkState
import com.smartbus360.app.teacher.data.model.*
import com.smartbus360.app.data.repository.PreferencesRepository
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherHomeworkScreen(
    viewModel: HomeworkViewModel = koinViewModel()
) {
    val context = LocalContext.current
//    val token = PreferencesRepository(context).getTeacherToken() ?: return
//    val teacher = PreferencesRepository(context).getTeacherProfile() ?: return

    val prefs = PreferencesRepository(context)
    val token = prefs.getTeacherToken() ?: return
    val teacherId = prefs.getTeacherId()
    if (teacherId == 0) return
    val teacherProfile = prefs.getTeacherProfile() ?: return
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val state by viewModel.state.collectAsState()

    // -------- CREATE --------
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var subjectId by remember { mutableStateOf("") } // MANUAL INPUT

    // -------- UPDATE --------
    var selectedHomework by remember { mutableStateOf<HomeworkItem?>(null) }
    var editTitle by remember { mutableStateOf("") }
    var editDescription by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        println("DEBUG teacherId = $teacherId")
    }

    LaunchedEffect(Unit) {
        viewModel.loadHomework(token, teacherId)
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        Text("Homework", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))

        // ================= CREATE HOMEWORK =================

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

//        OutlinedTextField(
//            value = dueDate,
//            onValueChange = { dueDate = it },
//            label = { Text("Due Date (YYYY-MM-DD)") },
//            modifier = Modifier.fillMaxWidth()
//        )
        OutlinedTextField(
            value = selectedDate?.format(formatter) ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Due Date") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Pick Date")
                }
            }
        )


        OutlinedTextField(
            value = subjectId,
            onValueChange = { subjectId = it },
            label = { Text("Subject ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (
                    teacherProfile.classId == null ||
                    teacherProfile.sectionId == null ||
                    subjectId.isBlank() ||
                    selectedDate == null
                ) return@Button

                viewModel.createHomework(
                    token = token,
                    teacherId = teacherId,
                            request = CreateHomeworkRequest(
                        classId = teacherProfile.classId!!,
                        sectionId = teacherProfile.sectionId!!,
                        subjectId = subjectId.toInt(),
                        title = title,
                        description = description,
                                dueDate = selectedDate!!.format(formatter)
                    )
                )

                title = ""
                description = ""
                dueDate = ""
                subjectId = ""
            }
        ) {
            Text("Create Homework")
        }

        Spacer(Modifier.height(20.dp))

        // ================= UPDATE / DELETE =================

        when (state) {
            is HomeworkState.Loading -> {
                CircularProgressIndicator()
            }

            is HomeworkState.ListLoaded -> {
                val homeworkList = (state as HomeworkState.ListLoaded).homework

                LazyColumn {
                    items(homeworkList) { hw ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(Modifier.padding(12.dp)) {

                                Text(hw.title, fontWeight = FontWeight.Bold)
                                Text(hw.description ?: "-")
                                Text("Due: ${hw.dueDate}")

                                Spacer(Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(onClick = {
                                        selectedHomework = hw
                                        editTitle = hw.title
                                        editDescription = hw.description ?: ""
                                    }) {
                                        Text("Edit")
                                    }

                                    TextButton(onClick = {
                                        viewModel.deleteHomework(
                                            token,
                                            teacherId = teacherId,
                                            hw.id
                                        )
//                                        viewModel.loadHomework(token, teacher.id)
                                    }) {
                                        Text("Delete", color = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            is HomeworkState.Error -> {
                Text(
                    text = (state as HomeworkState.Error).message,
                    color = Color.Red
                )
            }

            HomeworkState.Created -> {}
        }

        // ================= EDIT PANEL =================
        if (selectedHomework != null) {
            Spacer(Modifier.height(16.dp))
            Text("Update Homework", fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = editTitle,
                onValueChange = { editTitle = it },
                label = { Text("New Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = editDescription,
                onValueChange = { editDescription = it },
                label = { Text("New Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.updateHomework(
                        token = token,
                        homeworkId = selectedHomework!!.id,
                        teacherId = teacherId,
                        body = mapOf(
                            "title" to editTitle,
                            "description" to editDescription
                        )
                    )

                    selectedHomework = null
                    viewModel.loadHomework(token, teacherId)
                }
            ) {
                Text("Update Homework")
            }
        }
    }

    // ================= DATE PICKER DIALOG =================
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        selectedDate = java.time.Instant
                            .ofEpochMilli(millis)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

}
