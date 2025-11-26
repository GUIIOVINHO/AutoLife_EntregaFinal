package com.example.autolife.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.autolife.model.Car
import com.example.autolife.model.Maintenance
import com.example.autolife.model.User

/**
 * Define la base de datos de Room para el proyecto AutoMas.
 *
 * @Database: Lista todas las clases de modelos que serán tablas (entidades).
 * @version 3: La versión de la base de datos. Se incrementa si se cambian los esquemas.
 * @exportSchema false: Deshabilita la exportación de esquemas (opcional).
 */
@Database(
    entities = [Car::class, Maintenance::class, User::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Abstract functions para acceder a los DAOs
    abstract fun userDao(): UserDao
    abstract fun carDao(): CarDao
    abstract fun maintenanceDao(): MaintenanceDao

    /**
     * Objeto Companion para implementar el patrón Singleton.
     * Esto asegura que solo se cree una instancia de la base de datos,
     * lo cual es eficiente y evita problemas de concurrencia.
     */
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Retorna la única instancia de la base de datos. Si no existe, la crea.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                // Constructor de la base de datos: usa el contexto de la aplicación y un nombre de archivo
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "automas_db" // Nombre del archivo de la base de datos
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}