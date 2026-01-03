//package com.smartbus360.app.teacher.ui.attendance
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.smartbus360.app.teacher.viewModel.AttendanceViewModel
//import com.smartbus360.app.teacher.viewModel.AttendanceState
//import com.smartbus360.app.data.repository.PreferencesRepository
//import androidx.compose.ui.platform.LocalContext
//import java.time.LocalDate
//import com.smartbus360.app.teacher.data.model.StudentAttendanceItem
//
//@Composable
//fun TeacherAttendanceScreen(
//    viewModel: AttendanceViewModel = org.koin.androidx.compose.koinViewModel()
//) {
//    val context = LocalContext.current
//    val token = PreferencesRepository(context).getTeacherToken() ?: return
//
//    val state by viewModel.state.collectAsState()
//    val today = LocalDate.now().toString()
//    var selectedClassId by remember { mutableStateOf<Int?>(null) }
//    var selectedSectionId by remember { mutableStateOf<Int?>(null) }
//    var showSummary by remember { mutableStateOf(false) }
//    var showDateAttendance by remember { mutableStateOf(false) }
//
//    LaunchedEffect(Unit) {
//        viewModel.loadClasses(token)
//    }
//
//    when (state) {
//
//        AttendanceState.Idle -> {
//            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                Text("Select a class to take attendance")
//            }
//        }
//
//        AttendanceState.AttendanceTaken -> {
//            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                Text(
//                    "Attendance already taken for today",
//                    color = MaterialTheme.colorScheme.error,
//                    style = MaterialTheme.typography.titleMedium
//                )
//            }
//        }
//
//
//        is AttendanceState.Loading -> {
//            Box(Modifier.fillMaxSize(), Alignment.Center) {
//                CircularProgressIndicator()
//            }
//        }
//
//        is AttendanceState.AttendanceSummaryLoaded -> {
//            Column(
//                Modifier
//                    .fillMaxSize()
//                    .padding(16.dp)
//            ) {
//                Text(
//                    "Attendance Summary",
//                    style = MaterialTheme.typography.titleLarge
//                )
//
//                Spacer(Modifier.height(8.dp))
//
//                state.summary.forEach { (key, value) ->
//                    Text("$key : $value")
//                }
//            }
//        }
//
//        is AttendanceState.AttendanceDateLoaded -> {
//            Column(
//                Modifier
//                    .fillMaxSize()
//                    .padding(16.dp)
//            ) {
//                Text(
//                    "Attendance - $today",
//                    style = MaterialTheme.typography.titleLarge
//                )
//
//                Spacer(Modifier.height(8.dp))
//
//                LazyColumn {
//                    items(state.attendance) { record ->
//                        Row(
//                            Modifier
//                                .fillMaxWidth()
//                                .padding(8.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Text("Student ID: ${record.studentId}")
//                            Text(record.status)
//                        }
//                    }
//                }
//            }
//        }
//
//        Row {
//            Button(onClick = {
//                viewModel.updateAttendance(
//                    token,
//                    UpdateAttendanceRequest(
//                        studentId = record.studentId,
//                        classId = record.classId,
//                        sectionId = record.sectionId,
//                        date = record.date,
//                        status = "P"
//                    )
//                )
//            }) { Text("P") }
//
//            Spacer(Modifier.width(6.dp))
//
//            Button(onClick = {
//                viewModel.updateAttendance(
//                    token,
//                    UpdateAttendanceRequest(
//                        studentId = record.studentId,
//                        classId = record.classId,
//                        sectionId = record.sectionId,
//                        date = record.date,
//                        status = "A"
//                    )
//                )
//            }) { Text("A") }
//        }
//
//                is AttendanceState.ClassLoaded -> {
//            val classes = (state as AttendanceState.ClassLoaded).classes
//
//            Row(
//                Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                Button(
//                    enabled = selectedClassId != null && selectedSectionId != null,
//                    onClick = {
//                        showSummary = true
//                        showDateAttendance = false
//                        viewModel.loadAttendanceSummary(
//                            token,
//                            selectedClassId!!,
//                            selectedSectionId!!,
//                            LocalDate.now().monthValue,
//                            LocalDate.now().year
//                        )
//                    }
//                ) {
//                    Text("Summary")
//                }
//
//                Button(
//                    enabled = selectedClassId != null && selectedSectionId != null,
//                    onClick = {
//                        showDateAttendance = true
//                        showSummary = false
//                        viewModel.loadAttendanceByDate(
//                            token,
//                            selectedClassId!!,
//                            selectedSectionId!!,
//                            today
//                        )
//                    }
//                ) {
//                    Text("Today")
//                }
//            }
//
//            LazyColumn {
//                items(classes) { cls ->
//                    Button(
////                        onClick = {
////                            viewModel.loadStudents(token, cls.classId, cls.sectionId)
////                        },
//                        onClick = {
//                            selectedClassId = cls.classId
//                            selectedSectionId = cls.sectionId
//                            viewModel.checkAttendanceStatus(
//                                token = token,
//                                classId = cls.classId,
//                                sectionId = cls.sectionId,
//                                date = today
//                            )
//                        },
//                                modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(8.dp)
//                    ) {
//                        Text("${cls.classId} - ${cls.sectionId}")
//                    }
//                }
//            }
//        }
//
//        is AttendanceState.StudentsLoaded -> {
//            val students = remember {
//                mutableStateListOf<StudentAttendanceItem>().apply {
//                    addAll((state as AttendanceState.StudentsLoaded).students)
//                }
//            }
//
//            Column(Modifier.fillMaxSize().padding(16.dp)) {
//
//                LazyColumn(Modifier.weight(1f)) {
//                    items(students) { student ->
//                        Row(
//                            Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Text(student.name)
//                            Row {
//                                Button(onClick = { student.status = "P" }) {
//                                    Text("P")
//                                }
//                                Spacer(Modifier.width(6.dp))
//                                Button(onClick = { student.status = "A" }) {
//                                    Text("A")
//                                }
//                            }
//
//                        }
//                    }
//                }
//
////                Button(
////                    onClick = {
////                        viewModel.submitAttendance(
////                            token = token,
////                            classId = selectedClassId!!,
////                            sectionId = selectedSectionId!!,
////                            date = today,
////                            students = students
////                        )
////                    },
////                    modifier = Modifier.fillMaxWidth()
////                ) {
////                    Text("Submit Attendance")
////                }
//                Button(
//                    onClick = {
//                        viewModel.submitAttendance(
//                            token = token,
//                            classId = selectedClassId!!,
//                            sectionId = selectedSectionId!!,
//                            date = today,
//                            students = students
//                        )
//                    },
////                    enabled = true, // will never reach here if taken
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("Submit Attendance")
//                }
//
//            }
//        }
//
//        is AttendanceState.Success -> {
//            Text((state as AttendanceState.Success).message)
//        }
//
//        is AttendanceState.Error -> {
//            Text((state as AttendanceState.Error).message, color = MaterialTheme.colorScheme.error)
//        }
//    }
//}

package com.smartbus360.app.teacher.ui.attendance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.smartbus360.app.data.repository.PreferencesRepository
import com.smartbus360.app.teacher.data.model.*
import com.smartbus360.app.teacher.viewModel.AttendanceState
import com.smartbus360.app.teacher.viewModel.AttendanceViewModel
import java.time.LocalDate

@Composable
fun TeacherAttendanceScreen(
    viewModel: AttendanceViewModel = org.koin.androidx.compose.koinViewModel()
) {
    val context = LocalContext.current
    val token = PreferencesRepository(context).getTeacherToken() ?: return

    val uiState by viewModel.state.collectAsState()
    val today = LocalDate.now().toString()

    var selectedClassId by remember { mutableStateOf<Int?>(null) }
    var selectedSectionId by remember { mutableStateOf<Int?>(null) }

    // ✅ THIS IS WHAT YOU ASKED ABOUT
    var selectedTab by remember { mutableStateOf(AttendanceTab.TAKE) }

    LaunchedEffect(Unit) {
        viewModel.loadClasses(token)
    }

    Column(Modifier.fillMaxSize()) {

        /* -------- HEADER -------- */
        if (selectedClassId != null) {
            Text(
                "Class $selectedClassId - Section $selectedSectionId",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
        }

        /* -------- TABS -------- */
        TabRow(selectedTabIndex = selectedTab.ordinal) {
            AttendanceTab.values().forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    text = { Text(tab.title) },
                    onClick = {
                        selectedTab = tab

                        if (selectedClassId == null || selectedSectionId == null) return@Tab

                        when (tab) {
                            AttendanceTab.TAKE -> {
                                viewModel.startAttendanceFlow(
                                    token,
                                    selectedClassId!!,
                                    selectedSectionId!!,
                                    today
                                )
                            }

                            AttendanceTab.TODAY ->
                                viewModel.loadAttendanceByDate(
                                    token,
                                    selectedClassId!!,
                                    selectedSectionId!!,
                                    today
                                )

                            AttendanceTab.SUMMARY ->
                                viewModel.loadAttendanceSummary(
                                    token,
                                    selectedClassId!!,
                                    selectedSectionId!!,
                                    LocalDate.now().monthValue,
                                    LocalDate.now().year
                                )
                        }
                    }
                )
            }
        }

        Divider()

        /* -------- CONTENT -------- */
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            when (val state = uiState) {

                AttendanceState.Idle -> {
                    Text("Select a class to continue")
                }

                AttendanceState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

//                AttendanceState.AttendanceTaken -> {
//                    Text(
//                        "Attendance already taken today.\nUse tabs to view.",
//                        color = MaterialTheme.colorScheme.error
//                    )
//                }

                is AttendanceState.ClassLoaded -> {
                    ClassList(
                        classes = state.classes,
                        onClick = { cls ->
                            selectedClassId = cls.classId
                            selectedSectionId = cls.sectionId
                            selectedTab = AttendanceTab.TAKE

                            viewModel.startAttendanceFlow(
                                token = token,
                                classId = cls.classId,
                                sectionId = cls.sectionId,
                                date = today
                            )

                        }
                    )
                }

                is AttendanceState.StudentsLoaded -> {
                    TakeAttendanceUI(
                        students = state.students,
                        onSubmit = {
                            viewModel.submitAttendance(
                                token,
                                selectedClassId!!,
                                selectedSectionId!!,
                                today,
                                it
                            )
                        }
                    )
                }

                is AttendanceState.AttendanceDateLoaded -> {
                    TodayAttendanceUI(state.attendance)
                }

                is AttendanceState.AttendanceSummaryLoaded -> {
                    SummaryUI(state.summary)
                }

                is AttendanceState.Success -> {
                    Text(state.message)
                }

                is AttendanceState.Error -> {
                    Text(
                        state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
