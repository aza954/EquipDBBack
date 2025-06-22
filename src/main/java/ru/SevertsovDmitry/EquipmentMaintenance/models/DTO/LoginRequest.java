package ru.SevertsovDmitry.EquipmentMaintenance.models.DTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
