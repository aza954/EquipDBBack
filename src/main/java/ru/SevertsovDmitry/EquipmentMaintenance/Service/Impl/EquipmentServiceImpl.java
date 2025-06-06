package ru.SevertsovDmitry.EquipmentMaintenance.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.EquipmentRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.StaffRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.EquipmentService;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.EquipmentDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.EquipmentStatus;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Equipment;
import java.util.List;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "equipmentCache")
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Override
//    @CacheEvict(allEntries = true)
    public EquipmentDTO createEquipment(EquipmentDTO equipmentDTO) {
        Equipment equipment = new Equipment();
        equipment.setName(equipmentDTO.getName());
        equipment.setPurchaseDate(equipmentDTO.getPurchaseDate());
        equipment.setType(equipmentDTO.getType());
        equipment.setStatus(equipmentDTO.getStatus());
        equipment.setStaff(staffRepository.findById(equipmentDTO.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + equipmentDTO.getStaffId())));
        equipment = equipmentRepository.save(equipment);
        return new EquipmentDTO(
                equipment.getName(),
                equipment.getPurchaseDate(),
                equipment.getType(),
                equipment.getStatus(),
                equipment.getStaff() != null ? equipment.getStaff().getStaffId() : null
        );
    }

    @Override
    public List<EquipmentDTO> getEquipmentByStatus(EquipmentStatus status) {
        List<Equipment> equipmentList = equipmentRepository.findByStatus(status);
        return equipmentList.stream()
                .map(equipment -> new EquipmentDTO(
                        equipment.getName(),
                        equipment.getPurchaseDate(),
                        equipment.getType(),
                        equipment.getStatus(),
                        equipment.getStaff() != null ? equipment.getStaff().getStaffId() : null
                ))
                .collect(Collectors.toList());
    }

    @Override
//    @CacheEvict(allEntries = true)
    public EquipmentDTO updateEquipmentStatus(Long equipmentId, EquipmentStatus status) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + equipmentId));
        equipment.setStatus(status);
        equipment = equipmentRepository.save(equipment);
        return new EquipmentDTO(
                equipment.getName(),
                equipment.getPurchaseDate(),
                equipment.getType(),
                equipment.getStatus(),
                equipment.getStaff() != null ? equipment.getStaff().getStaffId() : null
        );
    }

    @Override
//    @Cacheable
    public List<Equipment> getAllEquipment() {
        List<Equipment> equipment = equipmentRepository.findAll();
        return equipment;
    }

    @Override
//    @CacheEvict(allEntries = true)
    public void deleteEquipmentById(Long id) {
        equipmentRepository.deleteById(id);
    }
}
