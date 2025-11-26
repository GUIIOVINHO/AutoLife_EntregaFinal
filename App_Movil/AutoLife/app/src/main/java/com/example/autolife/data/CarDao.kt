package com.example.autolife.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.autolife.model.Car
import kotlinx.coroutines.flow.Flow

/**
 * Interface de Acceso a Datos (DAO) para la entidad Car.
 * Define los métodos que interactúan con la tabla 'cars' en la base de datos Room.
 */
@Dao
interface CarDao {

    /**
     * Inserta un nuevo vehículo o reemplaza uno existente si hay conflicto (basado en la PrimaryKey).
     * El modificador 'suspend' indica que esta es una función de corrutina (asíncrona).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: Car)

    /**
     * Obtiene todos los vehículos de la base de datos, ordenados por ID de forma descendente.
     * Retorna un 'Flow', lo que significa que el ViewModel recibirá automáticamente
     * las actualizaciones cada vez que la tabla cambie.
     */
    @Query("SELECT * FROM cars ORDER BY id DESC")
    fun getAllCars(): Flow<List<Car>>

    /**
     * Obtiene un vehículo específico usando su ID.
     * Retorna un 'Flow' con un solo objeto 'Car' (o null si no se encuentra).
     */
    @Query("SELECT * FROM cars WHERE id = :carId LIMIT 1")
    fun getCar(carId: Int): Flow<Car?>
}