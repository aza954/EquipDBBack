package ru.SevertsovDmitry.EquipmentMaintenance.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.StaffService;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.StaffDTO;

import java.util.List;

@RestController
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<StaffDTO>> getStaffByRole(@PathVariable Long roleId) {
        return ResponseEntity.ok(staffService.getStaffByRole(roleId));
    }
}

