package com.example.autolife.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp // Icono de Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.autolife.model.Car
import com.example.autolife.viewmodel.AuthViewModel
import com.example.autolife.viewmodel.CarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarListScreen(
    // Recibe el ViewModel para obtener la lista de autos
    viewModel: CarViewModel,
    // Recibe el ViewModel para la acción de logout
    authViewModel: AuthViewModel,
    // Callback para navegar a la pantalla de registro de autos
    onAddCarClick: () -> Unit,
    // Callback para navegar a la pantalla de detalle de auto, pasando el ID
    onCarSelected: (Int) -> Unit
) {
    // Observa la lista de todos los autos en tiempo real (Flow)
    val carList by viewModel.allCars.collectAsState(initial = emptyList())

    // Observar el valor del Dólar (Esto ya lo tenías bien)
    val dolar by viewModel.dolarValue.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // 🆕 MODIFICADO: Usamos una Columna para poner el título y abajo el dólar
                    Column {
                        Text("Mis Vehículos - AutoMas")

                        // Si el valor del dólar ya llegó, lo mostramos
                        if (dolar != null) {
                            Text(
                                text = "Dólar hoy: $$dolar",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                },
                actions = {
                    // Botón de Cerrar Sesión
                    IconButton(onClick = { authViewModel.logout() }) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Cerrar Sesión")
                    }
                }
            )
        },
        floatingActionButton = {
            // Botón flotante para añadir un nuevo auto (Recurso Nativo: Cámara/Galería)
            FloatingActionButton(onClick = onAddCarClick) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Vehículo")
            }
        }
    ) { paddingValues ->

        // Contenido principal de la lista
        if (carList.isEmpty()) {
            // Muestra un mensaje si no hay autos
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No tienes vehículos registrados. ¡Añade uno con el botón '+'!",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            // Muestra la lista de autos usando LazyColumn para eficiencia
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(carList) { car ->
                    CarListItem(car = car, onClick = onCarSelected)
                }
            }
        }
    }
}

// ------------------------- COMPONENTE DE ITEM DE LISTA -------------------------

@Composable
fun CarListItem(car: Car, onClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            // ⬅️ Al hacer clic, llama al callback con el ID del auto
            .clickable { onClick(car.id ?: 0) }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    // Ejemplo: Honda Civic
                    "${car.make} ${car.model}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    // Ejemplo: Patente: XXXX-00
                    "Patente: ${car.plate}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            // Muestra el kilometraje actual
            Text(
                "${car.currentMileage} km",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}