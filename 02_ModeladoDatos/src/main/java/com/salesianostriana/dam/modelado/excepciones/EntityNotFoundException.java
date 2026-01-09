package com.salesianostriana.dam.modelado.excepciones;

public class EntityNotFoundException extends RuntimeException{

    public EntityNotFoundException(String message) {
        super(message);
    }

}
