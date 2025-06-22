package ru.SevertsovDmitry.EquipmentMaintenance.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.EquipmentRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.StaffRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.EquipmentService;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.EquipmentDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.EquipmentStatus;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Equipment;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "equipmentCache")
@Transactional(readOnly = true)
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Override
    @Transactional
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
    @Transactional
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
    @Transactional
//    @CacheEvict(allEntries = true)
    public void deleteEquipmentById(Long id) {
        equipmentRepository.deleteById(id);
    }

    @Override
    public ByteArrayResource generateServersReport() {
        List<Equipment> allEquipment = getAllEquipment();

        StringBuilder report = new StringBuilder();
        report.append("Отчёт по серверам\n");
        report.append("========================================\n\n");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (Equipment equipment : allEquipment) {
            report.append("ID: ").append(equipment.getEquipmentId()).append("\n");
            report.append("Название: ").append(equipment.getName()).append("\n");
            report.append("Дата покупки: ").append(equipment.getPurchaseDate().format(dtf)).append("\n");
            report.append("Тип: ").append(equipment.getType()).append("\n");
            report.append("Статус: ").append(equipment.getStatus()).append("\n");
            report.append("Сотрудник: ").append(
                    equipment.getStaff() != null ? equipment.getStaff().getName() : "Не назначен"
            ).append("\n");
            report.append("----------------------------------------\n");
        }

        byte[] data = report.toString().getBytes(StandardCharsets.UTF_8);
        ByteArrayResource resource = new ByteArrayResource(data);

        return resource;
    }
}
