package nl.rabobank.exception;

public class DuplicatePoaException extends RuntimeException {
    public DuplicatePoaException(String message) {
        super(message);
    }
}
