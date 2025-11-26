package com.example.autolife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autolife.network.RetrofitClient // Asegúrate de importar esto
import com.example.autolife.model.Car
import com.example.autolife.model.Maintenance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CarViewModel : ViewModel() {

    // 1. CLIENTES RETROFIT (La conexión con la nube)
    private val carService = RetrofitClient.carService
    private val maintenanceService = RetrofitClient.maintenanceService

    private val externalService = RetrofitClient.externalService

    // 2. ESTADOS (Donde guardamos los datos que llegan de internet)

    // Lista de autos
    private val _allCars = MutableStateFlow<List<Car>>(emptyList())
    val allCars: StateFlow<List<Car>> = _allCars

    // Auto seleccionado (para ver detalle)
    private val _selectedCar = MutableStateFlow<Car?>(null)

    // Lista de mantenimientos del auto seleccionado
    private val _carMaintenances = MutableStateFlow<List<Maintenance>>(emptyList())
    val carMaintenances: StateFlow<List<Maintenance>> = _carMaintenances

    // Variable para guardar el dólar
    private val _dolarValue = MutableStateFlow<Double?>(null)
    val dolarValue: StateFlow<Double?> = _dolarValue


    // 3. BLOQUE INIT (Lo primero que hace al arrancar)
    init {
        fetchAllCars()
        fetchDolar()
    }

    // -----------------------------------------------------------
    // FUNCIONES PARA TRAER DATOS (GET)
    // -----------------------------------------------------------

    fun fetchAllCars() {
        viewModelScope.launch {
            try {
                // Pide los autos al Microservicio (Puerto 8080)
                val carsFromApi = carService.getAllCars()
                _allCars.value = carsFromApi
            } catch (e: Exception) {
                e.printStackTrace() // Si falla (ej: server apagado), imprime el error
                // Aquí podrías poner un _errorState.value = "Error de conexión"
            }
        }
    }

    // Esta función reemplaza al Flow de Room para obtener un solo auto
    fun getCarById(carId: Int): StateFlow<Car?> {
        viewModelScope.launch {
            try {
                val car = carService.getCarById(carId)
                _selectedCar.value = car
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return _selectedCar
    }

    // Esta función reemplaza al Flow de Room para los mantenimientos
    fun getMaintenancesByCarId(carId: Int): StateFlow<List<Maintenance>> {
        viewModelScope.launch {
            try {
                // Pide mantenimientos al Microservicio (Puerto 8081)
                val maintenances = maintenanceService.getMaintenancesByCar(carId)
                _carMaintenances.value = maintenances
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return _carMaintenances
    }

    fun fetchDolar() {
        viewModelScope.launch {
            try {
                val response = externalService.getDolarValue()
                if (response.serie.isNotEmpty()) {
                    // Tomamos el primer valor (el de hoy)
                    _dolarValue.value = response.serie[0].valor
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Si falla (sin internet), no pasa nada, se queda en null
            }
        }
    }

    // -----------------------------------------------------------
    // FUNCIONES PARA GUARDAR DATOS (POST)
    // -----------------------------------------------------------

    fun saveCar(
        make: String,
        model: String,
        plate: String,
        year: String,
        mileage: String,
        photoUri: String?
    ) = viewModelScope.launch {
        try {
            val newCar = Car(
                id = null,
                make = make,
                model = model,
                plate = plate,
                year = year.toIntOrNull() ?: 0,
                currentMileage = mileage.toIntOrNull() ?: 0,
                photoUri = photoUri
            )
            // Enviamos al servidor
            carService.createCar(newCar)

            // Actualizamos la lista local para que se vea el cambio inmediato
            fetchAllCars()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun saveMaintenance(
        carId: Int,
        type: String,
        date: Long,
        mileage: Int,
        cost: Double,
        location: String?
    ) = viewModelScope.launch {
        try {
            val newMaintenance = Maintenance(
                id = null,
                carId = carId,
                type = type,
                date = date,
                mileage = mileage,
                cost = cost,
                location = location
            )
            // Enviamos al servidor
            maintenanceService.createMaintenance(newMaintenance)

            // Recargamos la lista de mantenimientos
            getMaintenancesByCarId(carId)
        } catch (e: Exception) {
            e.printStackTrace()
            println("ERROR AL GUARDAR: ${e.message}")
        }
    }
}