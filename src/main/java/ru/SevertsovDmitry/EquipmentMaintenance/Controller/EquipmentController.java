package ru.SevertsovDmitry.EquipmentMaintenance.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.EquipmentService;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.EquipmentDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.EquipmentStatus;
import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @Operation(summary = "Создать оборудование", description = "Добавляет новое оборудование в систему.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Оборудование успешно создано."),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации данных.")
    })
    @PostMapping
    public ResponseEntity<EquipmentDTO> createEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        EquipmentDTO created = equipmentService.createEquipment(equipmentDTO);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Получить список оборудования по статусу", description = "Возвращает список оборудования, соответствующего указанному статусу.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список оборудования получен."),
            @ApiResponse(responseCode = "400", description = "Некорректный статус.")
    })
    @GetMapping
    public ResponseEntity<List<EquipmentDTO>> getEquipmentByStatus(@RequestParam EquipmentStatus status) {
        List<EquipmentDTO> list = equipmentService.getEquipmentByStatus(status);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Обновить статус оборудования", description = "Изменяет статус оборудования по его ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статус оборудования обновлён."),
            @ApiResponse(responseCode = "404", description = "Оборудование не найдено.")
    })
    @PutMapping("/{equipmentId}/status")
    public ResponseEntity<EquipmentDTO> updateEquipmentStatus(@PathVariable Long equipmentId,
                                                              @RequestParam EquipmentStatus status) {
        EquipmentDTO updated = equipmentService.updateEquipmentStatus(equipmentId, status);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Получить список всего оборудования", description = "Возвращает список всего оборудования.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список оборудования получен."),
    })
    @GetMapping("/all")
    public ResponseEntity<List<EquipmentDTO>> getAllEquipment() {
        List<EquipmentDTO> list = equipmentService.getAllEquipment();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Удалить оборудование по id", description = "Удаляет оборудование.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Оборудование удалено."),
    })
    @DeleteMapping("/{equipmentId}/delete")
    public ResponseEntity deleteEquipment(@PathVariable Long equipmentId) {
        equipmentService.deleteEquipmentById(equipmentId);
        return (ResponseEntity) ResponseEntity.ok();
    }
}
