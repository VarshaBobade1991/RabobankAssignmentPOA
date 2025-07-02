package nl.rabobank.exception;

public class DuplicateAccountTypeMappingException extends RuntimeException {
    public DuplicateAccountTypeMappingException(String message) {
        super(message);
    }
}
