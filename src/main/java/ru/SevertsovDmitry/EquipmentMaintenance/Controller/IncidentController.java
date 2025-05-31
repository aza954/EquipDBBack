package ru.SevertsovDmitry.EquipmentMaintenance.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.IncidentService;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.IncidentDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.IncidentStatus;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Incident;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

    @Operation(summary = "Создать инцидент", description = "Регистрирует новый инцидент, связанный с оборудованием.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Инцидент успешно создан."),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации.")
    })
    @PostMapping
    public ResponseEntity<Incident> createIncident(@RequestBody IncidentDTO incidentDTO) {
        Incident created = incidentService.createIncident(incidentDTO);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Обновить статус инцидента", description = "Изменяет статус инцидента по его ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статус инцидента обновлён."),
            @ApiResponse(responseCode = "404", description = "Инцидент не найден.")
    })
    @PutMapping("/{incidentId}/status")
    public ResponseEntity<IncidentDTO> updateIncidentStatus(@PathVariable Long incidentId,
                                                            @RequestParam IncidentStatus status) {
        IncidentDTO updated = incidentService.updateIncidentStatus(incidentId, status);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Получить список всех инцидентов", description = "Возвращает список всех инцидентов, связанных с указанным оборудованием.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список инцидентов получен."),
    })
    @GetMapping
    public ResponseEntity<List<Incident>> getIncidentsByEquipment() {
        List<Incident> list = incidentService.getIncidents();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Удалить инцидент по id", description = "Удаляет инцидент")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Инцидент удален."),
    })
    @DeleteMapping("/{id}")
    public HttpStatus deleteIncident(@PathVariable Long id) {
        incidentService.deleteIncident(id);
        return HttpStatus.OK;
    }
}
