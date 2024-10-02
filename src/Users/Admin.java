package Users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import Users.Exceptions.*;

public class Admin extends User implements Complaints{
    public static ArrayList<Professor> prof_list = new ArrayList<>();
    public static ArrayList<ArrayList<Student>> stud_list = new ArrayList<>();
    public static ArrayList<TA> ta_list = new ArrayList<>();
    Scanner sc = new Scanner(System.in);
    int stud_id_counter = 1000;
    int prof_id_counter = 100;
    public Admin(){
        super(-1, "admin","admin","-1"); //ID and pass of admin is -1
        for(int i = 0; i < 10; i++){ //last array for ppl who cleared college
            courses.add(new ArrayList<>());
            stud_list.add(new ArrayList<>());
        }
        // 3 professors
        prof_list.add(new Professor(prof_id_counter++, "Sanjit Kaul", "sk", "sk"));
        prof_list.add(new Professor(prof_id_counter++, "Vivek Kumar", "vk", "vk"));
        prof_list.add(new Professor(prof_id_counter++, "Rohan", "rohan", "rohan"));
        // 3 students
        stud_list.get(1).add(new Student(stud_id_counter++, "Rohan", "rohan", "rohan"));
        stud_list.get(1).get(0).num = "9191919191";
        stud_list.get(1).add(new Student(stud_id_counter++, "Rahul", "rahul", "rahul"));
        stud_list.get(2).add(new Student(stud_id_counter++, "Alex", "alex", "alex"));
        stud_list.get(2).get(0).num = "1234567890";
        stud_list.get(2).get(0).sem = 2;
        // 5 courses
        Professor sanjit = prof_list.get(0);
        Professor vivek = prof_list.get(1);
        Professor rohan = prof_list.get(2);
        
        Course la = new Course("LA", sanjit.id, sanjit.name, 1, 4, 101, new ArrayList<>());
        Course dc = new Course("DC", vivek.id, vivek.name, 1, 4, 102, new ArrayList<>());
        courses.get(1).add(la);
        courses.get(1).add(dc);
        stud_list.get(2).get(0).completed_courses.add(la);
        //---
        ArrayList<Course> prereq = new ArrayList<>();
        prereq.add(dc);
        courses.get(2).add(new Course("OS", vivek.id, vivek.name, 2, 4, 201, prereq));
        courses.get(2).add(new Course("CP", rohan.id, rohan.name, 2, 2, 200, new ArrayList<>()));
        prereq = new ArrayList<>();
        prereq.add(la);
        courses.get(2).add(new Course("RA", sanjit.id, sanjit.name, 2, 4, 202, prereq));
        //---
        sanjit.prof_courses.add(courses.get(1).get(0));
        sanjit.prof_courses.add(courses.get(2).get(2));

        vivek.prof_courses.add(courses.get(1).get(1));
        vivek.prof_courses.add(courses.get(2).get(0));

        rohan.prof_courses.add(courses.get(2).get(1));
    }

    public final static Professor prof_exists(int prof_id){
        for(Professor p: prof_list){
            if(p.id == prof_id) return p;
        }
        return null;
    }
    
    public final static Student stud_exists(int stud_id){
        for(ArrayList<Student> semester: stud_list){
            for(Student c: semester){
                if(c.id == stud_id){
                    return c;
                }
            }
        }
        return null;
    }
    
    public void del_course(){
        Scanner s = new Scanner(System.in);
        int sem;
        Course course;
        while (true) { 
            System.out.printf("Enter sem number: ");
            sem = s.nextInt();
            if(sem < 1 || sem > courses.size() || courses.get(sem).isEmpty()){
                if(courses.get(sem).isEmpty()) System.out.println("No courses available this semester!");
                else System.out.println("Wrong semester, try again!");
            }
            else break;
        }
        System.out.println("Semester " + sem + " courses: ");
        this.display_sem_course(sem);

        int ind;
        while (true) { 
            System.out.printf("choose a course to remove(course ID): ");
            ind = s.nextInt();
            course = course_exists(ind);
            if(course == null){
                System.out.println("Wrong course ID, try again!");
            }
            else break;
        }
        courses.get(sem).remove(course);
        Course remove  = course;
        //update course list for prof and student
        for(Student stud: remove.stud_id){
            stud.stud_courses.remove(remove);
            stud.grades.remove(remove.course_id);
            if(stud.update_current()){
                stud_promote(stud);
            }
        }
        prof_exists(remove.prof_id).prof_courses.remove(remove);
        //--------
        for(ArrayList<Course> semester: courses){ //removing course from prerequisites
            for(Course cour: semester){
                cour.prereq.remove(remove);
            }
        }
        System.out.println("Semester " + sem + " courses: ");
        this.display_sem_course(sem);
        System.out.println(course + " removed");
    }

    public void add_course(){
        String course_name = "";
        int prof_id = 0;
        int sem = 0;
        int cred = 0;
        int course_id = 0;
        ArrayList<Course> prereq = new ArrayList<>();
    
        Scanner s = new Scanner(System.in);

        //course name
        System.out.printf("Course name: ");
        course_name = s.nextLine();
        //prof id
        while (true) { 
            System.out.printf("Professor id: ");
            prof_id = s.nextInt(); 
            Professor p = prof_exists(prof_id);
            if(p != null) break;
            else System.out.println("Wrong ID! Try again.");
        }
        //sem number
        while (true) { 
            System.out.printf("Enter sem number: ");
            sem = s.nextInt();
            if(sem < 1 || sem > courses.size()){
                System.out.println("Wrong sem! try again.");
            }
            else break;
        }
        //credits
        while (true) { 
            System.out.printf("Enter credits: ");
            cred = s.nextInt();
            if(cred != 2 && cred != 4){
                System.out.println("A course cant be of " + cred + " credits!");
            }
            else break;
        }
        //course id
        while (true) { 
            System.out.printf("Enter a unique course ID: ");
            course_id = s.nextInt();
            if(course_exists(course_id) == null) break;          
            else System.out.println("Course Id already exists!");
        }
        //prereq
        System.out.println("Prerequisites: ");
        OUT:
        while(true){
            if(sem == 1){
                System.out.println("1st semester course can't have prerequisites");
                break;
            }
            int ss = 0;
            while(true){
                System.out.println("Choose a semester less than this course sem (" + sem + ")(-1 to exit)");
                ss = s.nextInt();
                if(ss == -1) break OUT;
                if(ss < sem) break;
                else System.out.println("Wrong semester!");
            }
            
            while(true){
                System.out.println("Choose a course to add to prerequisites(Course ID): ");
                display_sem_course(ss);
                int cid = s.nextInt();
                Course c = course_exists(cid);
                if(c == null || !(courses.get(ss).contains(c))) System.out.println("Invalid course ID or course not in the chosen semester!");
                else {
                    prereq.add(c);
                    break;
                }
            }
            System.out.printf("Do you want to add more prerequisites? (y/n): ");
            String ans = s.nextLine().strip();
            if(ans.equalsIgnoreCase("No") || ans.equalsIgnoreCase("n")) break;
        }
        Professor p = prof_exists(prof_id);// did not add course to prof above in case we decide to stop adding a course
        Course add = new Course(course_name, prof_id, p.name, sem, cred, course_id, prereq);
        courses.get(sem).add(add); //added to course pool
        p.prof_courses.add(add); //adding course to prof

        //no need to add to students because they will have a choice to choose the course
    }

    public void change_prof(){
        int course_id;
        int prof_id;
        Course c;
        Professor p;
        Scanner s = new Scanner(System.in);
        while(true){
            System.out.printf("Enter course ID: ");
            course_id = s.nextInt();
            c = course_exists(course_id);
            if(c == null) System.out.println("Not a valid course ID!");
            else break;
        }
        while(true){
            System.out.printf("Enter Professor ID: ");
            prof_id = s.nextInt();
            p = prof_exists(prof_id);
            if(p == null) System.out.println("Not a valid Professor ID!");
            else break;
        }
        Professor q = prof_exists(c.prof_id);
        c.prof_id = prof_id;
        c.prof_name = p.name;
        p.prof_courses.add(c);
        for(int i = 0; i < q.prof_courses.size(); i++){
            Course temp = q.prof_courses.get(i);
            if(temp.equals(c)){
                q.prof_courses.remove(i);
                break;
            }
        }
    }

    public void stud_manage(){
        System.out.println("List of students: ");
        for(ArrayList<Student> sem: stud_list){
            if(sem.isEmpty()) continue;
            for(Student s: sem){
                System.out.println(s);
            }
        }
        Scanner s = new Scanner(System.in);
        Student stud;
        System.out.println("Choose a student(by ID): ");
        while(true){
            int temp = s.nextInt();
            s.nextLine();
            stud = stud_exists(temp);
            if(stud == null) System.out.println("Invalid student ID!");
            else break;
        }
        stud.display();
        System.out.println("---------");
        //when a student passes move him to the next sem slot in list and update the course lists
        System.out.println("Choose an action: ");
        while(true){
            System.out.println("1. Update Name  2. Update Number  3. Update grade");
            int i = s.nextInt();
            s.nextLine();
            switch (i) {
                case 1 -> {
                	s.nextLine();
                    String na = s.nextLine();
                    stud.name = na;
                    stud.display();
                }
                    
                case 2 -> {
                	s.nextLine();
                    String na = s.nextLine();
                    stud.num = na;
                    stud.display();
                }
                case 3 -> {
                    if(stud.stud_courses.isEmpty()){
                        System.out.println("No courses chosen yet for this semester!");
                        break;
                    }
                    System.out.println("Choose a course ID: " + stud.stud_courses);
                    int t = s.nextInt();
                    s.nextLine();
                    System.out.printf("Choose a Grade(integer between 0 - 10): ");
                    int val = (int) s.nextFloat();
                    for(int ii = 0; ii < stud.stud_courses.size(); ii++){
                        if(stud.stud_courses.get(ii).course_id == t){
                            stud.grades.put(t, val);
                            break;
                        }
                    }
                    if(stud.update_current()){
                        stud_promote(stud);
                    }
                }
                
                default -> System.out.println("Not a valid action!");
            }
            s.nextLine();
            System.out.println("Edit more? (y/n): ");
            String ans = s.nextLine();
            if(ans.equalsIgnoreCase("n") || ans.equalsIgnoreCase("No")) break;
        }
    }
    
    public void stud_promote(Student s){
        boolean done = false;
        for(int i = 1; i < 9; i++){
            for(Student stud: stud_list.get(i)){
                if(stud.equals(s)){
                    done = true;
                    if(stud_list.get(i).remove(s)) stud_list.get(i+1).add(s);
                    break;
                }
            }
            if(done) break;
        }
    }

    protected ArrayList<String> view_resolved_complaints(int stud_id){
        if(complaints.containsKey(stud_id)){
            return complaints.get(stud_id).get(1);
        }
        else return null;
        
    }

    
    protected ArrayList<String> view_pending_complaints(int stud_id){
        if(complaints.containsKey(stud_id)){
            return complaints.get(stud_id).get(0);
        }
        else return null; 
    }

    @Override
    public void view_complaints(){
        for(ArrayList<Student> sem: stud_list){
            for(Student stud: sem){
                if(complaints.containsKey(stud.id)){
                    System.out.println(stud);
                    stud.view_complaints();
                    System.out.println();
                }
            }
        }
        Scanner s = new Scanner(System.in);
        OUTER:
        while (true) {
            System.out.printf("Filter by 1. Pending, 2. Resolved, 3. exit: ");
            int view = s.nextInt();
            switch (view) {
                case 1 -> {
                    System.out.println("Pending complaints: ");
                    for(ArrayList<Student> sem: stud_list){
                        for(Student stud: sem){
                            if(complaints.containsKey(stud.id)){
                                System.out.println(stud.id + ": " + stud.view_pending_complaints());
                            }
                        }
                    }   
                    s.nextLine();
                    while(true){
                        System.out.printf("Enter Student ID and index number of complaint(1-based) to take action on it(enter -1 to take no action): ");
                        //example input: 101 2 (101 is student id and 2 is the index of the complaint(2nd complaint in the list))
                        String inp = s.nextLine();
                        inp = inp.strip();
                        if(inp.equals("-1".strip())) break;
                        else{
                            String[] input = inp.split(" ");
                            int sid = Integer.parseInt(input[0]);
                            int ind = Integer.parseInt(input[1]);
                            
                            if((stud_exists(sid) != null) && (ind - 1 < complaints.get(sid).get(0).size())){
                                resolve_complaint(sid, ind - 1, s);
                            }
                            else System.out.println("Invalid input(check student ID or index again!)");
                        }
                    }
                }
                case 2 -> {
                    System.out.println("Resolved complaints: ");
                    for(ArrayList<Student> sem: stud_list){
                        for(Student stud: sem){
                            if(complaints.containsKey(stud.id)){
                                System.out.println(stud.id + ": " + stud.view_resolved_complaints());
                            }
                        }
                    }
                }
                default -> {
                    break OUTER;
                }
            }
        }
    }

    private void resolve_complaint(int sid, int index, Scanner s){
        System.out.println("Type solution: ");
        String sol = s.nextLine();
        String fin = complaints.get(sid).get(0).get(index) + ": " + sol;
        complaints.get(sid).get(0).set(index, fin);
        complaints.get(sid).get(1).add(complaints.get(sid).get(0).remove(index));
    }

    public Student fetch_stud(ArrayList<String> cred) throws InvalidLoginException {
        for(ArrayList<Student> semester: stud_list){
            for(Student s: semester){
                ArrayList<String> crede = new ArrayList<>(Arrays.asList(s.credentials));
                if(crede.equals(cred)){
                    return s;
                }
            }
        }
        throw new InvalidLoginException();
    }
    
    public Student add_stud(ArrayList<String> cred){
        Student s = new Student(stud_id_counter++, cred.get(2), cred.get(0), cred.get(1));  
        stud_list.get(1).add(s);
        return s;
    }

    public Professor fetch_prof(ArrayList<String> cred) throws InvalidLoginException {
        for(Professor p: prof_list){
            ArrayList<String> crede = new ArrayList<>(Arrays.asList(p.credentials));
            if(crede.equals(cred)){
                return p;
            }
        }
        throw new InvalidLoginException();    }
    
    public Professor add_prof(ArrayList<String> cred){
        Professor p = new Professor(prof_id_counter++, cred.get(2), cred.get(0), cred.get(1));  
        prof_list.add(p);
        return p;
    }

    public TA fetch_TA(ArrayList<String> cred) throws InvalidLoginException {
        for(TA ta: ta_list){
            ArrayList<String> crede = new ArrayList<>(Arrays.asList(ta.which_stud.credentials));
            if(crede.equals(cred)) return ta;
        }
        throw new InvalidLoginException();    }

    public void add_TA(TA stud){
        ta_list.add(stud);
    }

    private void inputDeadline(int sem) {
        System.out.printf("Enter new Add/drop deadline for Semester %d (yyyy-mm-dd): ", sem);

        try {
            String input = sc.nextLine();
            LocalDate deadline = LocalDate.parse(input);
            for(Course c: courses.get(sem)){
                c.deadline = deadline;
            }
            System.out.println("Deadline set successfully: " + deadline);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format!");
        }
    }

    public void change_add_drop(){
        System.out.print("Choose a semester: ");
        int sem = sc.nextInt();
        sc.nextLine();
        while(sem > 9 || sem < 1){
            System.out.println("Not a valid course!");
            System.out.print("Choose a semester: ");
            sem = sc.nextInt();
            sc.nextLine();
        }
        inputDeadline(sem);
    }
}
