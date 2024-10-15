package org.example.exception;

public class AudioException extends RuntimeException {
    public AudioException(String message) {
        super(message);
    }

    public AudioException(String message, Throwable cause) {
        super(message, cause);
    }

    public AudioException(Class<?> clazz, String message, Throwable cause) {
        super(clazz.getSimpleName() + ": " + message, cause);
    }
}
