package com.salesianostriana.seguridad.security.auth;

import com.salesianostriana.seguridad.security.jwt.JwtAccessTokenService;
import com.salesianostriana.seguridad.user.dto.LoginRequest;
import com.salesianostriana.seguridad.user.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtAccessTokenService jwtAccessTokenService;

    public LoginResponse doLogin(LoginRequest loginRequest) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(), loginRequest.password())
        );

        String token = jwtAccessTokenService.generateAccessToken(loginRequest.username());

        return new LoginResponse(loginRequest.username(), token);

    }


}
