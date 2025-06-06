package ru.SevertsovDmitry.EquipmentMaintenance.models;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String middleName;
    private String name;
    private String position;
    private String contact;
}