package com.salesianostriana.dam.monumentos.error;

public class MonumentoNotFoundException extends RuntimeException{

    public MonumentoNotFoundException() {
        super("Monumento no encontrado");
    }

    public MonumentoNotFoundException(String message) {
        super(message);
    }

    public MonumentoNotFoundException(Long id) {
        super("No se ha encontrado el monumento con id %d".formatted(id));
    }
}
