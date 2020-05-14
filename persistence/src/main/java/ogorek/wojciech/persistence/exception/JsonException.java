package ogorek.wojciech.persistence.exception;

public class JsonException extends RuntimeException {
    private final String message;

    public JsonException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
