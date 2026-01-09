package com.salesianostriana.dam.modelado.excepciones;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
