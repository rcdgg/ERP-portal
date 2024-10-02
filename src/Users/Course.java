package Users;

import java.time.LocalDate;
import java.util.ArrayList;

public class Course{
    //details
    String course_name;
    public int prof_id;
    ArrayList<Student> stud_id = new ArrayList<>();
    String prof_name;
    public int sem;
    int cred;
    int course_id;
    LocalDate deadline;
    ArrayList<Course> prereq;
    ArrayList<String> syllabus = new ArrayList<>();
    int[] days = {1,0,1,0,1,0,0}; //days of the week
    String venue = "LHC";
    String class_timings = "8:30 - 9:30 AM";
    int enrol_limit = 100;
    //---
    public Course(String name, int prof_id, String prof_name, int sem, int cred, int id, ArrayList<Course> prereq){
        this.course_name = name;
        this.prof_id = prof_id;
        this.prof_name = prof_name;
        this.sem = sem;
        this.cred = cred;
        this.course_id = id;
        this.prereq = prereq;
        deadline = LocalDate.now();
    }
    public ArrayList<String> weekly(){
        ArrayList<String> schedule = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            if(days[i] == 1){
                String dayName = switch (i + 1) {
                    case 1 -> "Monday";
                    case 2 -> "Tuesday";
                    case 3 -> "Wednesday";
                    case 4 -> "Thursday";
                    case 5 -> "Friday";
                    case 6 -> "Saturday";
                    case 7 -> "Sunday";
                    default -> "Unknown Day";
                };
                schedule.add(dayName);
            }
        }
        return schedule;
    }
    public void display(){
        System.out.println("Course Name: " + course_name);
        System.out.println("Prof Name: " + prof_name);
        System.out.println("Semester: " + sem);
        System.out.println("Credits: " + cred);
        System.out.println("Course ID: " + course_id);
        System.out.println("Syllabus: " + syllabus);
        System.out.println("prereq: " + prereq);
        System.out.println("Enrollment limit: " + enrol_limit + " (" + (enrol_limit - this.stud_id.size()) + " vacancies)");
        System.out.println("Venue: " + venue);
        System.out.println("Class Timings: " + class_timings);
        System.out.println("Weekly schedule: " + weekly());
        System.out.println("Add/drop deadline: " + deadline);
    }
    public void display_prof(){
        this.display();
        System.out.println("Students: " + stud_id); // print student name with id
    }
    public void display_stud(){
        System.out.println("Course Name: " + course_name);
        System.out.println("Prof Name: " + prof_name);
        Professor p = Admin.prof_exists(prof_id);
        assert p != null;
        System.out.println("Office hours: " + p.office_hours);
        System.out.println("Course ID: " + course_id);
        System.out.println("Syllabus: " + syllabus);
        System.out.println("Add/drop deadline: " + deadline);
        System.out.println("Class Timings: " + class_timings);
        System.out.println("Weekly schedule: " + weekly());
    }
    @Override
    public String toString(){
        return "(" + this.course_name + ", " + this.course_id + ")";
    }
}
