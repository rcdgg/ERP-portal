package Users;
/*  app
*       user
*       course
*           student
*           prof
*           admin
 */

import java.util.ArrayList;
import java.util.Scanner;


public abstract class User {
    static ArrayList<ArrayList<Course>> courses = new ArrayList<>();
    public int id;
    public String name = "";
    public String[] credentials = new String[2];
    public User(int id, String name, String username, String pass){
        this.id = id;
        this.name = name;
        this.credentials[0] = username;
        this.credentials[1] = pass;
    }
    public User(){}
    public void display_sem_course(int sem){
    	int i = 1;
        System.out.println();
        for(Course c: courses.get(sem)){   
        	System.out.println((i++) + ".");
            c.display();
            System.out.println();
        }
    }
    public final static Course course_exists(int course_id){
        for(ArrayList<Course> semester: courses){
            for(Course c: semester){
                if(c.course_id == course_id){
                    return c;
                }
            }
        }
        return null;
    }
    public void view_courses() {
    	for(int i = 1; i < 9; i++) {
            System.out.println("-------Sem " + i + "------------");
    		if(courses.get(i).isEmpty()) System.out.println("No courses yet!");
            else this.display_sem_course(i);
    	}
    }
    public ArrayList<String> login(){       
        String username; String pass;
        ArrayList<String> cred = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Username: ");
        username = scanner.nextLine().strip();
        System.out.println("Password: ");
        pass = scanner.nextLine().strip();
        cred.add(username); cred.add(pass);
        
        return cred;
    }
    public ArrayList<String> signup(){
        String username; String pass; String namee;
        ArrayList<String> cred = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name: ");
        namee = scanner.nextLine().strip();
        System.out.println("---");
        System.out.println("Choose Username: ");
        username = scanner.nextLine().strip();
        System.out.println("Choose Password: ");
        pass = scanner.nextLine().strip();
        cred.add(username); cred.add(pass); cred.add(namee);
        System.out.println("---");
        return cred;
    }

    public void changepass(){
        System.out.println("Enter new password: ");
        Scanner scanner = new Scanner(System.in);
        String pass = scanner.nextLine().strip();
        this.credentials[1] = pass;
    }
}
