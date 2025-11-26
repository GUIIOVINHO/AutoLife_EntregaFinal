package com.autolife.car_service;

import com.autolife.car_service.controller.CarController;
import com.autolife.car_service.model.Car;
import com.autolife.car_service.repository.CarRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @Mock
    private CarRepository carRepository; // Base de datos falsa

    @InjectMocks
    private CarController carController; // Tu controlador

    @Test
    void testGetAllCars() {
        // 1. Datos de mentira
        Car car1 = new Car("Toyota", "Yaris", "AA-11", 2022, 10000, null);
        Car car2 = new Car("Nissan", "Versa", "BB-22", 2023, 5000, null);
        Mockito.when(carRepository.findAll()).thenReturn(Arrays.asList(car1, car2));

        // 2. Ejecutar la prueba
        List<Car> result = carController.getAllCars();

        // 3. Verificar que funcionó
        Assertions.assertEquals(2, result.size()); // Deben haber 2 autos
        Assertions.assertEquals("Toyota", result.get(0).getMake());
        System.out.println("✅ Test de Autos: APROBADO");
    }
}