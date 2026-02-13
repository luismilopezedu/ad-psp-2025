package com.salesianostriana.seguridad.security.jwt;

import com.salesianostriana.seguridad.user.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtAccessTokenService jwtService;
    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = getJwtAccessTokenFromRequest(request);

        try {
            if (StringUtils.hasText(token) && jwtService.validateAccessToken(token)) {
                String username = jwtService.getUsernameFromAccessToken(token);

                userRepository.findByUsername(username).ifPresentOrElse(user -> {
                    UsernamePasswordAuthenticationToken
                            authenticationToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );

                    authenticationToken.setDetails(
                            new WebAuthenticationDetails(request)
                    );

                    log.info("Usuario autenticado correctamente: " + username);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                }, () -> {;
                    // Caso raro de que un usuario tenga un token válido
                    // pero no encontremos su username en la base de datos
                    throw new UsernameNotFoundException("User not found with username: " + username);
                });

            }

            // El caso de pasar al bloque catch por una excepción, no encadenamos el siguiente filtro
            filterChain.doFilter(request, response);


        } catch (JwtException ex) {
            throw new RuntimeException(ex);
        }


    }

    private String getJwtAccessTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtAccessTokenService.TOKEN_HEADER);
        // Bearer asfkñaldsjfslk.asñklfdjadlsñfajs.asñkjdfaksdñlfjal
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtAccessTokenService.TOKEN_PREFIX)) {
            return bearerToken.substring(JwtAccessTokenService.TOKEN_PREFIX.length());
        }

        return null;
    }


}
