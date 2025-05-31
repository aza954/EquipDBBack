package ru.SevertsovDmitry.EquipmentMaintenance.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Role;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Staff;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    List<Staff> findByRole(Role role);
}

