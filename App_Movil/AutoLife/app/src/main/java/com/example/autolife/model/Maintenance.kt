package com.example.autolife.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Clase de datos que representa un registro de Mantenimiento (Maintenance) en la aplicación AutoMas.
 * @Entity(tableName = "maintenances"): Indica a Room que esta clase debe ser mapeada a
 * una tabla llamada 'maintenances' en la base de datos.
 */
@Entity(tableName = "maintenances")
data class Maintenance(

    /**
     * @PrimaryKey(autoGenerate = true): Define 'id' como la clave primaria del registro.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    // ⬅️ Clave foránea: Relación con la tabla Car
    val carId: Int,

    // Detalles del mantenimiento
    val type: String, // Tipo de servicio (ej: Cambio de aceite, revisión de frenos)
    val date: Long, // Fecha del servicio, almacenada como Long (timestamp Unix)
    val mileage: Int, // Kilometraje al momento del servicio
    val cost: Double, // Costo total del servicio

    // ⬅️ Campo para el Recurso Nativo (GPS): Ubicación del taller
    val location: String? = null // Coordenadas o nombre del lugar del servicio. Es opcional.
)