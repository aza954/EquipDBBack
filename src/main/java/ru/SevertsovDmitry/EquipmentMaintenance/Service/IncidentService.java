package ru.SevertsovDmitry.EquipmentMaintenance.Service;

import org.springframework.core.io.ByteArrayResource;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.IncidentDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.IncidentStatus;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Incident;

import java.time.LocalDate;
import java.util.List;

public interface IncidentService {
    Incident createIncident(IncidentDTO incidentDTO);
    IncidentDTO updateIncidentStatus(Long incidentId, IncidentStatus status);
    List<Incident> getIncidents();
    void deleteIncident(Long id);
    List<Incident> getIncidentsByPeriod(LocalDate startDate, LocalDate endDate);
    ByteArrayResource generateReportByPeriod(LocalDate startDate, LocalDate endDate);
}

