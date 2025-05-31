package ru.SevertsovDmitry.EquipmentMaintenance.Service;

import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.StaffDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Staff;

import java.util.List;

public interface StaffService {
    StaffDTO createStaff(StaffDTO staffDTO);
    List<StaffDTO> getStaffByRole(Long roleId);
    List<Staff> getStaff();
}
