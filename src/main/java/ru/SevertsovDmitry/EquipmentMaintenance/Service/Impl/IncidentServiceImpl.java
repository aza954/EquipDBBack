package ru.SevertsovDmitry.EquipmentMaintenance.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.IncidentRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.EquipmentRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.StaffRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.IncidentService;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.IncidentDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.IncidentStatus;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Equipment;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Incident;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.EquipmentStatus;
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

    @Override
    @Transactional
    public Incident createIncident(IncidentDTO incidentDTO) {
        Incident incident = new Incident();
        Equipment equipment = equipmentRepository.findById(incidentDTO.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + incidentDTO.getEquipmentId()));
        incident.setEquipment(equipment);
        incident.setStaff(staffRepository.findById(incidentDTO.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + incidentDTO.getStaffId())));
        incident.setDate(incidentDTO.getDate());
        incident.setStatus(incidentDTO.getStatus());
        if (incidentDTO.getStatus() == IncidentStatus.OPEN) {
            equipment.setStatus(EquipmentStatus.FAILED);
            equipmentRepository.save(equipment);
        }
        incident = incidentRepository.save(incident);
        return incident;
    }

    @Override
    @Transactional
    public IncidentDTO updateIncidentStatus(Long incidentId, IncidentStatus status) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found with id: " + incidentId));
        incident.setStatus(status);
        if (status == IncidentStatus.RESOLVED) {
            Equipment equipment = incident.getEquipment();
            equipment.setStatus(EquipmentStatus.ACTIVE);
            equipmentRepository.save(equipment);
        }
        incident = incidentRepository.save(incident);
        return new IncidentDTO(
                incident.getEquipment().getEquipmentId(),
                incident.getStaff().getStaffId(),
                incident.getDate(),
                incident.getStatus()
        );
    }

    @Override
    public List<Incident> getIncidents() {
        return incidentRepository.findAll();
    }

    @Override
    public void deleteIncident(Long id) {
        incidentRepository.deleteById(id);
    }
}
