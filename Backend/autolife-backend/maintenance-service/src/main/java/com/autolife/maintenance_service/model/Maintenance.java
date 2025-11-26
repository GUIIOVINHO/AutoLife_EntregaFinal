package com.autolife.maintenance_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "maintenances")
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ESTO ES IMPORTANTE: Relaciona el mantenimiento con el auto
    private Long carId; 

    private String type;     // Tipo (Aceite, Frenos, etc.)
    private Long date;       // Fecha en formato numérico (timestamp)
    private Integer mileage; // Kilometraje
    private Double cost;     // Costo
    private String location; // Ubicación GPS

    // --- CONSTRUCTORES ---
    public Maintenance() { }

    public Maintenance(Long carId, String type, Long date, Integer mileage, Double cost, String location) {
        this.carId = carId;
        this.type = type;
        this.date = date;
        this.mileage = mileage;
        this.cost = cost;
        this.location = location;
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getDate() { return date; }
    public void setDate(Long date) { this.date = date; }

    public Integer getMileage() { return mileage; }
    public void setMileage(Integer mileage) { this.mileage = mileage; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}