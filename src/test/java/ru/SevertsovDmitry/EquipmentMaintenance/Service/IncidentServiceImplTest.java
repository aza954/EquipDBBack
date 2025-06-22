package ru.SevertsovDmitry.EquipmentMaintenance.Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.EquipmentRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.IncidentRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.StaffRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.Impl.IncidentServiceImpl;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.IncidentDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.EquipmentStatus;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.IncidentStatus;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Equipment;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Incident;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Staff;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidentServiceImplTest {

    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private StaffRepository staffRepository;

    @InjectMocks
    private IncidentServiceImpl incidentService;

    @Test
    void createIncident_WithOpenStatus_ShouldUpdateEquipmentStatus() {
        IncidentDTO inputDTO = new IncidentDTO(1L, 2L, LocalDate.now(), IncidentStatus.OPEN);

        Equipment equipment = new Equipment();
        equipment.setStatus(EquipmentStatus.ACTIVE);

        Staff staff = new Staff();

        Incident savedIncident = new Incident();
        savedIncident.setEquipment(equipment);
        savedIncident.setStaff(staff);
        savedIncident.setStatus(IncidentStatus.OPEN);

        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
        when(staffRepository.findById(2L)).thenReturn(Optional.of(staff));
        when(incidentRepository.save(any(Incident.class))).thenReturn(savedIncident);

        Incident result = incidentService.createIncident(inputDTO);

        assertNotNull(result);
        assertEquals(IncidentStatus.OPEN, result.getStatus());
        assertEquals(EquipmentStatus.FAILED, equipment.getStatus());
        verify(incidentRepository, times(1)).save(any(Incident.class));
        verify(equipmentRepository, times(1)).save(equipment);
    }

    @Test
    void createIncident_WithNonOpenStatus_ShouldNotUpdateEquipment() {
        IncidentDTO inputDTO = new IncidentDTO(1L, 2L, LocalDate.now(), IncidentStatus.IN_PROGRESS);

        Equipment equipment = new Equipment();
        equipment.setStatus(EquipmentStatus.ACTIVE);

        Staff staff = new Staff();
        staff.setStaffId(1L);

        Incident savedIncident = new Incident();
        savedIncident.setEquipment(equipment);
        savedIncident.setStaff(staff);
        savedIncident.setStatus(IncidentStatus.IN_PROGRESS);

        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
        when(staffRepository.findById(2L)).thenReturn(Optional.of(staff));
        when(incidentRepository.save(any(Incident.class))).thenReturn(savedIncident);

        Incident result = incidentService.createIncident(inputDTO);

        assertNotNull(result);
        assertEquals(IncidentStatus.IN_PROGRESS, result.getStatus());
        assertEquals(EquipmentStatus.ACTIVE, equipment.getStatus());
        verify(equipmentRepository, never()).save(any());
    }

    @Test
    void updateIncidentStatus_ToResolved_ShouldUpdateEquipmentStatus() {
        Long incidentId = 1L;
        IncidentStatus newStatus = IncidentStatus.RESOLVED;

        Equipment equipment = new Equipment();
        equipment.setStatus(EquipmentStatus.FAILED);

        Staff staff = new Staff();
        staff.setStaffId(1L);

        Incident incident = new Incident();
        incident.setStaff(staff);
        incident.setStatus(IncidentStatus.OPEN);
        incident.setEquipment(equipment);

        when(incidentRepository.findById(incidentId)).thenReturn(Optional.of(incident));
        when(incidentRepository.save(any(Incident.class))).thenReturn(incident);

        IncidentDTO result = incidentService.updateIncidentStatus(incidentId, newStatus);

        assertNotNull(result);
        assertEquals(newStatus, result.getStatus());
        assertEquals(EquipmentStatus.ACTIVE, equipment.getStatus());
        verify(incidentRepository, times(1)).save(incident);
        verify(equipmentRepository, times(1)).save(equipment);
    }

    @Test
    void updateIncidentStatus_ToOtherStatus_ShouldNotUpdateEquipment() {
        Long incidentId = 1L;
        IncidentStatus newStatus = IncidentStatus.IN_PROGRESS;

        Equipment equipment = new Equipment();
        equipment.setStatus(EquipmentStatus.FAILED);

        Staff staff = new Staff();
        staff.setStaffId(1L);

        Incident incident = new Incident();
        incident.setStaff(staff);
        incident.setStatus(IncidentStatus.OPEN);
        incident.setEquipment(equipment);

        when(incidentRepository.findById(incidentId)).thenReturn(Optional.of(incident));
        when(incidentRepository.save(any(Incident.class))).thenReturn(incident);

        IncidentDTO result = incidentService.updateIncidentStatus(incidentId, newStatus);

        assertNotNull(result);
        assertEquals(newStatus, result.getStatus());
        assertEquals(EquipmentStatus.FAILED, equipment.getStatus());
        verify(equipmentRepository, never()).save(any());
    }

    @Test
    void getIncidents_ShouldReturnAll() {
        Incident incident = new Incident();
        incident.setStatus(IncidentStatus.OPEN);
        List<Incident> incidentList = Collections.singletonList(incident);

        when(incidentRepository.findAll()).thenReturn(incidentList);

        List<Incident> result = incidentService.getIncidents();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(IncidentStatus.OPEN, result.get(0).getStatus());
    }

    @Test
    void deleteIncident_ShouldCallDelete() {
        incidentService.deleteIncident(1L);

        verify(incidentRepository, times(1)).deleteById(1L);
    }

    @Test
    void getIncidentsByPeriod_ShouldReturnFiltered() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        Incident incident = new Incident();
        incident.setDate(LocalDate.of(2023, 6, 15));

        List<Incident> incidentList = Collections.singletonList(incident);

        when(incidentRepository.findByDateBetween(startDate, endDate)).thenReturn(incidentList);

        List<Incident> result = incidentService.getIncidentsByPeriod(startDate, endDate);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(LocalDate.of(2023, 6, 15), result.get(0).getDate());
    }

    @Test
    void generateReportByPeriod_ShouldReturnFormattedReport() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        Equipment equipment = new Equipment();
        equipment.setName("Server-01");

        Staff staff = new Staff();
        staff.setName("John Doe");

        Incident incident = new Incident();
        incident.setIncidentId(1L);
        incident.setDate(LocalDate.of(2023, 6, 15));
        incident.setStatus(IncidentStatus.RESOLVED);
        incident.setEquipment(equipment);
        incident.setStaff(staff);

        List<Incident> incidents = Collections.singletonList(incident);

        when(incidentRepository.findByDateBetween(startDate, endDate)).thenReturn(incidents);

        ByteArrayResource resource = incidentService.generateReportByPeriod(startDate, endDate);
        String reportContent = new String(resource.getByteArray(), StandardCharsets.UTF_8);

        assertNotNull(resource);
        assertTrue(reportContent.contains("Отчёт по инцидентам"));
        assertTrue(reportContent.contains("ID инцидента: 1"));
        assertTrue(reportContent.contains("15-06-2023"));
        assertTrue(reportContent.contains("RESOLVED"));
        assertTrue(reportContent.contains("Server-01"));
        assertTrue(reportContent.contains("John Doe"));
    }

    @Test
    void createIncident_EquipmentNotFound_ShouldThrowException() {
        IncidentDTO inputDTO = new IncidentDTO(999L, 1L, LocalDate.now(), IncidentStatus.OPEN);

        when(equipmentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> incidentService.createIncident(inputDTO));
    }

    @Test
    void createIncident_StaffNotFound_ShouldThrowException() {
        IncidentDTO inputDTO = new IncidentDTO(1L, 999L, LocalDate.now(), IncidentStatus.OPEN);
        Equipment equipment = new Equipment();

        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
        when(staffRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> incidentService.createIncident(inputDTO));
    }

    @Test
    void updateIncidentStatus_IncidentNotFound_ShouldThrowException() {
        when(incidentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> incidentService.updateIncidentStatus(999L, IncidentStatus.RESOLVED));
    }
}
