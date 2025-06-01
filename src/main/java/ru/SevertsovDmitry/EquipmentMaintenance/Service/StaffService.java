package ru.SevertsovDmitry.EquipmentMaintenance.Service;

import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.StaffDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Staff;

import java.util.List;
import java.util.Optional;

public interface StaffService {
    StaffDTO createStaff(StaffDTO staffDTO);
    List<StaffDTO> getStaffByRole(Long roleId);
    List<Staff> getStaff();
    Staff getStaffById(Long id);

    Staff getStaffByUsername(String username);
}
