package ru.SevertsovDmitry.EquipmentMaintenance.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.MaintenanceService;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.MaintenanceDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.MaintenanceType;

import java.util.List;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    @PostMapping
    public ResponseEntity<MaintenanceDTO> createMaintenance(@RequestBody MaintenanceDTO maintenanceDTO) {
        MaintenanceDTO createdMaintenance = maintenanceService.createMaintenance(maintenanceDTO);
        return ResponseEntity.ok(createdMaintenance);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<MaintenanceDTO>> getMaintenanceByType(@PathVariable MaintenanceType type) {
        return ResponseEntity.ok(maintenanceService.getMaintenanceByType(type));
    }
}

