package ru.SevertsovDmitry.EquipmentMaintenance.models;
import lombok.Data;
import jakarta.persistence.*;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.MaintenanceType;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "maintenance")
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maintenanceId;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @Column(nullable = false)
    private LocalDate date;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaintenanceType type;
}

