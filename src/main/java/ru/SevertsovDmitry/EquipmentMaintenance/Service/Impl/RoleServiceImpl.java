package ru.SevertsovDmitry.EquipmentMaintenance.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.RoleRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.RoleService;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.RoleDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Role;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = new Role();
        role.setRoleName(roleDTO.getRoleName());
        role.setAccessLevel(roleDTO.getAccessLevel());

        role = roleRepository.save(role);

        return new RoleDTO(role.getRoleName(), role.getAccessLevel());
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(role -> new RoleDTO(role.getRoleName(), role.getAccessLevel()))
                .collect(Collectors.toList());
    }
}

