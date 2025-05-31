package ru.SevertsovDmitry.EquipmentMaintenance.Service;

import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.IncidentDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.IncidentStatus;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Incident;

import java.util.List;

public interface IncidentService {
    IncidentDTO createIncident(IncidentDTO incidentDTO);
    List<IncidentDTO> getIncidentsByStatus(IncidentStatus status);
    IncidentDTO updateIncidentStatus(Long incidentId, IncidentStatus status);
}
