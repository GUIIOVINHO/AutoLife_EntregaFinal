package com.autolife.maintenance_service.controller; // Asegúrate que el paquete coincida con tu proyecto

import com.autolife.maintenance_service.model.Maintenance;
import com.autolife.maintenance_service.repository.MaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenances")
public class MaintenanceController {

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    // ------------------------------------------------
    // 1. CREATE (Crear) - POST
    // ------------------------------------------------
    @PostMapping
    public Maintenance createMaintenance(@RequestBody Maintenance maintenance) {
        return maintenanceRepository.save(maintenance);
    }

    // ------------------------------------------------
    // 2. READ (Leer por Auto) - GET
    // ------------------------------------------------
    @GetMapping("/car/{carId}")
    public List<Maintenance> getMaintenancesByCar(@PathVariable Long carId) {
        return maintenanceRepository.findByCarId(carId);
    }

    // ------------------------------------------------
    // 3. READ (Leer Uno solo por ID) - GET (Opcional)
    // ------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Maintenance> getMaintenanceById(@PathVariable Long id) {
        return maintenanceRepository.findById(id)
                .map(maintenance -> ResponseEntity.ok(maintenance))
                .orElse(ResponseEntity.notFound().build());
    }

    // ------------------------------------------------
    // 4. UPDATE (Actualizar) - PUT
    // ------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<Maintenance> updateMaintenance(@PathVariable Long id, @RequestBody Maintenance details) {
        return maintenanceRepository.findById(id)
                .map(maintenance -> {
                    // Actualizamos los campos editables
                    maintenance.setType(details.getType());
                    maintenance.setDate(details.getDate());
                    maintenance.setMileage(details.getMileage());
                    maintenance.setCost(details.getCost());
                    maintenance.setLocation(details.getLocation());
                    
                    // Guardamos los cambios
                    Maintenance updatedMaintenance = maintenanceRepository.save(maintenance);
                    return ResponseEntity.ok(updatedMaintenance);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ------------------------------------------------
    // 5. (Eliminar) - DELETE
    // ------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Long id) {
        if (maintenanceRepository.existsById(id)) {
            maintenanceRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}