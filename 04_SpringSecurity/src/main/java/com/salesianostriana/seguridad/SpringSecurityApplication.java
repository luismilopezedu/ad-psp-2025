package com.salesianostriana.seguridad;

import com.salesianostriana.seguridad.user.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@SpringBootApplication
public class SpringSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

    @Bean
    CommandLineRunner cmd(UserRepository repository) {
        return args -> {

            String secretString = "qpwioeruqpiweoruqwioruqweioruqewioruqepworuqweoruqpwoeruqweioruqewu";
            String username = "user";
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
            String token = Jwts.builder()
                    .subject(username)
                    .issuedAt(new Date())
                    .expiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                    //.signWith(key, SignatureAlgorithm.HS512)
                    .signWith(key)
                    .compact();
            System.out.println("Token JWT generado para el usuario '" + username + "': " + token);
        };
    }



}
