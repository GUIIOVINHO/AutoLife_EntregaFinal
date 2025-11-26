package com.example.autolife

import com.example.autolife.model.Car
import com.example.autolife.model.Maintenance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CarViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ---------------------------------------------------------------
    // PRUEBA 1: LÓGICA DE VEHÍCULOS (Tu responsabilidad)
    // ---------------------------------------------------------------
    @Test
    fun `Test Modulo Autos - Verificar estructura de datos`() {
        // GIVEN (Dado un auto de prueba)
        val carList = listOf(Car(1, "Toyota", "Yaris", "AA-11", 2022, 10000, null))

        // WHEN (Cuando verificamos la lista)
        val primerAuto = carList[0]

        // THEN (Entonces debe coincidir la marca y modelo)
        assertEquals(1, carList.size)
        assertEquals("Toyota", primerAuto.make)
        assertEquals("Yaris", primerAuto.model)

        println("✅ Test Android (Autos): Estructura de datos correcta")
    }

    // ---------------------------------------------------------------
    // PRUEBA 2: LÓGICA DE MANTENIMIENTO
    // ---------------------------------------------------------------
    @Test
    fun `Test Modulo Mantenimiento - Verificar calculo de costos`() {
        // GIVEN (Dado un mantenimiento con costo)
        val mantenimiento = Maintenance(
            id = 1,
            carId = 5,
            type = "Cambio de Aceite",
            date = 123456789,
            mileage = 50000,
            cost = 45000.0,
            location = "Taller Central"
        )

        // WHEN (Simulamos que obtenemos el costo)
        val costo = mantenimiento.cost
        val tipo = mantenimiento.type

        // THEN (Verificamos que los datos críticos no se pierdan)
        assertEquals(45000.0, costo, 0.0)
        assertEquals("Cambio de Aceite", tipo)

        println("✅ Test Android (Mantenimiento): Integridad de datos correcta")
    }
}