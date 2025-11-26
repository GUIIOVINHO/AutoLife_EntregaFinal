package com.example.autolife.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.autolife.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    // Recibe el ViewModel para manejar la lógica de negocio
    viewModel: AuthViewModel,
    // Callback para manejar la navegación exitosa después del registro
    onRegisterSuccess: () -> Unit,
    // Callback para volver a la pantalla anterior (Login)
    onNavigateBack: () -> Unit
) {
    // Estados locales para los campos del formulario
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Observa el estado de autenticación del ViewModel (StateFlow)
    val authState by viewModel.authState.collectAsState()

    // Estado para manejar mensajes de error
    var errorText by remember { mutableStateOf<String?>(null) }

    // ➡️ EFECTO LATERAL: Si la autenticación es exitosa, navega
    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            onRegisterSuccess()
        }
    }

    // Comprobación de la validez del formulario
    val isFormValid = name.isNotBlank() &&
            email.isNotBlank() &&
            password.length >= 6 &&
            password == passwordConfirm

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Usuario") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Crea tu Cuenta AutoMas",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Campo de Nombre
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            // Campo de Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            // Campo de Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (password == passwordConfirm) errorText = null
                },
                label = { Text("Contraseña (mín. 6 caracteres)") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "Toggle password visibility")
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            // Campo de Confirmar Contraseña
            OutlinedTextField(
                value = passwordConfirm,
                onValueChange = {
                    passwordConfirm = it
                    if (password == passwordConfirm) errorText = null
                },
                label = { Text("Confirmar Contraseña") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "Toggle password visibility")
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )

            // Mostrar mensaje de error
            if (errorText != null) {
                Text(
                    text = errorText!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }


            // Botón de Registro
            Button(
                onClick = {
                    if (password != passwordConfirm) {
                        errorText = "Las contraseñas no coinciden."
                    } else if (password.length < 6) {
                        errorText = "La contraseña debe tener al menos 6 caracteres."
                    } else {
                        errorText = null
                        // Llama a la función de registro del ViewModel
                        viewModel.register(
                            email = email,
                            name = name,
                            passwordHash = password
                        )
                    }
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Registrarse", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}