package ru.SevertsovDmitry.EquipmentMaintenance.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.RoleRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.StaffRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.StaffService;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.StaffDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Role;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Staff;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    @Override
    public StaffDTO createStaff(StaffDTO staffDTO) {
        Role role = roleRepository.findById(staffDTO.getRoleId()).orElseThrow();
        Staff staff = new Staff();
        staff.setName(staffDTO.getName());
        staff.setPosition(staffDTO.getPosition());
        staff.setContact(staffDTO.getContact());
        staff.setRole(role);

        staff = staffRepository.save(staff);

        return new StaffDTO(
                staff.getName(),
                staff.getPosition(),
                staff.getRole() != null ? staff.getRole().getRoleId() : null,
                staff.getContact()
        );
    }

    @Override
    public List<StaffDTO> getStaffByRole(Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow();
        List<Staff> staffList = staffRepository.findByRole(role);
        return staffList.stream()
                .map(staff -> new StaffDTO(
                        staff.getName(),
                        staff.getPosition(),
                        staff.getRole() != null ? staff.getRole().getRoleId() : null,
                        staff.getContact()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Staff> getStaff() {
        return staffRepository.findAll();
    }

    @Override
    public Staff getStaffById(Long id) {
        return staffRepository.findById(id).orElseThrow(() -> new RuntimeException("Не найден"));
    }

    @Override
    public Staff getStaffByUsername(String username) {
        return staffRepository.findByName(username).orElseThrow(() -> new RuntimeException("Не найден"));
    }
}

