package Users;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import Users.Exceptions.*;

public class Student extends User implements Complaints{
    Scanner s = new Scanner(System.in);
    String num = "";
    public int sem = 1;
    int cred = 0;
    double sgpa = 0;
    double cgpa = 0;
    public boolean is_TA = false;
    public ArrayList<Course> stud_courses = new ArrayList<>();
    public ArrayList<Course> completed_courses = new ArrayList<>();
    Map<Integer, Integer> grades = new HashMap<>(); //maps course id to grade: -1 = no grade, > 4 = pass, <= 4  =  fail
    public Student(int id, String name, String username, String pass){
        super(id, name, username, pass);
    }
    public Student(){}
    public void display(){
        System.out.println("Name: " + name);
        System.out.println("ID: " + id);
        System.out.println("Semester: " + sem);
        System.out.println("Phone no.: " + num);
        System.out.println("Courses: " + stud_courses);
    }
    @Override
    public String toString(){
        return "(" + this.name + ", " + this.id + ")";
    }

    public void display_sem_course(){
        display_sem_course(sem);
    }

	private void reset(){ //when sem changes
        this.completed_courses.addAll(this.stud_courses);
        for(Course c: this.stud_courses){ //removing student from the courses they have cleared
            c.stud_id.remove(this);
        }
        this.stud_courses.clear();
        this.sem++;
    }

    public void register() throws CourseFullException, DeadlinePassedException, InvalidCourseRegistrationException {
        //add in course stud list as well
        System.out.println("---");
        if(cred >= 20){
            System.out.println("You must drop another course since the credit quota is full!");
            return;
        }
        Course c;
        while(true) {
            System.out.println("Choose from available courses(Enter Course ID): ");
            this.display_sem_course();
            int reg_id;
            while(true) {
                try {
                    String ss = s.nextLine();
                    reg_id = Integer.parseInt(ss);
                    break;
                } catch (NumberFormatException e){
                    System.out.println("Enter correct ID");
                    continue;
                }
            }
            c = course_exists(reg_id);
            if(c == null) System.out.println("Wrong course ID!");
            else break;
        }
        //---------- exception checking
        {
            if (c.enrol_limit - c.stud_id.size() <= 0) {
                throw new CourseFullException();
            } else if (!(this.completed_courses.containsAll(c.prereq))) {
                throw new InvalidCourseRegistrationException("Prerequisites incomplete!");
            } else if (this.cred + c.cred > 20) {
                throw new InvalidCourseRegistrationException("Overload not allowed!");
            } else if (this.completed_courses.contains(c)) {
                throw new InvalidCourseRegistrationException("Course already completed!");
            } else if (this.stud_courses.contains(c)) {
                throw new InvalidCourseRegistrationException("Course already added!");
            } else if (c.deadline.isBefore(LocalDate.now())){
                throw new DeadlinePassedException();
            }
        }
        //----------

        this.stud_courses.add(c);
        c.stud_id.add(this);
        this.grades.put(c.course_id, -1);
        this.cred += c.cred;
        System.out.println("---");
    }

    public void schedule(){
        int i = 1;
        System.out.println("---");
        for(Course c: stud_courses){
            System.out.println(i++ + ". ");
            c.display_stud();
        }
        System.out.println("---");
    }

    public boolean drop() throws DeadlinePassedException {
        //remember to reduce the cred score as well and also not allow to drop if grade already given
        System.out.println("---");
        System.out.println("Choose from available courses to drop(Enter Course ID): " + this.stud_courses);
        if(stud_courses.isEmpty()){
            System.out.println("No courses registered yet!");
            return false;
        }
        Course c;
        while(true) {
            int drop_id;
            while(true) {
                try {
                    String ss = s.nextLine();
                    drop_id = Integer.parseInt(ss);
                    break;
                } catch (NumberFormatException e){
                    System.out.println("Enter correct ID");
                    continue;
                }
            }
            c = course_exists(drop_id);
            if(c == null) System.out.println("Wrong Course ID!");
            else break;
        }
        if(grades.get(c.course_id) != -1 || c.deadline.isBefore(LocalDate.now())){
            System.out.println("-------");
            throw new DeadlinePassedException();
        }

        this.stud_courses.remove(c);
        c.stud_id.remove(this);
        this.cred -= c.cred;
        grades.remove(c.course_id);
        System.out.println("---");
        if(this.stud_courses.isEmpty()) return false;
        return this.update_current();
    }

    public void track(){
        System.out.println("---");
        for(int i = 1; i <= this.sem; i++){
            for(Course c: courses.get(i)){
                if(this.stud_courses.contains(c) || this.completed_courses.contains(c)){
                    String grade = (grades.get(c.course_id) > 4) ? "PASS" : "FAIL";
                    System.out.printf("ID: %d, Name: %s, Grade: %d (%s)\n", c.course_id, c.course_name, grades.get(c.course_id), grade);
                }
            }
        }
        System.out.println("---------\nCurrent GPA");
        System.out.printf("SGPA: %.1f   CGPA: %.1f\n", this.sgpa, this.cgpa);
        System.out.println("---");
    }

    protected boolean update_current(){ //only accessible by student and admin, returns true if student passes
        boolean done = true;
        double gpa = 0;
        int count = 0;
        int crdt = 0;
        for(Course c: this.stud_courses){
            int grade = grades.get(c.course_id);
            crdt += c.cred;
            if(grade <= 4){ //no grade assigned yet or fail
                done = false;
            }
            if(grade > -1) {gpa+= grade; count++;}
            
        }
//        if(crdt < 16) done = false;
        if(count == 0) this.sgpa = 0;
        else this.sgpa = gpa/count;

        if(done){
            System.out.println("Semester " + this.sem + " cleared.");
            this.cgpa = this.cgpa * this.completed_courses.size() + gpa;
            this.reset();
            this.cgpa /= this.completed_courses.size();
            System.out.println("CGPA: " + this.cgpa);
            this.sgpa = 0;
        }
        return done;
    }

    protected ArrayList<String> view_resolved_complaints(){
        if(complaints.containsKey(this.id)){
            return complaints.get(this.id).get(1);
        }
        else return null;
        
    }
    
    protected ArrayList<String> view_pending_complaints(){
        if(complaints.containsKey(this.id)){
            return complaints.get(this.id).getFirst();
        }
        else return null; 
    }
    
    @Override
    public void view_complaints(){
        if(complaints.containsKey(this.id)){
            ArrayList<String> pending = this.view_pending_complaints();
            ArrayList<String> resolved = this.view_resolved_complaints();
            System.out.println("Pending complaints: " + pending);
            System.out.println("Resolved complaints: " + resolved);
        }
        else System.out.println("No complaints have been submitted");
    }

    public void add_complaint(){
        System.out.print("Write your complaint: ");
        Scanner s = new Scanner(System.in);
        String com = s.nextLine();
        if(complaints.containsKey(this.id)){
            complaints.get(this.id).getFirst().add(com);
        }
        else{
            ArrayList<ArrayList<String>> c = new ArrayList<>(2);
            c.add(new ArrayList<>());
            c.add(new ArrayList<>());
            c.getFirst().add(com);
            complaints.put(this.id, c);
        }
    }

    public ArrayList<Object> feedback(){
        if(this.completed_courses.isEmpty()){
            System.out.println("No completed courses yet!");
            return null;
        }
        System.out.println("Choose from completed courses(Course ID): " + this.completed_courses);
        Course c;
        while(true){
            int c_id;
            while(true) {
                try {
                    String ss = s.nextLine();
                    c_id = Integer.parseInt(ss);
                    break;
                } catch (NumberFormatException e){
                    System.out.println("Enter correct ID");
                    continue;
                }
            }
            c = course_exists(c_id);
            if(c == null) System.out.println("Wrong course ID!\n" + "Choose from completed courses(Course ID): " + this.completed_courses);
            else break;
        }
        ArrayList<Object> rtrn = new ArrayList<>(2);
        System.out.println("Enter rating and/or comments:" );
        System.out.print("1. ");
        Object one = s.nextLine().strip();
        System.out.print("\n2. ");
        Object two = s.nextLine().strip();
        Feedback<Object, Object> f = new Feedback<>(one, two);
        rtrn.add(c);
        rtrn.add(f);
        return rtrn;
    }

}