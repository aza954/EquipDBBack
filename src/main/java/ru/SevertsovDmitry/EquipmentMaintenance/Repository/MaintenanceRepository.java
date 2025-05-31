package ru.SevertsovDmitry.EquipmentMaintenance.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.MaintenanceType;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Maintenance;

import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    List<Maintenance> findByType(MaintenanceType type);
}

