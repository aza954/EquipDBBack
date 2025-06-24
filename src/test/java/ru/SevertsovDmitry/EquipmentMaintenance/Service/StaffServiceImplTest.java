package ru.SevertsovDmitry.EquipmentMaintenance.Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.RoleRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.StaffRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.Impl.StaffServiceImpl;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.StaffDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Role;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Staff;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StaffServiceImplTest {

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private StaffServiceImpl staffService;

    @Test
    void createStaff_ShouldReturnStaffDTO() {
        StaffDTO inputDTO = new StaffDTO("John Doe", "Developer", 1L, "contact");
        Role role = new Role();
        role.setRoleId(1L);

        Staff savedStaff = new Staff();
        savedStaff.setName("John Doe");
        savedStaff.setPosition("Developer");
        savedStaff.setRole(role);
        savedStaff.setContact("contact");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(staffRepository.save(any(Staff.class))).thenReturn(savedStaff);

        StaffDTO result = staffService.createStaff(inputDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals(1L, result.getRoleId());
        verify(staffRepository, times(1)).save(any(Staff.class));
    }

    @Test
    void getStaffByRole_ShouldReturnList() {
        Role role = new Role();
        role.setRoleId(1L);

        Staff staff = new Staff();
        staff.setName("Test User");
        staff.setRole(role);

        List<Staff> staffList = Collections.singletonList(staff);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(staffRepository.findByRole(role)).thenReturn(staffList);

        List<StaffDTO> result = staffService.getStaffByRole(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test User", result.get(0).getName());
    }

    @Test
    void getStaff_ShouldReturnAllStaff() {
        Staff staff = new Staff();
        staff.setName("Test User");
        List<Staff> staffList = Collections.singletonList(staff);

        when(staffRepository.findAll()).thenReturn(staffList);

        List<Staff> result = staffService.getStaff();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test User", result.get(0).getName());
    }

    @Test
    void getStaffById_ShouldReturnStaff() {
        Staff staff = new Staff();
        staff.setStaffId(1L);
        staff.setName("Test User");

        when(staffRepository.findById(1L)).thenReturn(Optional.of(staff));

        Staff result = staffService.getStaffById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getStaffId());
        assertEquals("Test User", result.getName());
    }

    @Test
    void getStaffByUsername_ShouldReturnStaff() {
        Staff staff = new Staff();
        staff.setName("testuser");

        when(staffRepository.findByName("testuser")).thenReturn(Optional.of(staff));

        Staff result = staffService.getStaffByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getName());
    }

    @Test
    void getStaffById_NotFound_ShouldThrowException() {
        when(staffRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> staffService.getStaffById(999L));
    }

    @Test
    void getStaffByUsername_NotFound_ShouldThrowException() {
        when(staffRepository.findByName("unknown")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> staffService.getStaffByUsername("unknown"));
    }
}
