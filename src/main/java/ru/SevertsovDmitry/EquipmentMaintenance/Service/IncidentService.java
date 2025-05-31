package ru.SevertsovDmitry.EquipmentMaintenance.Service;

import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.IncidentDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.IncidentStatus;
import java.util.List;

public interface IncidentService {
    IncidentDTO createIncident(IncidentDTO incidentDTO);
    IncidentDTO updateIncidentStatus(Long incidentId, IncidentStatus status);
    List<IncidentDTO> getIncidentsByEquipment(Long equipmentId);
}

