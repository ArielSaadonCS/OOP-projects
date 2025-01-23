package gym.Exception;

/**
 * Thrown when attempting to register a client to a session they are already registered for.
 */
public class DuplicateLessonRegistrationException extends Exception {
    public DuplicateLessonRegistrationException(String message) {
        super(message);
    }
}
