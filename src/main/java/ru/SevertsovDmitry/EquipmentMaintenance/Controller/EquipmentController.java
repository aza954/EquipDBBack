package ru.SevertsovDmitry.EquipmentMaintenance.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.EquipmentService;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.EquipmentDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.EquipmentStatus;

import java.util.List;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @PostMapping
    public ResponseEntity<EquipmentDTO> createEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        EquipmentDTO createdEquipment = equipmentService.createEquipment(equipmentDTO);
        return ResponseEntity.ok(createdEquipment);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EquipmentDTO>> getEquipmentByStatus(@PathVariable EquipmentStatus status) {
        return ResponseEntity.ok(equipmentService.getEquipmentByStatus(status));
    }

    @PutMapping("/{equipmentId}/status/{status}")
    public ResponseEntity<EquipmentDTO> updateEquipmentStatus(
            @PathVariable Long equipmentId, @PathVariable EquipmentStatus status) {
        return ResponseEntity.ok(equipmentService.updateEquipmentStatus(equipmentId, status));
    }
}

