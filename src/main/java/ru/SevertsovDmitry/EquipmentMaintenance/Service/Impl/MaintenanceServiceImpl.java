package ru.SevertsovDmitry.EquipmentMaintenance.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.EquipmentRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.MaintenanceRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.StaffRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.MaintenanceService;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.MaintenanceDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.MaintenanceType;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Maintenance;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private StaffRepository staffRepository;

    public MaintenanceDTO createMaintenance(MaintenanceDTO maintenanceDTO) {
        Maintenance maintenance = new Maintenance();
        maintenance.setEquipment(equipmentRepository.findById(maintenanceDTO.getEquipmentId()).orElseThrow());
        maintenance.setStaff(staffRepository.findById(maintenanceDTO.getStaffId()).orElseThrow());
        maintenance.setDate(maintenanceDTO.getDate());
        maintenance.setDescription(maintenanceDTO.getDescription());
        maintenance.setType(maintenanceDTO.getType());

        maintenance = maintenanceRepository.save(maintenance);

        return new MaintenanceDTO(
                maintenance.getEquipment().getEquipmentId(),
                maintenance.getStaff() != null ? maintenance.getStaff().getStaffId() : null,
                maintenance.getDate(),
                maintenance.getDescription(),
                maintenance.getType()
        );
    }

    public List<MaintenanceDTO> getMaintenanceByType(MaintenanceType type) {
        List<Maintenance> maintenanceList = maintenanceRepository.findByType(type);
        return maintenanceList.stream()
                .map(maintenance -> new MaintenanceDTO(
                        maintenance.getEquipment().getEquipmentId(),
                        maintenance.getStaff() != null ? maintenance.getStaff().getStaffId() : null,
                        maintenance.getDate(),
                        maintenance.getDescription(),
                        maintenance.getType()))
                .collect(Collectors.toList());
    }
}

