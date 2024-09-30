import java.util.ArrayList;
import java.util.Scanner;

public class TA extends Student{
    ArrayList<Course> courses_TA = new ArrayList<>();
    public TA(int id, String name, String username, String pass){
        super(id, name, username, pass);
        is_TA = true;
    }
    public void manage_TA_course(){
        Scanner s = new Scanner(System.in);
        System.out.println("Course list: " + courses_TA);
        System.out.print("Choose a course by ID: ");
        int c_id = s.nextInt();
        s.nextLine();
        Course c = course_exists(c_id);
        System.out.println("----");
        c.display_prof();
        System.out.println("----");
        while(true){
            System.out.print("Student list: " + c.stud_id + "\nChoose student by ID to update grades: ");
            int s_id = s.nextInt();
            s.nextLine();
            Student stud = new Student();
            for(Student i : c.stud_id){
                if(i.id == s_id){
                    stud = i;
                    break;
                }
            }
            System.out.println("\n1. View current grade\n2. Change grade");
            int i = 
        }
    }
    
}
