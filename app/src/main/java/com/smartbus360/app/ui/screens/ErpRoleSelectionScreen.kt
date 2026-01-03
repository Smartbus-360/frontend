//package com.smartbus360.app.ui.screens
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//
//@Composable
//fun ErpRoleSelectionScreen(navController: NavController) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//
//        Text(
//            "ERP Module",
//            style = MaterialTheme.typography.titleLarge,
//            modifier = Modifier.padding(bottom = 30.dp)
//        )
//
//        // Parent Login Button
//        Button(
//            onClick = { navController.navigate("parent_login") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 12.dp)
//        ) {
//            Text("Parent Login")
//        }
//
//        // Teacher Login Button
//        Button(
//            onClick = { navController.navigate("teacher") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 12.dp)
//        ) {
//            Text("Teacher Login")
//        }
//    }
//}


package com.smartbus360.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.content.Intent
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErpRoleSelectionScreen(navController: NavController) {
    val context = LocalContext.current   // ✅ ADD THIS

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "ERP Module",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 30.dp)
        )

        // 🔐 ADMIN LOGIN
        Button(
            onClick = {
                context.startActivity(
                    Intent(
                        context,
                        com.smartbus360.app.erpadmin.MainActivity::class.java
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Text("Admin Login")
        }


        // 👨‍🎓 PARENT LOGIN
        Button(
            onClick = { navController.navigate("parent_login") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Text("Parent Login")
        }

        // 👩‍🏫 TEACHER LOGIN
        Button(
            onClick = { navController.navigate("teacher") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Text("Teacher Login")
        }
    }
}
