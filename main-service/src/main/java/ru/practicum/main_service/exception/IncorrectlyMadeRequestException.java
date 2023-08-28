package ru.practicum.main_service.exception;

public class IncorrectlyMadeRequestException extends RuntimeException {
    public IncorrectlyMadeRequestException(String message) {
        super(message);
    }
}
