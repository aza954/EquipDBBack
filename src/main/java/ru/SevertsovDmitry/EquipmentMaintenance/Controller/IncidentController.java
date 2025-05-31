package ru.SevertsovDmitry.EquipmentMaintenance.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.IncidentService;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.IncidentDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.IncidentStatus;

import java.util.List;

@RestController
@RequestMapping("/incidents")
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

    @PostMapping
    public ResponseEntity<IncidentDTO> createIncident(@RequestBody IncidentDTO incidentDTO) {
        IncidentDTO createdIncident = incidentService.createIncident(incidentDTO);
        return ResponseEntity.ok(createdIncident);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<IncidentDTO>> getIncidentsByStatus(@PathVariable IncidentStatus status) {
        return ResponseEntity.ok(incidentService.getIncidentsByStatus(status));
    }

    @PutMapping("/{incidentId}/status/{status}")
    public ResponseEntity<IncidentDTO> updateIncidentStatus(
            @PathVariable Long incidentId, @PathVariable IncidentStatus status) {
        return ResponseEntity.ok(incidentService.updateIncidentStatus(incidentId, status));
    }
}

