package com.smartbus360.app.parent.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ParentLoginScreen(
    onLoginSuccess: (studentId: Int) -> Unit
) {

    val viewModel: ParentLoginViewModel = org.koin.androidx.compose.koinViewModel()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginState = viewModel.loginState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text("Parent Login", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { viewModel.login(username, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }

            when (val state = loginState.value) {
                is LoginUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(20.dp))
                }
                is LoginUiState.Error -> {
                    Text(state.message, color = MaterialTheme.colorScheme.error)
                }
                is LoginUiState.Success -> {
//                    val studentId = state.data.user.students.firstOrNull()?.id ?: 0
                    onLoginSuccess(state.data.userId)
                }
                else -> Unit
            }
        }
    }
}
