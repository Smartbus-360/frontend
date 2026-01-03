package com.smartbus360.app.teacher.ui.auth


import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smartbus360.app.teacher.viewModel.TeacherAuthViewModel
import com.smartbus360.app.teacher.viewModel.TeacherAuthState
import com.smartbus360.app.data.repository.PreferencesRepository
import kotlinx.coroutines.flow.collectLatest
import com.smartbus360.app.teacher.navigation.TeacherRoutes

@Composable
fun TeacherLoginScreen(
    navController: NavController,
    viewModel: TeacherAuthViewModel = org.koin.androidx.compose.koinViewModel()
) {
    val context = LocalContext.current
    val prefs = remember { PreferencesRepository(context) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.authState.collectLatest { state ->
            if (state is TeacherAuthState.Success) {
//                prefs.saveTeacherToken(state.data.token)
//                prefs.setUserRole("staff") // teacher
                prefs.saveTeacherToken(state.data.token)
                prefs.saveTeacherId(state.data.teacher.id)      // ✅ ADD THIS
                prefs.saveTeacherProfileFromApi(state.data.teacher)
                prefs.setUserRole("staff")

                navController.navigate(TeacherRoutes.DASHBOARD) {
                    popUpTo(TeacherRoutes.LOGIN) { inclusive = true }
                }

            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Teacher Login",
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Enter email & password", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.login(email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }

            if (authState is TeacherAuthState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            if (authState is TeacherAuthState.Error) {
                Text(
                    text = (authState as TeacherAuthState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
