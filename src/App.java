import Users.*;
import Users.Exceptions.DeadlinePassedException;
import Users.Exceptions.InvalidLoginException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

//**use setters/ getters maybe for accessing the static variables that i did

public class App {
    //close the Scanner object here only after everything else is executed because we need to keep the System.in channel open for every other function
    static Admin admin = new Admin();
    static Scanner s = new Scanner(System.in);
    public static void main(String[] args) {

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
            System.out.println("Login as:\n1. Student\n2. Professor\n3. Admin\n4. TA\n5. Exit");
            String act;
            int action;
            while(true) {
                try {
                    act = s.nextLine().strip();
                    action = Integer.parseInt(act);
                    break;
                } catch (Exception e) {
                    System.out.println("Enter a number!");
                }
            }
            if(action == 5){
                System.out.println("Closing portal...");
                System.out.println("=====================================\n");
                s.close();
                System.exit(0);
            }
            if(action == 4 && Admin.ta_list.isEmpty()) System.out.println("No TAs currently available!");
            else if(action > 0 && action < 5) {
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
                String act;
                int action;
                while(true) {
                    try {
                        act = s.nextLine().strip();
                        action = Integer.parseInt(act);
                        break;
                    } catch (Exception e) {
                        System.out.println("Enter a number!");
                    }
                }
                if(action == 1){
                    while(true){
                        cred = login(stud);
                        try {
                            stud = admin.fetch_stud(cred);
                            break;
                        } catch (InvalidLoginException e) {
                            System.out.println(e.getMessage());
                        }
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
                String act;
                int action;
                while(true) {
                    try {
                        act = s.nextLine().strip();
                        action = Integer.parseInt(act);
                        break;
                    } catch (Exception e) {
                        System.out.println("Enter a number!");
                    }
                }
                if(action == 1){
                    while(true){
                        cred = login(prof);
                        try {
                            prof = admin.fetch_prof(cred);
                            break;
                        } catch (InvalidLoginException e) {
                            System.out.println(e.getMessage());
                        }
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
            case 4 ->{
                TA ta = new TA();
                System.out.println("Sign in - ");

                while(true) {
                    cred = login(ta);
                    try {
                        ta = admin.fetch_TA(cred);
                        break;
                    } catch (InvalidLoginException e) {
                        System.out.println(e.getMessage());
                    }
                }
                run_TA(ta);
            }
            default -> throw new AssertionError();
        }
    }

    private static void run_stud(Student stud){
        OUT:
        while(true){
            System.out.printf("===========Welcome, %s============\n", stud.name);
            System.out.println("Choose Action: ");
            System.out.println("1. View Available Courses:\n2. Register for Courses:\n3. View Schedule:\n4. Track Academic Progress:\n5. Drop Courses:\n6. View Complaints:\n7. Submit Complaints:\n8. Submit Feedback\n9. Change Password:\n10. Logout");
            String act;
            int action;
            while(true) {
                try {
                    act = s.nextLine().strip();
                    action = Integer.parseInt(act);
                    break;
                } catch (Exception e) {
                    System.out.println("Enter a number!");
                }
            }
            switch (action) {
                case 1 -> {
                    System.out.printf("--- Semester %d --- \n", stud.sem);
                    stud.display_sem_course();
                    System.out.println("---");
                }
                case 2 -> {
                    try {
                        stud.register();
                    } catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                case 3 -> stud.schedule();
                case 4 -> stud.track();
                case 5 -> {
                    try{
                        boolean promo = stud.drop();
                        if(promo){
                            admin.stud_promote(stud);
                        }
                    } catch (DeadlinePassedException e){
                        System.out.println(e.getMessage());
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
                    ArrayList<Object> f = stud.feedback();
                    if(f == null){
                        System.out.println("---");
                        break;
                    }
                    Course c = (Course) f.getFirst();
                    Feedback<Object, Object> feed = (Feedback<Object, Object>) f.getLast();
                    Professor p = Admin.prof_exists(c.prof_id);
                    assert p != null;
                    p.feedbackMap.putIfAbsent(c, new HashMap<>());
                    p.feedbackMap.get(c).put(stud, feed);
                    System.out.println("---");
                }
                case 9 -> {
                    System.out.println("---");
                    stud.changepass();
                    System.out.println("---");
                }
                case 10 -> {
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
            System.out.println("1. Manage your courses:\n2. Choose TA for your course\n3. View enrolled students:\n4. View Feedback\n5. Change Password:\n6. Logout");
            String act;
            int action;
            while(true) {
                try {
                    act = s.nextLine().strip();
                    action = Integer.parseInt(act);
                    break;
                } catch (Exception e) {
                    System.out.println("Enter a number!");
                }
            }
            switch (action) {
                case 1 -> {
                    System.out.println("---");
                    prof.manage_course();
                    System.out.println("---");
                }
                case 2 ->{
                    TA ta = null;
                    System.out.println("---");
                    Course c = prof.course_fetch();
                    System.out.println("Possible TA candidates: ");
                    ArrayList<Student> tas = new ArrayList<>();
                    for(int i = c.sem + 1; i < 9; i++){
                            for(Student s: Admin.stud_list.get(i)){
                                if(s.completed_courses.contains(c)) tas.add(s);
                        }
                    }
                    System.out.println(tas);
                    if(tas.isEmpty()){
                        System.out.println("No candidates!");
                        break;
                    }
                    while(true){
                        System.out.print("Enter Student ID: ");
                        boolean done = false;
                        int i;
                        while(true) {
                            try {
                                String ss = s.nextLine();
                                i = Integer.parseInt(ss);
                                break;
                            } catch (NumberFormatException e){
                                System.out.println("Enter correct ID");
                                continue;
                            }
                        }
                        for(Student stud: tas){
                            if(stud.id == i){
                                if(!stud.is_TA){
                                    ta = new TA(stud);
                                    stud.is_TA = true;
                                    ta.courses_TA.add(c);
                                    admin.add_TA(ta);
                                } else {
                                    for(TA hh: Admin.ta_list){
                                        if(hh.which_stud.equals(stud)){
                                            ta = hh;
                                            break;
                                        }
                                    }
                                    assert ta != null;
                                    ta.courses_TA.add(c);
                                }
                                done = true;
                                break;
                            }
                        }
                        if(done){
                            System.out.println(ta);
                            break;
                        }
                        else{
                            System.out.println("Enter correct ID!\n" + tas);
                        }
                    }
                    System.out.println("---");
                }
                case 3 -> {
                    System.out.println("---");
                    prof.view_stud();
                    System.out.println("---");
                }
                case 4 ->{
                    System.out.println("---");
                    prof.view_feedback();
                    System.out.println("---");
                }
                case 5 -> {
                    System.out.println("---");
                    prof.changepass();
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
    
    private static void run_admin(){
        OUT:
        while(true){
            System.out.println("===========Welcome, Admin============\n");
            System.out.println("Choose Action: ");
            System.out.println("1. Add a course:\n2. Delete a course:\n3. Update student records and grades:\n4. Change a course professor\n5. Handle complaints\n6. Change Add/drop deadline\n7. Logout");
            String act;
            int action;
            while(true) {
                try {
                    act = s.nextLine().strip();
                    action = Integer.parseInt(act);
                    break;
                } catch (Exception e) {
                    System.out.println("Enter a number!");
                }
            }
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
                case 6 ->{
                    System.out.println("---");
                    admin.change_add_drop();
                    System.out.println("---");
                }
                case 7 -> {
                    System.out.println("Logging out..");
                    break OUT;
                }
                default -> System.out.println("Enter correct number!");
            }
        }
    }

    private static void run_TA(TA ta){
        OUT:
        while(true){
            System.out.printf("===========Welcome, %s============\n", ta.which_stud.name);
            System.out.println("Choose Action: ");
            System.out.println("1. Manage your courses:\n2. Logout");
            String act;
            int action;
            while(true) {
                try {
                    act = s.nextLine().strip();
                    action = Integer.parseInt(act);
                    break;
                } catch (Exception e) {
                    System.out.println("Enter a number!");
                }
            }
            switch (action) {
                case 1 -> {
                    System.out.println("---");
                    ta.manage_TA_course(admin);
                    System.out.println("---");
                }
                case 2 -> {
                    System.out.println("Logging out..");
                    break OUT;
                }
                default -> System.out.println("Enter correct number!");
            }
        }
    }
}

