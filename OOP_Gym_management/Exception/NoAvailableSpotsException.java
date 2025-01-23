package gym.Exception;

/**
 * Thrown when attempting to register a client to a session that has no available spots.
 */
public class NoAvailableSpotsException extends Exception {
    public NoAvailableSpotsException(String message) {
        super(message);
    }
}