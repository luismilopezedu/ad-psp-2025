package com.salesianostriana.dam.monumentos.error;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
        super("Entidad no encontrada");
    }

}
