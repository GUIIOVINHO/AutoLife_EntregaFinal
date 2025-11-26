package com.example.autolife.ui

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.autolife.viewmodel.CarViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Calendar
import kotlin.coroutines.resume

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun RegisterMaintenanceScreen(
    carId: Int,
    viewModel: CarViewModel,
    onSaveSuccess: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // 1. Estados del formulario
    var type by remember { mutableStateOf("") }
    var mileage by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }
    // Almacena las coordenadas o un mensaje
    var locationString by remember { mutableStateOf<String?>("Ubicación no capturada") }

    // 2. Permiso de ubicación (Recurso Nativo: GPS)
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    // Comprobación de formulario
    val isFormValid = type.isNotBlank() && (mileage.toIntOrNull() != null) && (cost.toDoubleOrNull() != null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Mantenimiento (Car ID: $carId)") },
                navigationIcon = {
                    IconButton(onClick = onSaveSuccess) {
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Detalles del Servicio",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Campo Tipo de Mantenimiento
            OutlinedTextField(
                value = type,
                onValueChange = { type = it },
                label = { Text("Tipo de Mantenimiento (Ej: Cambio de aceite)") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            // Campo Kilometraje
            OutlinedTextField(
                value = mileage,
                onValueChange = { mileage = it.filter { char -> char.isDigit() } },
                label = { Text("Kilometraje al momento del servicio (km)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            // Campo Costo
            OutlinedTextField(
                value = cost,
                onValueChange = { cost = it.filter { char -> char.isDigit() || char == '.' } },
                label = { Text("Costo (Ej: 150.50)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )

            // -------------------- SECCIÓN DE UBICACIÓN (RECURSO NATIVO: GPS) --------------------

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = locationString ?: "",
                    onValueChange = { },
                    label = { Text("Ubicación Registrada") },
                    readOnly = true,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))

                // Botón de Captura GPS
                Button(
                    onClick = {
                        // Lógica para pedir y capturar la ubicación
                        if (locationPermissionState.status.isGranted) {
                            coroutineScope.launch {
                                // Muestra un mensaje mientras espera la ubicación
                                locationString = "Obteniendo ubicación..."
                                locationString = captureLocation(context)
                            }
                        } else {
                            locationPermissionState.launchPermissionRequest()
                            Toast.makeText(context, "Permiso de ubicación solicitado", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.height(56.dp)
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Capturar GPS")
                }
            }

            // -------------------- BOTÓN DE GUARDAR --------------------

            Button(
                onClick = {
                    val date = Calendar.getInstance().timeInMillis // Timestamp actual
                    viewModel.saveMaintenance(
                        carId = carId,
                        type = type,
                        date = date,
                        mileage = mileage.toIntOrNull() ?: 0,
                        cost = cost.toDoubleOrNull() ?: 0.0,
                        location = locationString
                    )
                    onSaveSuccess()
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Guardar Mantenimiento")
            }
        }
    }
}

// ------------------------- FUNCIÓN DE RECURSO NATIVO (GPS) -------------------------

/**
 * Función suspendida que utiliza la API de Google Play Services Location para obtener la ubicación.
 */
@SuppressWarnings("MissingPermission") // La comprobación de permiso se realiza en el Composable
suspend fun captureLocation(context: Context): String {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    return suspendCancellableCoroutine { continuation ->
        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                val location = task.result
                val coords = "Lat: ${"%.4f".format(location!!.latitude)}, Lon: ${"%.4f".format(location.longitude)}"
                continuation.resume(coords) { /* cleanup */ }
            } else {
                continuation.resume("No se pudo obtener la ubicación") { /* cleanup */ }
            }
        }
    }
}