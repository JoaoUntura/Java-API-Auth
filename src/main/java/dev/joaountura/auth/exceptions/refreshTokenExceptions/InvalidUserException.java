package dev.joaountura.auth.exceptions.refreshTokenExceptions;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException() { super("Invalid user for this refresh token"); }
}
