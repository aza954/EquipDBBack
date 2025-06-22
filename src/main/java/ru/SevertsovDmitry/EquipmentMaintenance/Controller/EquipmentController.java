package ru.SevertsovDmitry.EquipmentMaintenance.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.EquipmentService;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.EquipmentDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Enum.EquipmentStatus;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Equipment;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
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
    public ResponseEntity<List<Equipment>> getAllEquipment() {
        List<Equipment> list = equipmentService.getAllEquipment();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Удалить оборудование по id", description = "Удаляет оборудование.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Оборудование удалено."),
    })
    @DeleteMapping("/{equipmentId}/delete")
    public HttpStatus deleteEquipment(@PathVariable Long equipmentId) {
        equipmentService.deleteEquipmentById(equipmentId);
        return HttpStatus.OK;
    }

    @Operation(summary = "Отчёт по серверам", description = "Генерирует текстовый отчёт по оборудованию типа SERVER с указанием их характеристик.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Отчёт успешно сгенерирован."),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации данных.")
    })
    @GetMapping("/report-servers")
    public ResponseEntity<ByteArrayResource> generateServersReport() {
        ByteArrayResource resource = equipmentService.generateServersReport();

        byte[] data = resource.toString().getBytes(StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=servers_report.txt");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(data.length)
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }
}
