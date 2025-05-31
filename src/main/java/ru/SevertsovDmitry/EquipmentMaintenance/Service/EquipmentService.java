package ru.SevertsovDmitry.EquipmentMaintenance.Service;

import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.EquipmentDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.EquipmentStatus;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Equipment;

import java.util.List;

public interface EquipmentService {
    EquipmentDTO createEquipment(EquipmentDTO equipmentDTO);
    EquipmentDTO updateEquipmentStatus(Long equipmentId, EquipmentStatus status);
    List<EquipmentDTO> getEquipmentByStatus(EquipmentStatus status);
}
