package com.smartbus360.app.erpadmin.ui.login


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smartbus360.app.erpadmin.util.TokenManager
import com.smartbus360.app.erpadmin.viewmodel.AdminAuthViewModel
import com.smartbus360.app.erpadmin.viewmodel.AuthState

@Composable
fun AdminErpLoginScreen(
    navController: NavController,
    viewModel: AdminAuthViewModel,
    tokenManager: TokenManager
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val state by viewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("Admin Login", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.login(email, password) {
                tokenManager.saveToken(it)
                navController.navigate("dashboard") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }) {
            Text("Login")
        }

        when (state) {
            is AuthState.Loading -> CircularProgressIndicator()
            is AuthState.Error -> Text((state as AuthState.Error).message, color = MaterialTheme.colorScheme.error)
            else -> {}
        }
    }
}
