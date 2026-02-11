package com.salesianostriana.seguridad.user;

import com.salesianostriana.seguridad.security.jwt.JwtAccessTokenService;
import com.salesianostriana.seguridad.user.dto.LoginRequest;
import com.salesianostriana.seguridad.user.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtAccessTokenService jwtAccessTokenService;
    private final AuthenticationManager authManager;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> doLogin(@RequestBody LoginRequest loginRequest) {

        // Autenticamos al usuario con authManager.
        // Si la autenticación va bien, fabricamos el token y lo devolvemos

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(), loginRequest.password())
        );

        String token = jwtAccessTokenService.generateAccessToken(loginRequest.username());

        return ResponseEntity.status(201)
                .body(new LoginResponse(loginRequest.username(), token));

    }


}
