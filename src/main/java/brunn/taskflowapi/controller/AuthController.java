package brunn.taskflowapi.controller;

import brunn.taskflowapi.dto.login.LoginRequestDTO;
import brunn.taskflowapi.dto.login.LoginResponseDTO;
import brunn.taskflowapi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {


    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponseDTO> userLogin(@RequestBody @Valid LoginRequestDTO login) {
        return ResponseEntity.ok(service.login(login));
    }
}
