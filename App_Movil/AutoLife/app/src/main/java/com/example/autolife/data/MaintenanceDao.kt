package com.example.autolife.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.autolife.model.Maintenance
import kotlinx.coroutines.flow.Flow

/**
 * Interface de Acceso a Datos (DAO) para la entidad Maintenance.
 * Define los métodos que interactúan con la tabla 'maintenances' en la base de datos Room.
 */
@Dao
interface MaintenanceDao {

    /**
     * Inserta un nuevo registro de mantenimiento.
     * El modificador 'suspend' indica que esta es una función de corrutina (asíncrona).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaintenance(maintenance: Maintenance)

    /**
     * Obtiene todos los registros de mantenimiento asociados a un vehículo específico (carId).
     * Esto es esencial para mostrar el historial en la pantalla CarDetailScreen.
     * Retorna un 'Flow' para obtener actualizaciones en tiempo real.
     * * @param carId El ID del vehículo del cual se desea ver el historial.
     */
    @Query("SELECT * FROM maintenances WHERE carId = :carId ORDER BY date DESC")
    fun getMaintenancesForCar(carId: Int): Flow<List<Maintenance>>
}