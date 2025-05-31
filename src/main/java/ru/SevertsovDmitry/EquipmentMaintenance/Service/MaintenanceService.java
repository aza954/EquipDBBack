package ru.SevertsovDmitry.EquipmentMaintenance.Service;

import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.MaintenanceDTO;
import java.util.List;

public interface MaintenanceService {
    MaintenanceDTO createMaintenance(MaintenanceDTO maintenanceDTO);
    MaintenanceDTO updateMaintenance(Long maintenanceId, MaintenanceDTO maintenanceDTO);
    List<MaintenanceDTO> getMaintenanceByEquipment(Long equipmentId);
}
