public class CourseFullException extends RuntimeException {
    public CourseFullException() {
        super("No vacancies left!");
    }
}
