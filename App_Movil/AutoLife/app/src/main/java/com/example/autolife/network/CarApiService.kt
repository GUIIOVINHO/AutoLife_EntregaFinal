package com.example.autolife.network

import com.example.autolife.model.Car
import retrofit2.Response
import retrofit2.http.*

interface CarApiService {

    // Obtener todos los autos
    @GET("/api/cars")
    suspend fun getAllCars(): List<Car>

    // Obtener un auto por ID
    @GET("/api/cars/{id}")
    suspend fun getCarById(@Path("id") id: Int): Car

    // Guardar un nuevo auto
    @POST("/api/cars")
    suspend fun createCar(@Body car: Car): Car

    // Actualizar un auto existente
    @PUT("/api/cars/{id}")
    suspend fun updateCar(@Path("id") id: Int, @Body car: Car): Car

    // Eliminar un auto
    @DELETE("/api/cars/{id}")
    suspend fun deleteCar(@Path("id") id: Int): Response<Void>
}