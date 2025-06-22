package ru.SevertsovDmitry.EquipmentMaintenance.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.MaintenanceService;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.MaintenanceDTO;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Maintenance;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MaintenanceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MaintenanceService maintenanceService;

    @InjectMocks
    private MaintenanceController maintenanceController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(maintenanceController).build();
    }

    @Test
    void createMaintenance_ShouldReturnCreatedDTO() throws Exception {
        MaintenanceDTO inputDTO = new MaintenanceDTO();
        MaintenanceDTO outputDTO = new MaintenanceDTO();

        when(maintenanceService.createMaintenance(any(MaintenanceDTO.class))).thenReturn(outputDTO);

        mockMvc.perform(post("/api/maintenance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void updateMaintenance_ShouldReturnUpdatedDTO() throws Exception {
        Long maintenanceId = 1L;
        MaintenanceDTO inputDTO = new MaintenanceDTO();
        MaintenanceDTO outputDTO = new MaintenanceDTO();

        when(maintenanceService.updateMaintenance(eq(maintenanceId), any(MaintenanceDTO.class))).thenReturn(outputDTO);

        mockMvc.perform(put("/api/maintenance/{maintenanceId}", maintenanceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void deleteMaintenanceById_ShouldReturnOk() throws Exception {
        Long maintenanceId = 1L;

        mockMvc.perform(delete("/api/maintenance/{maintenanceId}", maintenanceId))
                .andExpect(status().isOk());
    }

    @Test
    void getAllMaintenance_ShouldReturnList() throws Exception {
        List<Maintenance> maintenanceList = Collections.singletonList(new Maintenance());

        when(maintenanceService.getAllMaintenance()).thenReturn(maintenanceList);

        mockMvc.perform(get("/api/maintenance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
