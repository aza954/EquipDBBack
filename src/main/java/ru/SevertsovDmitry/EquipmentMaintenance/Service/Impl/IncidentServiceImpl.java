package ru.SevertsovDmitry.EquipmentMaintenance.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.EquipmentRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.IncidentRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.StaffRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.IncidentService;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.IncidentDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.IncidentStatus;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Incident;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncidentServiceImpl implements IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private StaffRepository staffRepository;

    public IncidentDTO createIncident(IncidentDTO incidentDTO) {
        Incident incident = new Incident();
        incident.setEquipment(equipmentRepository.findById(incidentDTO.getEquipmentId()).orElseThrow());
        incident.setStaff(staffRepository.findById(incidentDTO.getStaffId()).orElseThrow());
        incident.setDate(incidentDTO.getDate());
        incident.setStatus(incidentDTO.getStatus());

        incident = incidentRepository.save(incident);

        return new IncidentDTO(
                incident.getEquipment().getEquipmentId(),
                incident.getStaff() != null ? incident.getStaff().getStaffId() : null,
                incident.getDate(),
                incident.getStatus()
        );
    }

    public List<IncidentDTO> getIncidentsByStatus(IncidentStatus status) {
        List<Incident> incidents = incidentRepository.findByStatus(status);
        return incidents.stream()
                .map(incident -> new IncidentDTO(
                        incident.getEquipment().getEquipmentId(),
                        incident.getStaff() != null ? incident.getStaff().getStaffId() : null,
                        incident.getDate(),
                        incident.getStatus()))
                .collect(Collectors.toList());
    }

    public IncidentDTO updateIncidentStatus(Long incidentId, IncidentStatus status) {
        Incident incident = incidentRepository.findById(incidentId).orElseThrow();
        incident.setStatus(status);
        incident = incidentRepository.save(incident);

        return new IncidentDTO(
                incident.getEquipment().getEquipmentId(),
                incident.getStaff() != null ? incident.getStaff().getStaffId() : null,
                incident.getDate(),
                incident.getStatus()
        );
    }
}


