package ru.SevertsovDmitry.EquipmentMaintenance.models;

import jakarta.persistence.*;
import lombok.Data;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.EquipmentStatus;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.EquipmentType;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "equipment")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long equipmentId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate purchaseDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentStatus status;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

}
