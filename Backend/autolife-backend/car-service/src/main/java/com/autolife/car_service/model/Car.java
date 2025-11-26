package com.autolife.car_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cars") // Mismo nombre de tabla que en tu Room
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // En Android es Int, aquí Long es mejor, pero son compatibles

    private String make;           // Marca
    private String model;          // Modelo
    private String plate;          // Patente
    private Integer year;          // Año ("year" es palabra reservada en algunas DB, pero suele funcionar)
    private Integer currentMileage; // Kilometraje actual
    private String photoUri;  // URI de la foto del auto

    // Nota: 'photoUri' lo omitiremos por ahora en el backend porque guardar
    // una ruta local de Android en el servidor no sirve. 
    // Si necesitas fotos reales, eso requiere otro manejo de archivos.

    // --- CONSTRUCTORES ---
    public Car() { }

    public Car(String make, String model, String plate, Integer year, Integer currentMileage, String photoUri) {
        this.make = make;
        this.model = model;
        this.plate = plate;
        this.year = year;
        this.currentMileage = currentMileage;
        this.photoUri = photoUri;
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Integer getCurrentMileage() { return currentMileage; }
    public void setCurrentMileage(Integer currentMileage) { this.currentMileage = currentMileage; }

    public String getPhotoUri() { return photoUri; }
    public void setPhotoUri(String photoUri) { this.photoUri = photoUri; }
}