package com.autolife.car_service.repository;

import com.autolife.car_service.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    // Aquí puedes agregar búsquedas personalizadas si quieres
    // Ejemplo: buscar por patente para evitar duplicados
    boolean existsByPlate(String plate);
}