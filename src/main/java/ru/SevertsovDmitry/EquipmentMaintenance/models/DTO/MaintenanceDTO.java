package ru.SevertsovDmitry.EquipmentMaintenance.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.MaintenanceType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceDTO {
    private Long equipmentId;
    private Long staffId;
    private LocalDate date;
    private String description;
    private MaintenanceType type;
}
