package cz.inspire.thesis.exceptions;

/**
 * A simple exception class for signaling creation errors.
 * Used for mimicking behavior of Beans business logic
 */
public class CreateException extends Exception {

    /**
     * Default constructor with no message or cause.
     */
    public CreateException() {
        super();
    }

    /**
     * Constructor with a custom message.
     *
     * @param message the detail message
     */
    public CreateException(String message) {
        super(message);
    }

    /**
     * Constructor with a custom message and a cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public CreateException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with a cause.
     *
     * @param cause the cause of the exception
     */
    public CreateException(Throwable cause) {
        super(cause);
    }
}
