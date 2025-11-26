package com.example.autolife.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Clase de datos que representa un Usuario (User) en la aplicación AutoMas.
 * Esta entidad es utilizada para el Registro y el Login.
 * * @Entity(tableName = "users"): Indica a Room que esta clase debe ser mapeada a
 * una tabla llamada 'users' en la base de datos.
 */
@Entity(tableName = "users")
data class User(

    /**
     * @PrimaryKey(autoGenerate = true): Define 'id' como la clave primaria.
     * Aunque el email podría ser la clave, usar un ID numérico es una práctica recomendada
     * en Room para simplificar las relaciones y el manejo interno.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // El correo electrónico, usado para la identificación principal (Login)
    val email: String,

    // Nombre completo del usuario
    val name: String,

    // El hash de la contraseña (se recomienda almacenar un hash, no la contraseña plana)
    val passwordHash: String
)