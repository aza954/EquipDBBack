package ru.SevertsovDmitry.EquipmentMaintenance.Service;

import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.MaintenanceDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Maintenance;

import java.util.List;

public interface MaintenanceService {
    MaintenanceDTO createMaintenance(MaintenanceDTO maintenanceDTO);
    MaintenanceDTO updateMaintenance(Long maintenanceId, MaintenanceDTO maintenanceDTO);
    List<MaintenanceDTO> getMaintenanceByEquipment(Long equipmentId);

    List<Maintenance> getAllMaintenance();

    void deleteMainenceById(Long maintenanceId);
}
