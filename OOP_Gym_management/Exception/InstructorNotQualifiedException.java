package gym.Exception;

/**
 * Thrown when an instructor attempts to conduct a session they are not qualified for.
 */
public class InstructorNotQualifiedException extends Exception {
    public InstructorNotQualifiedException(String message) {
        super(message);
    }
}
