package com.example.autolife.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // URLs para el Emulador (10.0.2.2)
    private const val BASE_URL_CARS = "http://10.0.2.2:8080/"
    private const val BASE_URL_MAINTENANCE = "http://10.0.2.2:8081/"

    // API EXTERNA
    private const val BASE_URL_EXTERNAL = "https://mindicador.cl/api/"

    // 1. Cliente para Autos
    private val retrofitCars = Retrofit.Builder()
        .baseUrl(BASE_URL_CARS)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Variable pública que usarás en el ViewModel
    val carService: CarApiService = retrofitCars.create(CarApiService::class.java)


    // 2. Cliente para Mantenimiento
    private val retrofitMaintenance = Retrofit.Builder()
        .baseUrl(BASE_URL_MAINTENANCE)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Variable pública que usarás en el ViewModel
    val maintenanceService: MaintenanceApiService = retrofitMaintenance.create(MaintenanceApiService::class.java)


    // Cliente Dólar
    val externalService: ExternalApiService = Retrofit.Builder()
        .baseUrl(BASE_URL_EXTERNAL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ExternalApiService::class.java)
}