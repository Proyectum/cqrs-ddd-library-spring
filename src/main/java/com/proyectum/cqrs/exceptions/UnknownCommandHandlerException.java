package com.proyectum.cqrs.exceptions;

public class UnknownCommandHandlerException extends RuntimeException {

    public UnknownCommandHandlerException(String message) {
        super(message);
    }
}
