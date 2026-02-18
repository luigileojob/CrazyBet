package com.CrazyBet.Controller;

import com.CrazyBet.Security.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        String password = request.get("password");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        String role = authentication.getAuthorities().iterator().next().getAuthority();
        if(role.startsWith("ROLE_")) {
            role = role.substring(5); // rimuove "ROLE_" se presente
        }

        String token = jwtService.generateToken(email, role);

        return Map.of("token", token);
    }

}
