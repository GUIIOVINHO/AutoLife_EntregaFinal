package com.autolife.maintenance_service;

import com.autolife.maintenance_service.controller.MaintenanceController;
import com.autolife.maintenance_service.model.Maintenance;
import com.autolife.maintenance_service.repository.MaintenanceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MaintenanceControllerTest {

    @Mock
    private MaintenanceRepository maintenanceRepository;

    @InjectMocks
    private MaintenanceController maintenanceController;

    @Test
    void testCreateMaintenance() {
        // 1. Crear dato de prueba
        Maintenance mantenimiento = new Maintenance(1L, "Aceite", 123456L, 1000, 50.0, "Taller X");
        
        // Simulamos que al guardar devuelve el mismo objeto
        Mockito.when(maintenanceRepository.save(any(Maintenance.class))).thenReturn(mantenimiento);

        // 2. Ejecutar
        Maintenance resultado = maintenanceController.createMaintenance(mantenimiento);

        // 3. Verificar
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Aceite", resultado.getType());
        System.out.println("✅ Test de Mantenimiento: APROBADO");
    }
}