package ru.SevertsovDmitry.EquipmentMaintenance.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);
}

