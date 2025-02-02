package Users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Professor extends User{
    String office_hours = "3:00 - 4:00 PM";
    ArrayList<Course> prof_courses = new ArrayList<>();
    public Map<Course, Map<Student,Feedback<Object, Object>>> feedbackMap = new HashMap<>();
    public Professor(int id, String name, String username, String pass){
        super(id, name, username, pass);

    }
    public Professor(){}
    // public void update_courses() {
    //     List<Users.Course> toRemove = new ArrayList<>();
    //     for (Users.Course c : prof_courses) {
    //         if (course_exists(c.course_id) == null) {
    //             toRemove.add(c);
    //         }
    //     }
    //     prof_courses.removeAll(toRemove);  // Remove all invalid courses at once
    // }
    
    public Course course_fetch(){ //browse through all prof courses and choose one
        Scanner s = new Scanner(System.in);
        System.out.println("Choose a course from your courses: ");
        int i = 1;
        for(Course c: this.prof_courses){
        System.out.println((i++) + ". " + c);
        }
        while(true){
            System.out.print("Enter course ID: ");
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
            for(Course c: this.prof_courses){
                if (c.course_id == i) return c;
            }
            System.out.println("Wrong Course ID!");
        }
    }
    
    public void manage_course(){
        Scanner s = new Scanner(System.in);
        int i;
        Course manage = this.course_fetch();
        System.out.println("-------");
        manage.display_prof();
        System.out.println("-------");
        System.out.println("What do you want to change?");
        OUT:
        while(true){
            System.out.println("1. Update Syllabus,  2. Credits,  3. Class Schedule,  4. Office Hours,  5. Prerequisites,  6. Enrollment limit 7. Exit");
            while(true) {
                try {
                    String ss = s.nextLine();
                    i = Integer.parseInt(ss);
                    break;
                } catch (NumberFormatException e){
                    System.out.println("Enter correct index");
                    continue;
                }
            }
            switch (i) {
                case 1 -> {
                    System.out.println("Current syllabus: " + manage.syllabus);
                    while (true) { 
                        System.out.print("Add to syllabus(-1 to exit): ");
                        String syll = s.nextLine().strip();
                        if(syll.equals("-1")){
                            break;
                        }
                        manage.syllabus.add(syll);
                    }
                    System.out.println("Syllabus now: " + manage.syllabus);
                }                    
                case 2 -> {
                    System.out.print("Enter credits: ");
                    int cred;
                    while(true) {
                        try {
                            String ss = s.nextLine();
                            cred = Integer.parseInt(ss);
                            break;
                        } catch (NumberFormatException e){
                            System.out.println("Enter correct ID");
                            continue;
                        }
                    }
                    manage.cred = cred;
                }
                case 3 -> {
                    System.out.print("Enter class timings(-1 to not change): ");
                    String t = s.nextLine().strip();
                    if(!("-1".equals(t))) manage.class_timings = t;
                    System.out.print("Enter class venue(-1 to not change): ");
                    t = s.nextLine().strip();
                    if(!("-1".equals(t))) manage.venue = t;
                    System.out.println("Current weekly schedule: " + manage.weekly());
                    while(true){
                        System.out.println("Enter day number to schedule classes(124 -> Mon, Tue, Fri)(no input for no change): ");
                        /*
                         * if 124 entered, if there was class on day 1 i.e. Monday, there'll be no class on that day now
                         * And if there wasn't a class on Monday, now classes are scheduled weekly on Monday;
                         * Same for day 2(tuesday) and day 4(Thursday)
                         */
                        t = s.nextLine();
                        if(t.length() > 7) System.out.println("There are 7 days in a week!");
                        else{ 
                            for(Character ch : t.toCharArray()){
                                int day = ch - '1';
                                manage.days[day] = (manage.days[day] + 1)%2;
                            }
                            System.out.println("New schedule: " + manage.weekly() +"\nNeed to change?(y/n): ");
                            t = s.nextLine();
                            if("n".equalsIgnoreCase(t) || "no".equalsIgnoreCase(t)) break;
                        }
                    }
                }
                case 4 -> {
                    System.out.print("Enter Office hours: ");
                    this.office_hours = s.nextLine();
                }
                case 5 -> {
                    //make sure to add a valid course as the prereq is used in student methods
                    System.out.println("Current prerequisites: " + manage.prereq);
                    if(manage.sem == 1){
                        System.out.println("It is a 1st semester course(no prerequisites allowed)!");
                        break;
                    }
                OUTER:
                while (true) {
                    System.out.println("Do you want to 1. Add, 2. Remove, 3. Exit: ");
                    int action;
                    while(true) {
                        try {
                            String ss = s.nextLine();
                            action = Integer.parseInt(ss);
                            break;
                        } catch (NumberFormatException e){
                            System.out.println("Enter correct ID");
                            continue;
                        }
                    }
                    switch (action) {
                        case 1 -> {
                            int ss;
                            while(true){
                                while(true){
                                System.out.println("Choose a semester less than the course sem (" + manage.sem + ")");
                                    while(true) {
                                        try {
                                            String sss = s.nextLine();
                                            ss = Integer.parseInt(sss);
                                            break;
                                        } catch (NumberFormatException e){
                                            System.out.println("Enter correct ID");
                                            continue;
                                        }
                                    }
                                if(ss < manage.sem) break;
                                else System.out.println("Wrong semester!");
                            }
                            while(true){
                                System.out.println("Choose a course to add to prerequisites(Course ID): ");
                                display_sem_course(ss);
                                int cid;
                                while(true) {
                                    try {
                                        String sss = s.nextLine();
                                        cid = Integer.parseInt(sss);
                                        break;
                                    } catch (NumberFormatException e){
                                        System.out.println("Enter correct ID");
                                        continue;
                                    }
                                }
                                Course c = course_exists(cid);
                                if(c == null || !(courses.get(ss).contains(c))) System.out.println("Invalid course ID or course not in the chosen semester!");
                                else {
                                    manage.prereq.add(c);
                                    break;
                                }
                            }
                            System.out.print("Do you want to add more prerequisites? (y/n): ");
                            String ans = s.nextLine().strip();
                            if(ans.equalsIgnoreCase("No") || ans.equalsIgnoreCase("n")) break;
                        }
                        }
                        case 2 -> {
                            System.out.println("Choose a course ID to remove from prerequisite list: " + manage.prereq);
                            while (true) {
                                int cid;
                                while(true) {
                                    try {
                                        String ss = s.nextLine();
                                        cid = Integer.parseInt(ss);
                                        break;
                                    } catch (NumberFormatException e){
                                        System.out.println("Enter correct ID");
                                        continue;
                                    }
                                }
                                Course c = course_exists(cid);
                                if(c == null || !(manage.prereq.contains(c))) System.out.println("Invalid course ID or course not in the prereq list!");
                                else {
                                    manage.prereq.remove(c);
                                    break;
                                }
                            }
                        }
                        default -> {
                            break OUTER;
                        }
                    }
                }
                    System.out.println("Prerequisites now: " + manage.prereq);
                }
                case 6 -> {
                    System.out.print("Enter new limit: ");
                    int lim;
                    while(true) {
                        try {
                            String ss = s.nextLine();
                            lim = Integer.parseInt(ss);
                            break;
                        } catch (NumberFormatException e){
                            System.out.println("Enter correct ID");
                            continue;
                        }
                    }
                    manage.enrol_limit = lim;
                }
                case 7 -> {
                    break OUT;
                }
                default -> System.out.println("Enter correct number!");
            }
        }

    }

    public void view_stud(){
        Course view = this.course_fetch();
        System.out.println("List of enrolled students: " + view.stud_id);
    }

    public void view_feedback(){
        for(Course c: prof_courses){
            System.out.printf("%s ------\n", c.course_name);
            feedbackMap.putIfAbsent(c, new HashMap<>());
            for(Map.Entry<Student, Feedback<Object, Object>> feed : feedbackMap.get(c).entrySet()){
                System.out.println(feed.getValue());
            }
        }
    }
}
