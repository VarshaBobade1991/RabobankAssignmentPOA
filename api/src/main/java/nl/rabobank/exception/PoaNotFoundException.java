package nl.rabobank.exception;


public class PoaNotFoundException extends RuntimeException{
    public PoaNotFoundException(String message) {
        super(message);
    }
}
