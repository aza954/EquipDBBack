package ru.SevertsovDmitry.EquipmentMaintenance.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.MaintenanceService;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.MaintenanceDTO;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    @Operation(summary = "Создать запись о техобслуживании", description = "Добавляет новую запись об обслуживании оборудования.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запись успешно создана."),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации данных.")
    })
    @PostMapping
    public ResponseEntity<MaintenanceDTO> createMaintenance(@RequestBody MaintenanceDTO maintenanceDTO) {
        MaintenanceDTO created = maintenanceService.createMaintenance(maintenanceDTO);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Обновить данные техобслуживания", description = "Изменяет данные записи об обслуживании по её ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запись обновлена."),
            @ApiResponse(responseCode = "404", description = "Запись не найдена.")
    })
    @PutMapping("/{maintenanceId}")
    public ResponseEntity<MaintenanceDTO> updateMaintenance(@PathVariable Long maintenanceId,
                                                            @RequestBody MaintenanceDTO maintenanceDTO) {
        MaintenanceDTO updated = maintenanceService.updateMaintenance(maintenanceId, maintenanceDTO);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Получить список обслуживаний по оборудованию", description = "Возвращает список всех записей об обслуживании оборудования.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список обслуживаний получен."),
            @ApiResponse(responseCode = "404", description = "Оборудование не найдено.")
    })
    @GetMapping
    public ResponseEntity<List<MaintenanceDTO>> getMaintenanceByEquipment(@RequestParam Long equipmentId) {
        List<MaintenanceDTO> list = maintenanceService.getMaintenanceByEquipment(equipmentId);
        return ResponseEntity.ok(list);
    }
}
