package dev.joaountura.auth.exceptions.refreshTokenExceptions;

public class RefreshTokenExpiredException extends RuntimeException {
    public RefreshTokenExpiredException() { super("Refresh token expired"); }
}
