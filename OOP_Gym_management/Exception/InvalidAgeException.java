package gym.Exception;

public class InvalidAgeException extends Exception {/**
 * Thrown when a client's age is invalid for gym registration.
 */
    public InvalidAgeException(String message) {
        super(message);
    }
}

