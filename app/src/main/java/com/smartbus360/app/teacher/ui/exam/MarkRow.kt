package com.smartbus360.app.teacher.ui.exam

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smartbus360.app.teacher.data.model.StudentMarkItem
import androidx.compose.ui.Alignment

@Composable
fun MarkRow(student: StudentMarkItem) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {
            Text(text = student.studentName?:"Student" )
            Text(
                "Max: ${student.maxMarks}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        OutlinedTextField(
            value = student.marksObtained.toString(),
            onValueChange = {
                student.marksObtained = it.toIntOrNull() ?: 0
            },
            modifier = Modifier.width(100.dp),
            singleLine = true,
            label = { Text("Marks") }
        )
    }
}
