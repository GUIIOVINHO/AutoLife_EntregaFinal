package com.example.autolife.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.autolife.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Interface de Acceso a Datos (DAO) para la entidad User.
 * Define los métodos necesarios para manejar la tabla 'users' en la base de datos Room.
 */
@Dao
interface UserDao {

    /**
     * Inserta un nuevo usuario en la tabla.
     * Si un usuario con el mismo ID ya existe, lo reemplaza (aunque la lógica de registro
     * debería prevenir IDs duplicados, Room maneja el conflicto).
     * El modificador 'suspend' indica que esta operación se ejecuta en una corrutina.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    /**
     * Busca un usuario específico por su correo electrónico.
     * Esta es la consulta clave para la funcionalidad de Login.
     * * @param email El correo electrónico del usuario a buscar.
     * @return Flow<User?>: Retorna un 'Flow' que emite el objeto 'User' si se encuentra,
     * o 'null' si el email no existe en la base de datos.
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    fun getUserByEmail(email: String): Flow<User?>
}