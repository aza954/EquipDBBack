package ru.SevertsovDmitry.EquipmentMaintenance.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.IncidentStatus;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Incident;

import java.time.LocalDate;
import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByStatus(IncidentStatus status);
    List<Incident> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Incident> findByEquipment_EquipmentId(Long equipmentId);
}

