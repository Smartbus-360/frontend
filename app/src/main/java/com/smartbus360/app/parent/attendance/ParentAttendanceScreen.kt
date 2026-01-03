package com.smartbus360.app.parent.attendance

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ParentAttendanceScreen(
    onMonthly: () -> Unit,
    onDaily: () -> Unit,
    onSummary: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Text("Attendance", style = MaterialTheme.typography.headlineMedium)

        Button(
            onClick = onMonthly,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Monthly Attendance") }

        Button(
            onClick = onDaily,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Daily Attendance") }

        Button(
            onClick = onSummary,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Attendance Summary") }
    }
}
