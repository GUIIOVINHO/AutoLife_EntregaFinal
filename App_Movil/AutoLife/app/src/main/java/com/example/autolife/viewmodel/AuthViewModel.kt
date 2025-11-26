package com.example.autolife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autolife.data.UserDao
import com.example.autolife.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/**
 * Clase de datos que representa el estado de autenticación de la aplicación.
 * Útil para saber si el usuario ha iniciado sesión y quién es (su ID).
 */
data class AuthState(
    val isAuthenticated: Boolean = false,
    val currentUserId: Int? = null
)

/**
 * ViewModel encargado de la lógica de Autenticación (Login y Registro).
 * Recibe el UserDao a través de la inyección de dependencia proporcionada por AuthViewModelFactory.
 */
class AuthViewModel(private val userDao: UserDao) : ViewModel() {

    // MutableStateFlow para que el ViewModel pueda modificar el estado internamente
    private val _authState = MutableStateFlow(AuthState())

    // StateFlow para que la UI observe el estado de forma segura (solo lectura)
    val authState: StateFlow<AuthState> = _authState

    /**
     * Registra un nuevo usuario en la base de datos.
     * @param passwordHash La contraseña ya procesada (ej: hasheada), no la contraseña plana.
     */
    fun register(email: String, name: String, passwordHash: String) = viewModelScope.launch {
        // Crea el objeto User (el ID será generado automáticamente por Room)
        val newUser = User(
            email = email,
            name = name,
            passwordHash = passwordHash
        )
        // Inserta en la base de datos
        userDao.insert(newUser)

        // Actualiza el estado a autenticado y establece el ID del usuario
        // Nota: En un flujo más robusto, se recuperaría el ID generado después de la inserción.
        _authState.value = AuthState(isAuthenticated = true, currentUserId = newUser.id)
    }

    /**
     * Intenta iniciar sesión buscando el usuario por email y verificando la contraseña.
     */
    fun login(email: String, passwordHash: String) = viewModelScope.launch {
        // Busca el usuario por email. .firstOrNull() espera el primer valor del Flow y luego cancela.
        val user = userDao.getUserByEmail(email).firstOrNull()

        if (user != null && user.passwordHash == passwordHash) {
            // Si el usuario existe y la contraseña coincide (hash), el login es exitoso
            _authState.value = AuthState(isAuthenticated = true, currentUserId = user.id)
        } else {
            // Si el login falla, el estado no cambia (isAuthenticated sigue en false o se mantiene)
            // Se puede añadir lógica aquí para emitir un error a la UI.
        }
    }

    /**
     * Cierra la sesión del usuario.
     */
    fun logout() {
        _authState.value = AuthState(isAuthenticated = false, currentUserId = null)
    }
}