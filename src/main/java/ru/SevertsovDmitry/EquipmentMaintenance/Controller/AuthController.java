package ru.SevertsovDmitry.EquipmentMaintenance.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.StaffService;
import ru.SevertsovDmitry.EquipmentMaintenance.config.Security.JwtTokenProvider;
import ru.SevertsovDmitry.EquipmentMaintenance.models.DTO.JwtResponse;
import ru.SevertsovDmitry.EquipmentMaintenance.models.LoginRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final StaffService staffService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthenticationManager authenticationManager,
                          StaffService staffService,
                          JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.staffService = staffService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Operation(summary = "Авторизация пользователя", description = "Аутентифицирует пользователя по логину и паролю и возвращает JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешная авторизация."),
            @ApiResponse(responseCode = "401", description = "Ошибка аутентификации, неверные данные.")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwtToken = jwtTokenProvider.generateToken(authentication);

            return ResponseEntity.ok(new JwtResponse(jwtToken));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.ok("OK");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
    }
}
