package com.example.autolife.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Clase de datos que representa un Vehículo (Car) en la aplicación AutoMas.
 * * @Entity(tableName = "cars"): Indica a Room que esta clase debe ser mapeada a
 * una tabla llamada 'cars' en la base de datos.
 */
@Entity(tableName = "cars")
data class Car(

    /**
     * @PrimaryKey(autoGenerate = true): Define 'id' como la clave primaria de la tabla.
     * 'autoGenerate = true' le dice a Room que incremente automáticamente el ID
     * cada vez que se inserta un nuevo vehículo.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    // Datos principales del vehículo
    val make: String, // Marca (ej: Toyota)
    val model: String, // Modelo (ej: Corolla)
    val plate: String, // Patente o Placa (única)
    val year: Int, // Año de fabricación

    // Kilometraje actual del vehículo
    val currentMileage: Int,

    // URI para la foto (Recurso Nativo: Cámara/Galería). Es opcional (nullable).
    val photoUri: String? = null
)