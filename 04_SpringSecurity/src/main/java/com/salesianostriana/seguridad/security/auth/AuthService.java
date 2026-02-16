package com.salesianostriana.seguridad.security.auth;

import com.salesianostriana.seguridad.security.jwt.JwtAccessTokenService;
import com.salesianostriana.seguridad.user.UserRepository;
import com.salesianostriana.seguridad.user.dto.LoginRequest;
import com.salesianostriana.seguridad.user.dto.LoginResponse;
import com.salesianostriana.seguridad.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtAccessTokenService jwtAccessTokenService;
    private final UserRepository userRepository;

    public LoginResponse doLogin(LoginRequest loginRequest) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(), loginRequest.password())
        );

        // Rescatar al usuario por username para obtener su id
        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + loginRequest.username()));


        String token = jwtAccessTokenService.generateAccessToken(user);

        return new LoginResponse(loginRequest.username(), token);

    }


}
