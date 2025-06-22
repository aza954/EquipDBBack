package ru.SevertsovDmitry.EquipmentMaintenance.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.EquipmentRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.IncidentRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.StaffRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.IncidentService;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.IncidentDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.EquipmentStatus;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.IncidentStatus;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Equipment;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Incident;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@CacheConfig(cacheNames = "incidentCache")
@Transactional(readOnly = true)
public class IncidentServiceImpl implements IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Override
    @Transactional
//    @CacheEvict(allEntries = true)
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
//    @CacheEvict(allEntries = true)
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
//    @Cacheable
    public List<Incident> getIncidents() {
        return incidentRepository.findAll();
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional
    public void deleteIncident(Long id) {
        incidentRepository.deleteById(id);
    }

    @Override
    public List<Incident> getIncidentsByPeriod(LocalDate startDate, LocalDate endDate) {
        return incidentRepository.findByDateBetween(startDate, endDate);
    }

    @Override
    public ByteArrayResource generateReportByPeriod(LocalDate startDate, LocalDate endDate) {
        // Получаем инциденты за указанный период,
        // предполагается, что в IncidentService реализован метод getIncidentsByPeriod
        List<Incident> incidents = getIncidentsByPeriod(startDate, endDate);

        // Формирование отчёта
        StringBuilder report = new StringBuilder();
        report.append("Отчёт по инцидентам с ")
                .append(startDate.toString())
                .append(" по ")
                .append(endDate.toString())
                .append("\n=====================================================\n\n");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (Incident incident : incidents) {
            report.append("ID инцидента: ").append(incident.getIncidentId()).append("\n");
            report.append("Дата: ").append(incident.getDate().format(dtf)).append("\n");
            report.append("Статус: ").append(incident.getStatus().name()).append("\n");
            report.append("Оборудование: ")
                    .append(incident.getEquipment() != null ? incident.getEquipment().getName() : "Не указано")
                    .append("\n");
            report.append("Сотрудник: ")
                    .append(incident.getStaff() != null ? incident.getStaff().getName() : "Не указан")
                    .append("\n");
            report.append("--------------------------------------\n");
        }

        byte[] data = report.toString().getBytes(StandardCharsets.UTF_8);
        ByteArrayResource resource = new ByteArrayResource(data);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=incidents_report_"
                + startDate.toString() + "_to_" + endDate.toString() + ".txt");

        return resource;
    }
}
