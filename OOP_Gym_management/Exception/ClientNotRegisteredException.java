package gym.Exception;

/**
 * Thrown when trying to perform an operation on a client who is not registered in the gym.
 */
public class ClientNotRegisteredException extends Exception {
    public ClientNotRegisteredException(String s) {
        super(s);
    }

}

