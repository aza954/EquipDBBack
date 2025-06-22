package ru.SevertsovDmitry.EquipmentMaintenance.models;

import jakarta.persistence.*;
import lombok.Data;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.IncidentStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "incidents")
public class Incident implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long incidentId;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentStatus status;

}

