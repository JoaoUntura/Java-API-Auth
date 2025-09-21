package dev.joaountura.auth.exceptions.refreshTokenExceptions;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException() { super("Refresh token not found"); }
}
