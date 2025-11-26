package com.autolife.maintenance_service.repository;

import com.autolife.maintenance_service.model.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    
    // Spring crea esta consulta SQL automáticamente al leer el nombre del método:
    // "SELECT * FROM maintenances WHERE car_id = ?"
    List<Maintenance> findByCarId(Long carId);
}