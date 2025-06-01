package ru.SevertsovDmitry.EquipmentMaintenance.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.SevertsovDmitry.EquipmentMaintenance.Service.StaffService;
import ru.SevertsovDmitry.EquipmentMaintenance.models.LoginRequest;
import ru.SevertsovDmitry.EquipmentMaintenance.models.Staff;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final StaffService staffService;

    public AuthController(AuthenticationManager authenticationManager, StaffService staffService) {
        this.authenticationManager = authenticationManager;
        this.staffService = staffService;
    }

    @Operation(summary = "Авторизация пользователя", description = "Аутентифицирует пользователя по логину и паролю и устанавливает сессию.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешная авторизация."),
            @ApiResponse(responseCode = "401", description = "Ошибка аутентификации, неверные данные.")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        Staff staff = staffService.getStaffByUsername(loginRequest.getUsername());

        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            session.setAttribute("USERID", staff.getStaffId());

            Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
            sessionCookie.setPath("/");
            sessionCookie.setHttpOnly(true);
            sessionCookie.setMaxAge(60 * 60 * 24); // 24 часа

            Cookie userIdCookie = new Cookie("USERID", String.valueOf(staff.getStaffId()));
            userIdCookie.setPath("/");
            userIdCookie.setHttpOnly(false);
            userIdCookie.setMaxAge(60 * 60 * 24);

            response.addCookie(sessionCookie);
            response.addCookie(userIdCookie);

            return ResponseEntity.ok("Login successful");
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }





    @Operation(summary = "Выход пользователя из системы", description = "Удаляет сессию пользователя и очищает контекст безопасности.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешный выход."),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован.")
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("SPRING_SECURITY_CONTEXT") != null) {
            return ResponseEntity.ok("OK");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
    }

}
