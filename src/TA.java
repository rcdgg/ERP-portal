import java.util.ArrayList;
import java.util.Scanner;

public class TA extends Student{
    ArrayList<Course> courses_TA = new ArrayList<>();
    public TA(int id, String name, String username, String pass){
        super(id, name, username, pass);
        is_TA = true;
    }
    public boolean manage_TA_course(){ //if true returned => Student passed semester and admin will call stud_promote
        boolean return_val = false;
        Scanner s = new Scanner(System.in);
        System.out.println("Course list: " + courses_TA);
        boolean valid = false;
        Course c = null;
        while(!valid) {
            System.out.print("Choose a course by ID: ");
            int c_id = s.nextInt();
            s.nextLine();
            try {
                c = course_exists(c_id);
                assert c != null : "Wrong course ID!\n";
                valid = true;
            } catch (AssertionError e) {
                System.out.println("Try again!");
            }
        }
        System.out.println("----");
        c.display_prof();
        System.out.println("----");
        OUT:
        while(true){
            System.out.println("Student list: " + c.stud_id);
            valid = false;
            Student stud = new Student();
            while(true) {
                System.out.print("Choose student by ID to update grades: ");
                int s_id = s.nextInt();
                s.nextLine();
                stud = new Student();
                for (Student i : c.stud_id) {
                    if (i.id == s_id) {
                        stud = i;
                        valid = true;
                        break;
                    }
                }
                if(!valid) System.out.println("Wrong student ID!");
                else break;
            }
            System.out.println("\n1. View current grade\n2. Change grade\n 3. Exit");
            int i = s.nextInt();
            s.nextLine();
            switch (i) {
                case 1 -> {
                    System.out.printf("%s : %d\n", c.course_name, stud.grades.get(c.course_id));
                }
                case 2 -> {
                    System.out.print("Choose a Grade(integer between 0 - 10): ");
                    int val = (int) s.nextFloat();
                    stud.grades.put(c.course_id, val);
                    return_val = stud.update_current();
                }
                case 3 -> {
                    break OUT;
                }
                default -> throw new IllegalStateException("Unexpected value: " + i);
            }
        }
        return return_val;
    }
    
}
