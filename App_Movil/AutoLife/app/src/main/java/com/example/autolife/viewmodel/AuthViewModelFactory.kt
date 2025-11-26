package com.example.autolife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.autolife.data.UserDao

/**
 * Factory personalizada para crear el AuthViewModel.
 * * Este patrón es necesario porque el AuthViewModel requiere el UserDao como
 * argumento en su constructor, y el sistema de Compose/Android lo necesita
 * para construir el ViewModel en el contexto de la aplicación.
 */
@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {

    /**
     * @param modelClass La clase del ViewModel que el sistema de Android quiere crear.
     * @return T La instancia del ViewModel.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica que la clase solicitada sea AuthViewModel
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            // Si lo es, crea una nueva instancia inyectándole el UserDao
            return AuthViewModel(userDao) as T
        }
        // Si se solicita una clase diferente, lanza una excepción
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}