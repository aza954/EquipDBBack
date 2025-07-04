package ru.SevertsovDmitry.EquipmentMaintenance.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.EquipmentRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.MaintenanceRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.StaffRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.MaintenanceService;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.MaintenanceDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Equipment;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Maintenance;
import ru.SevertsovDmitry.EquipmentMaintenance.util.Exception.EquipmentNotFoundException;
import ru.SevertsovDmitry.EquipmentMaintenance.util.Exception.MaintenanceNotFoundException;
import ru.SevertsovDmitry.EquipmentMaintenance.util.Exception.StaffNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MaintenanceServiceImpl implements MaintenanceService {

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Override
    @Transactional
    public MaintenanceDTO createMaintenance(MaintenanceDTO maintenanceDTO) {
        Maintenance maintenance = new Maintenance();
        Equipment equipment = equipmentRepository.findById(maintenanceDTO.getEquipmentId())
                .orElseThrow(() -> new EquipmentNotFoundException("Equipment not found with id: " + maintenanceDTO.getEquipmentId()));
        maintenance.setEquipment(equipment);
        maintenance.setStaff(staffRepository.findById(maintenanceDTO.getStaffId())
                .orElseThrow(() -> new StaffNotFoundException("Staff not found with id: " + maintenanceDTO.getStaffId())));
        maintenance.setDate(maintenanceDTO.getDate());
        maintenance.setDescription(maintenanceDTO.getDescription());
        maintenance.setType(maintenanceDTO.getType());
        equipment.setStatus(ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.EquipmentStatus.MAINTENANCE);
        equipmentRepository.save(equipment);
        maintenance = maintenanceRepository.save(maintenance);
        return new MaintenanceDTO(
                maintenance.getEquipment().getEquipmentId(),
                maintenance.getStaff().getStaffId(),
                maintenance.getDate(),
                maintenance.getDescription(),
                maintenance.getType()
        );
    }

    @Override
    @Transactional
    public MaintenanceDTO updateMaintenance(Long maintenanceId, MaintenanceDTO maintenanceDTO) {
        Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new MaintenanceNotFoundException("Maintenance record not found with id: " + maintenanceId));
        maintenance.setDate(maintenanceDTO.getDate());
        maintenance.setDescription(maintenanceDTO.getDescription());
        maintenance.setType(maintenanceDTO.getType());
        maintenance = maintenanceRepository.save(maintenance);
        return new MaintenanceDTO(
                maintenance.getEquipment().getEquipmentId(),
                maintenance.getStaff().getStaffId(),
                maintenance.getDate(),
                maintenance.getDescription(),
                maintenance.getType()
        );
    }

    @Override
    public List<MaintenanceDTO> getMaintenanceByEquipment(Long equipmentId) {
        List<Maintenance> maintenances = maintenanceRepository.findByEquipment_EquipmentId(equipmentId);
        return maintenances.stream()
                .map(maintenance -> new MaintenanceDTO(
                        maintenance.getEquipment().getEquipmentId(),
                        maintenance.getStaff().getStaffId(),
                        maintenance.getDate(),
                        maintenance.getDescription(),
                        maintenance.getType()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<Maintenance> getAllMaintenance() {
        return maintenanceRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteMainenceById(Long maintenanceId) {
        maintenanceRepository.deleteById(maintenanceId);
    }
}
