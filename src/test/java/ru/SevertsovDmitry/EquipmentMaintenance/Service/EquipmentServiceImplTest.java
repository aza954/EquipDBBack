package ru.SevertsovDmitry.EquipmentMaintenance.Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.EquipmentRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.StaffRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.Impl.EquipmentServiceImpl;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.EquipmentDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.EquipmentStatus;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.EquipmentType;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Equipment;
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
class EquipmentServiceImplTest {

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private StaffRepository staffRepository;

    @InjectMocks
    private EquipmentServiceImpl equipmentService;

    @Test
    void createEquipment_ShouldReturnEquipmentDTO() {
        EquipmentDTO inputDTO = new EquipmentDTO("Server", LocalDate.now(), EquipmentType.SERVER, EquipmentStatus.ACTIVE, 1L);
        Staff staff = new Staff();
        staff.setStaffId(1L);

        Equipment savedEquipment = new Equipment();
        savedEquipment.setName("Server");
        savedEquipment.setStaff(staff);

        when(staffRepository.findById(1L)).thenReturn(Optional.of(staff));
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(savedEquipment);
        EquipmentDTO result = equipmentService.createEquipment(inputDTO);

        assertNotNull(result);
        assertEquals("Server", result.getName());
        assertEquals(1L, result.getStaffId());
        verify(equipmentRepository, times(1)).save(any(Equipment.class));
    }

    @Test
    void getEquipmentByStatus_ShouldReturnList() {
        Equipment equipment = new Equipment();
        equipment.setStatus(EquipmentStatus.ACTIVE);

        List<Equipment> equipmentList = Collections.singletonList(equipment);

        when(equipmentRepository.findByStatus(EquipmentStatus.ACTIVE)).thenReturn(equipmentList);

        List<EquipmentDTO> result = equipmentService.getEquipmentByStatus(EquipmentStatus.ACTIVE);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(EquipmentStatus.ACTIVE, result.get(0).getStatus());
    }

    @Test
    void updateEquipmentStatus_ShouldReturnUpdatedDTO() {
        Long equipmentId = 1L;
        EquipmentStatus newStatus = EquipmentStatus.ACTIVE;

        Equipment equipment = new Equipment();
        equipment.setEquipmentId(equipmentId);
        equipment.setStatus(EquipmentStatus.FAILED);

        Equipment updatedEquipment = new Equipment();
        updatedEquipment.setEquipmentId(equipmentId);
        updatedEquipment.setStatus(newStatus);

        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.of(equipment));
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(updatedEquipment);

        EquipmentDTO result = equipmentService.updateEquipmentStatus(equipmentId, newStatus);

        assertNotNull(result);
        assertEquals(newStatus, result.getStatus());
        verify(equipmentRepository, times(1)).save(any(Equipment.class));
    }

    @Test
    void getAllEquipment_ShouldReturnAll() {
        Equipment equipment = new Equipment();
        equipment.setName("Test Equipment");
        List<Equipment> equipmentList = Collections.singletonList(equipment);

        when(equipmentRepository.findAll()).thenReturn(equipmentList);

        List<Equipment> result = equipmentService.getAllEquipment();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test Equipment", result.get(0).getName());
    }

    @Test
    void deleteEquipmentById_ShouldCallDelete() {
        equipmentService.deleteEquipmentById(1L);

        verify(equipmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void generateServersReport_ShouldReturnResource() {
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(1L);
        equipment.setName("Server-01");
        equipment.setPurchaseDate(LocalDate.of(2023, 1, 15));
        equipment.setType(EquipmentType.SERVER);
        equipment.setStatus(EquipmentStatus.ACTIVE);

        Staff staff = new Staff();
        staff.setName("John Doe");
        equipment.setStaff(staff);

        List<Equipment> equipmentList = Collections.singletonList(equipment);

        when(equipmentRepository.findAll()).thenReturn(equipmentList);

        ByteArrayResource resource = equipmentService.generateServersReport();
        String reportContent = new String(resource.getByteArray(), StandardCharsets.UTF_8);

        assertNotNull(resource);
        assertTrue(reportContent.contains("Отчёт по серверам"));
        assertTrue(reportContent.contains("Server-01"));
        assertTrue(reportContent.contains("John Doe"));
        assertTrue(reportContent.contains("15-01-2023"));
    }

    @Test
    void createEquipment_StaffNotFound_ShouldThrowException() {
        EquipmentDTO inputDTO = new EquipmentDTO("Server", LocalDate.now(), EquipmentType.SERVER, EquipmentStatus.ACTIVE, 999L);

        when(staffRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> equipmentService.createEquipment(inputDTO));
    }

    @Test
    void updateEquipmentStatus_EquipmentNotFound_ShouldThrowException() {
        when(equipmentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> equipmentService.updateEquipmentStatus(999L, EquipmentStatus.ACTIVE));
    }
}