package Users.Exceptions;

public class DeadlinePassedException extends RuntimeException {
    public DeadlinePassedException() {
        super("Deadline for Add/drop has passed!");
    }
}
