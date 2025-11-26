package com.example.autolife.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.autolife.viewmodel.CarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterCarScreen(
    viewModel: CarViewModel,
    onSaveSuccess: () -> Unit
) {
    val context = LocalContext.current

    // 1. Estados del formulario
    var make by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var plate by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var mileage by remember { mutableStateOf("") }

    // 2. Estado para la URI de la foto (Recurso Nativo)
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para seleccionar imagen de la galería
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        photoUri = uri // Almacena la URI seleccionada
    }

    // Comprueba si todos los campos requeridos están llenos y son válidos
    val isFormValid = make.isNotBlank() && model.isNotBlank() && plate.isNotBlank() &&
            (year.toIntOrNull() != null) && (mileage.toIntOrNull() != null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Nuevo Vehículo") },
                navigationIcon = {
                    IconButton(onClick = onSaveSuccess) { // Usamos onSaveSuccess para volver
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
                .verticalScroll(rememberScrollState()), // Permite el scroll en pantallas pequeñas
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // -------------------- SECCIÓN DE FOTO (RECURSO NATIVO) --------------------

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable { imagePickerLauncher.launch("image/*") } // Abre la galería al hacer clic
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (photoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(photoUri),
                        contentDescription = "Foto del vehículo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = "Añadir Foto",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("Toca para añadir foto del auto", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            // -------------------- CAMPOS DEL FORMULARIO --------------------

            OutlinedTextField(
                value = make,
                onValueChange = { make = it },
                label = { Text("Marca (Ej: Toyota)") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = model,
                onValueChange = { model = it },
                label = { Text("Modelo (Ej: Corolla)") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = plate,
                onValueChange = { plate = it.uppercase() }, // Patente en mayúsculas
                label = { Text("Patente / Placa") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = year,
                onValueChange = { year = it.filter { char -> char.isDigit() } }, // Solo números
                label = { Text("Año (Ej: 2020)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = mileage,
                onValueChange = { mileage = it.filter { char -> char.isDigit() } }, // Solo números
                label = { Text("Kilometraje Actual (km)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )

            // -------------------- BOTÓN DE GUARDAR --------------------

            Button(
                onClick = {
                    viewModel.saveCar(
                        make = make,
                        model = model,
                        plate = plate,
                        year = year,
                        mileage = mileage,
                        photoUri = photoUri?.toString() // Convierte la URI a String para guardarla en Room
                    )
                    onSaveSuccess() // Navega de vuelta a la lista
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Guardar Vehículo")
            }
        }
    }
}