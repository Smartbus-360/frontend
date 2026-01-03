package com.smartbus360.app.teacher.ui.attendance


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smartbus360.app.teacher.data.model.*

@Composable
fun ClassList(
    classes: List<TeacherClass>,
    onClick: (TeacherClass) -> Unit
) {
    LazyColumn {
        items(classes) { cls ->
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                onClick = { onClick(cls) }
            ) {
                Text("Class ${cls.classId} - Section ${cls.sectionId}")
            }
        }
    }
}

@Composable
fun TakeAttendanceUI(
    students: List<StudentAttendanceItem>,
    onSubmit: (List<StudentAttendanceItem>) -> Unit
) {
    val list = remember { students.toMutableStateList() }

    Column {
        LazyColumn(Modifier.weight(1f)) {
            items(list) { student ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Student ID: ${student.studentId}")
                    Row {
                        Button(onClick = { student.status = "P" }) { Text("P") }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { student.status = "A" }) { Text("A") }
                    }
                }
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onSubmit(list) }
        ) {
            Text("Submit Attendance")
        }
    }
}

@Composable
fun TodayAttendanceUI(records: List<AttendanceRecord>) {
    LazyColumn {
        items(records) {
            Text("Student ${it.studentId} : ${it.status}")
        }
    }
}

@Composable
fun SummaryUI(summary: Map<String, Int>) {
    Column {
        summary.forEach { (k, v) ->
            Text("$k : $v")
        }
    }
}
