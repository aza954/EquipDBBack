package ru.SevertsovDmitry.EquipmentMaintenance.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.StaffService;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.StaffDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Staff;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<StaffDTO>> getStaffByRole(@PathVariable Long roleId) {
        return ResponseEntity.ok(staffService.getStaffByRole(roleId));
    }

    @GetMapping()
    public ResponseEntity<List<Staff>> getStaff() {
        return ResponseEntity.ok(staffService.getStaff());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Staff> getStaffById(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.getStaffById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<Staff> getStaffById(@PathVariable String username) {
        return ResponseEntity.ok(staffService.getStaffByUsername(username));
    }
}

