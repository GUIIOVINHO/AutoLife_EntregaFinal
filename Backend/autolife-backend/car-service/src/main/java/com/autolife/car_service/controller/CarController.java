package com.autolife.car_service.controller;

import com.autolife.car_service.model.Car;
import com.autolife.car_service.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars") // Ruta base: http://localhost:8080/api/cars
public class CarController {

    @Autowired
    private CarRepository carRepository;

    // 1. OBTENER TODOS (GET)
    @GetMapping
    public List<Car> getAllCars() {
        // Equivale a tu carDao.getAllCars()
        return carRepository.findAll();
    }

    // 2. OBTENER UNO POR ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return carRepository.findById(id)
                .map(car -> ResponseEntity.ok(car))
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. CREAR NUEVO (POST)
    @PostMapping
    public Car createCar(@RequestBody Car car) {
        // Equivale a tu carDao.insertCar(car)
        return carRepository.save(car);
    }

    // 4. ACTUALIZAR (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car carDetails) {
        return carRepository.findById(id)
                .map(car -> {
                    car.setMake(carDetails.getMake());
                    car.setModel(carDetails.getModel());
                    car.setPlate(carDetails.getPlate());
                    car.setYear(carDetails.getYear());
                    car.setCurrentMileage(carDetails.getCurrentMileage());
                    
                    Car updatedCar = carRepository.save(car);
                    return ResponseEntity.ok(updatedCar);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. ELIMINAR (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}