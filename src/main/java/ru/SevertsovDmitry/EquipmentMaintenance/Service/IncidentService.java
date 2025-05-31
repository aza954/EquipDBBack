package ru.SevertsovDmitry.EquipmentMaintenance.Service;

import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.IncidentDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.IncidentStatus;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Incident;

import java.util.List;

public interface IncidentService {
    Incident createIncident(IncidentDTO incidentDTO);
    IncidentDTO updateIncidentStatus(Long incidentId, IncidentStatus status);
    List<Incident> getIncidents();
    void deleteIncident(Long id);
}

