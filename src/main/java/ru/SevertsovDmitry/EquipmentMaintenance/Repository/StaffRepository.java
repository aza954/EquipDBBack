package ru.SevertsovDmitry.EquipmentMaintenance.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Role;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Staff;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    List<Staff> findByRole(Role role);

    Optional<Staff> findByName(String name);

//    Optional<Staff> findByUsername(String userName);
}

