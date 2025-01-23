package gym.Exception;

/**
 * Thrown when attempting to register a client who is already registered.
 */
public  class DuplicateClientException extends Exception {
    public DuplicateClientException(String message) {
        super(message);
    }
}
//The client is already registered for this lesson

