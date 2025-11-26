package com.example.autolife.network

import com.example.autolife.model.Maintenance
import retrofit2.Response
import retrofit2.http.*

interface MaintenanceApiService {

    // Obtener historial de un auto específico
    @GET("/api/maintenances/car/{carId}")
    suspend fun getMaintenancesByCar(@Path("carId") carId: Int): List<Maintenance>

    // Guardar un nuevo mantenimiento
    @POST("/api/maintenances")
    suspend fun createMaintenance(@Body maintenance: Maintenance): Maintenance

    // Actualizar mantenimiento
    @PUT("/api/maintenances/{id}")
    suspend fun updateMaintenance(@Path("id") id: Int, @Body maintenance: Maintenance): Maintenance

    // Eliminar mantenimiento
    @DELETE("/api/maintenances/{id}")
    suspend fun deleteMaintenance(@Path("id") id: Int): Response<Void>
}