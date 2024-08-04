package com.proyectum.cqrs.exceptions;

public class UnknownQueryHandlerException extends RuntimeException {

    public UnknownQueryHandlerException(String message) {
        super(message);
    }
}
