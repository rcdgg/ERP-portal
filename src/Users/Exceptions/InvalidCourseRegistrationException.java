package Users.Exceptions;

public class InvalidCourseRegistrationException extends RuntimeException {
    public InvalidCourseRegistrationException(String message) {
        super(message);
    }
}
