package exeptions

public class MissingRequirementException extends RuntimeException {
    public MissingRequirementException(String message) {
        super(message);
    }
}