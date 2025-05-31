package ru.SevertsovDmitry.EquipmentMaintenance.Service;

import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.EquipmentDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.EquipmentStatus;

import java.util.List;

public interface EquipmentService {
    EquipmentDTO createEquipment(EquipmentDTO equipmentDTO);
    List<EquipmentDTO> getEquipmentByStatus(EquipmentStatus status);
    EquipmentDTO updateEquipmentStatus(Long equipmentId, EquipmentStatus status);

    List<EquipmentDTO> getAllEquipment();

    void deleteEquipmentById(Long id);

}
