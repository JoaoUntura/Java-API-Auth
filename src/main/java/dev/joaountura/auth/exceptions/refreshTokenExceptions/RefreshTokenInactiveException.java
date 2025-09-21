package dev.joaountura.auth.exceptions.refreshTokenExceptions;

public class RefreshTokenInactiveException extends RuntimeException {
    public RefreshTokenInactiveException() { super("Refresh token not active"); }
}
