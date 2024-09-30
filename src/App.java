import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class App {
    //close the Scanner object here only after everything else is executed because we need to keep the System.in channel open for every other function
    static Admin admin = new Admin();   
    static Scanner s = new Scanner(System.in);
    public static void main(String[] args) throws Exception {       
        
        while(true) run();

    }

    private static ArrayList<String> login(User user){
        return user.login();
    }
    private static ArrayList<String> signup(User user){
        return user.signup();
    }
    private static void run(){
        System.out.println("=======Welcome to ERP Portal=========");
        while(true){
            System.out.println("Login as:\n1. Student\n2. Professor\n3. Admin\n4. Exit");
            int action = s.nextInt();
            s.nextLine();
            if(action == 4){
                System.out.println("Closing portal...");
                System.out.println("=====================================\n");
                s.close();
                System.exit(0);
            }
            else if(action > 0 && action < 4) {
                App.run_sign_page(action);
                break;
            }
            else System.out.println("Enter correct number!");
        }
    }

    private static void run_sign_page(int mode){
        ArrayList<String> cred;       
        switch (mode) {
            case 1 -> {
                Student stud = new Student();
                System.out.println("1. Sign in\n2. Sign up\n");
                int l = s.nextInt();
                s.nextLine();
                if(l == 1){
                    while(true){
                        cred = login(stud);
                        stud = admin.fetch_stud(cred);
                        if(stud == null) {
                            System.out.println("Wrong username or password! Try again");
                            stud = new Student();
                        }
                        else break;
                    }
                }
                else{
                    cred = signup(stud);
                    stud = admin.add_stud(cred);
                }
                run_stud(stud);
            }
            case 2 -> {
                Professor prof = new Professor();
                System.out.println("1. Sign in\n2. Sign up\n");
                int l = s.nextInt();
                s.nextLine();
                if(l == 1){
                    while(true){
                        cred = login(prof);
                        prof = admin.fetch_prof(cred);
                        if(prof == null) {
                            System.out.println("Wrong username or password! Try again");
                            prof = new Professor();
                        }
                        else break;
                    }
                }
                else{
                    cred = signup(prof);
                    prof = admin.add_prof(cred);
                }
                run_prof(prof);
            }
            case 3 -> {
            	while(true) {
            		cred = login(admin);
            		ArrayList<String> crede = new ArrayList<>(Arrays.asList(admin.credentials));
            		if(cred.equals(crede)) break;
            		else System.out.println("Wrong username or password! Try again");
            	}
                run_admin();
            }
            default -> throw new AssertionError();
        }
    }

    private static void run_stud(Student stud){
        OUT:
        while(true){
            System.out.printf("===========Welcome, %s============\n", stud.name);
            System.out.println("Choose Action: ");
            System.out.println("1. View Available Courses:\n2. Register for Courses:\n3. View Schedule:\n4. Track Academic Progress:\n5. Drop Courses:\n6. View Complaints:\n7. Submit Complaints:\n8. Change Password:\n9. Logout");
            int action = s.nextInt();
            switch (action) {
                case 1 -> {
                    System.out.printf("--- Semester %d --- \n", stud.sem);
                    stud.display_sem_course();
                    System.out.println("---");
                }
                case 2 -> {
                    stud.register();
                }
                case 3 -> {
                    stud.schedule();
                }
                case 4 -> {
                    stud.track();
                }
                case 5 -> {
                    if(stud.drop()){
                        admin.stud_promote(stud);
                    }
                }
                case 6 -> {
                    System.out.println("---");
                    stud.view_complaints();
                    System.out.println("---");
                }
                case 7 -> {
                    System.out.println("---");
                    stud.add_complaint();
                    System.out.println("---");
                }
                case 8 -> {
                    System.out.println("---");
                    stud.changepass();
                    System.out.println("---");
                }
                case 9 -> {
                    System.out.println("Logging out..");
                    break OUT;
                }
                default -> System.out.println("Enter correct number!");
            }
        }
    }
    
    private static void run_prof(Professor prof){
        OUT:
        while(true){
            System.out.printf("===========Welcome, %s============\n", prof.name);
            System.out.println("Choose Action: ");
            System.out.println("1. Manage your courses:\n2. View enrolled students:\n3. Change Password:\n4. Logout");
            int action = s.nextInt();
            s.nextLine();
            switch (action) {
                case 1 -> {
                    System.out.println("---");
                    prof.manage_course();
                    System.out.println("---");
                }
                case 2 -> {
                    System.out.println("---");
                    prof.view_stud();
                    System.out.println("---");
                }
                case 3 -> {
                    System.out.println("---");
                    prof.changepass();
                    System.out.println("---");
                }
                case 4 -> {
                    System.out.println("Logging out..");
                    break OUT;
                }
                default -> System.out.println("Enter correct number!");
            }
        }
    }
    
    private static void run_admin(){
        OUT:
        while(true){
            System.out.println("===========Welcome, Admin============\n");
            System.out.println("Choose Action: ");
            System.out.println("1. Add a course:\n2. Delete a course:\n3. Update student records and grades:\n4. Change a course professor\n5. Handle complaints\n6. Logout");
            int action = s.nextInt();
            switch (action) {
                case 1 -> {
                    System.out.println("---");
                    admin.add_course();
                    System.out.println("---");
                }
                case 2 -> {
                    System.out.println("---");
                    admin.del_course();
                    System.out.println("---");
                }
                case 3 -> {
                    System.out.println("---");
                    admin.stud_manage();
                    System.out.println("---");
                }
                case 4 -> {
                	System.out.println("---");
                    admin.change_prof();
                    System.out.println("---");
                }
                case 5 -> {
                	System.out.println("---");
                    admin.view_complaints();
                    System.out.println("---");
                }
                
                case 6 -> {
                    System.out.println("Logging out..");
                    break OUT;
                }
                default -> System.out.println("Enter correct number!");
            }
        }
    }
}

