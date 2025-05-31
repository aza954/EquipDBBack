package ru.SevertsovDmitry.EquipmentMaintenance.Service;

import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.MaintenanceDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.MaintenanceType;

import java.util.List;

public interface MaintenanceService {
    MaintenanceDTO createMaintenance(MaintenanceDTO maintenanceDTO);
    List<MaintenanceDTO> getMaintenanceByType(MaintenanceType type);
}
