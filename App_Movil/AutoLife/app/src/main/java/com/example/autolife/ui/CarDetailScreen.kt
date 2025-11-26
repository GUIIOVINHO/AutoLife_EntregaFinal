package com.example.autolife.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment // ⬅️ Faltaba esta
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.autolife.model.Car
import com.example.autolife.model.Maintenance
import com.example.autolife.viewmodel.CarViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailScreen(
    carId: Int,
    viewModel: CarViewModel,
    onNavigateBack: () -> Unit,
    onAddMaintenanceClick: (Int) -> Unit
) {
    val car by viewModel.getCarById(carId).collectAsState(initial = null)
    val maintenanceList by viewModel.getMaintenancesByCarId(carId).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(car?.model ?: "Cargando Vehículo...") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddMaintenanceClick(carId) }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Mantenimiento")
            }
        }
    ) { paddingValues ->

        // Manejo de carga de datos/error
        if (car == null) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center // ⬅️ Alignment.Center ya funciona con la importación
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        LazyColumn(Modifier.padding(paddingValues).padding(16.dp)) {

            // Sección de Detalle del Vehículo
            item {
                Text("Detalle del Vehículo", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(8.dp))

                // Lógica de foto (omitida por simplicidad, usa Text temporal)
                if (!car?.photoUri.isNullOrEmpty()) {
                    Text("Foto cargada (componente de imagen omitido)", modifier = Modifier.padding(vertical = 8.dp))
                }

                CarDetailCard(car!!) // ⬅️ Componente definido abajo

                Divider(Modifier.padding(vertical = 24.dp))

                // Título del Historial
                Text("Historial de Mantenimiento", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))
            }

            // Sección de Historial de Mantenimiento
            if (maintenanceList.isEmpty()) {
                item { Text("No hay mantenimientos registrados para este vehículo.") } // ⬅️ Línea donde reportabas error
            } else {
                items(maintenanceList) { maintenance ->
                    MaintenanceItem(maintenance) // ⬅️ Componente definido abajo
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

// ------------------------- COMPONENTES REUTILIZABLES -------------------------

@Composable
fun CarDetailCard(car: Car) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("Marca: ${car.make}", style = MaterialTheme.typography.titleMedium)
            Text("Modelo: ${car.model}", style = MaterialTheme.typography.bodyMedium)
            Text("Patente: ${car.plate}", style = MaterialTheme.typography.bodyMedium)
            Text("Año: ${car.year}", style = MaterialTheme.typography.bodyMedium)
            Text("Kilometraje Actual: ${car.currentMileage} km", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun MaintenanceItem(maintenance: Maintenance) {
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    Card(Modifier.fillMaxWidth().heightIn(min = 90.dp)) {
        Column(Modifier.padding(16.dp)) {
            // Fila de Tipo y Fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    maintenance.type,
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
                )
                Text(
                    dateFormatter.format(Date(maintenance.date)),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(4.dp))

            // Detalles
            Text("Costo: $${"%.2f".format(maintenance.cost)}", style = MaterialTheme.typography.bodyMedium)
            Text("Kilometraje: ${maintenance.mileage} km", style = MaterialTheme.typography.bodyMedium)

            // Ubicación (GPS)
            if (!maintenance.location.isNullOrEmpty()) {
                Text("Ubicación: ${maintenance.location}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}