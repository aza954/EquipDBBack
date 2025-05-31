package ru.SevertsovDmitry.EquipmentMaintenance.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffDTO {
    private String name;
    private String position;
    private Long roleId;
    private String contact;
}

