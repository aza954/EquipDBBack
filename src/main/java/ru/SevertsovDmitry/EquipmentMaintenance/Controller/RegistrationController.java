package ru.SevertsovDmitry.EquipmentMaintenance.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.RoleRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.Repository.StaffRepository;
import ru.SevertsovDmitry.EquipmentMaintenance.models.RegistrationRequest;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Role;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Staff;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final StaffRepository staffRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(StaffRepository staffRepository,
                                  RoleRepository roleRepository,
                                  PasswordEncoder passwordEncoder) {
        this.staffRepository = staffRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Регистрация нового пользователя", description = "Создаёт нового пользователя в системе и назначает ему роль USER.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован."),
            @ApiResponse(responseCode = "400", description = "Ошибка регистрации, пользователь уже существует.")
    })
    @PostMapping
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
        if (staffRepository.findByName(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        Role userRole = roleRepository.findByRoleName("ADMIN")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName("USER");
                    newRole.setAccessLevel(1);
                    return roleRepository.save(newRole);
                });

        Staff staff = new Staff();
        staff.setName(request.getUsername());
        staff.setPassword(passwordEncoder.encode(request.getPassword()));
        staff.setPosition(request.getContact() == null ? "Employee" : request.getPosition());
        staff.setFirstName(request.getFirstName());
        staff.setLastName(request.getLastName());
        staff.setMiddleName(request.getMiddleName());
        staff.setContact(request.getContact() == null ? "NoContact" : request.getContact());
        staff.setRole(userRole);
        staffRepository.save(staff);
        return ResponseEntity.ok("User registered successfully");
    }
}
