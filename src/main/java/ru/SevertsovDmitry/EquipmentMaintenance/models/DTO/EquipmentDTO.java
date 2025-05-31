package ru.SevertsovDmitry.EquipmentMaintenance.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.EquipmentStatus;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.EquipmentType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDTO {
    private String name;
    private LocalDate purchaseDate;
    private EquipmentType type;
    private EquipmentStatus status;
    private Long staffId;
}
