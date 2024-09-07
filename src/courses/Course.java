package courses;

import roles.Professor;
import roles.Student;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Course class is used to store information of a course
 * @author Xuanhe Zhang, Huiwen Jia, Haoran Hua
 */
public class Course {
	
	// instance variables
    private final String id;
    private final String name;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final String weekDay;
    private final Professor professor;
    private final List<Student> students;
    private final int capacity;

    // constructor
    /**
     * initialize all variables for object course
     * @param id of the course
     * @param name of the course
     * @param professor taking this course
     * @param weekDay is when the course is held each week
     * @param startTime of the course
     * @param endTime of the course
     * @param capacity of the course
     */
    public Course(String id, String name, Professor professor, String weekDay, LocalTime startTime, LocalTime endTime, int capacity){
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.weekDay = weekDay;
        this.professor = professor;
        this.capacity = capacity;
        this.students = new ArrayList<>();
    }

    /**
     * get the name of the course
     * @return course name
     */
    public String getName() {
        return name;
    }

    /**
     * get the id of the course
     * @return course ID
     */
    public String getId() {
        return id;
    }

    /**
     * get the start time of the course
     * @return the start time
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * get the end time of the course
     * @return the end time
     */
    public LocalTime getEndTime(){
        return endTime;
    }

    /**
     * get the days of held courses
     * @return the days
     */
    public String getWeekDay() {
        return weekDay;
    }

    /**
     * get the professor holding the course
     * @return the professor of the course
     */
    public Professor getProfessor() {
        return professor;
    }

    /**
     * get the list of students who taking this course
     * @return the list of students
     */
    public List<Student> getStudents() {
        return students;
    }

    /**
     * get the capacity of the class
     * @return the class capacity
     */
    public int getCapacity(){
        return capacity;
    }

    /**
     * add student to the list
     * @param student  that is going to add to the list
     * @return is student added
     */
    public boolean addStudent(Student student){
        // if input is invalid, return false
        if(student == null){
            return false;
        }

    	// if the list, students, has student in it, student cannot be added
        if(students.contains(student)){
            return false;
        }
        
        // else, add the student
        students.add(student);
        return true;
    }

    /**
     * remove student from the list
     * @param student that is going to be removed from list
     * @return is student removed
     */
    public boolean removeStudent(Student student){
        if(student == null){
            return false;
        }
        return students.remove(student);
    }

    /**
     * if the course is conflicted with another course, return false
     * @param course that might be conflicted
     * @return true if there is no conflict between courses
     */
    public boolean conflictWith(Course course){

    	// if the day that is going to have the course is unequal to another, then there is no conflict
        if(!hasSameWeekDay(course)){
            return false;
        }

        LocalTime thisStart = this.startTime;
        LocalTime thisEnd = this.endTime;
        LocalTime otherStart = course.getStartTime();
        LocalTime otherEnd = course.getEndTime();

        if( (thisStart.isBefore(otherStart) || thisStart.equals(otherStart)) && (thisEnd.isAfter(otherEnd) || thisEnd.equals(otherEnd)) ){
            return true;
        }
        if( (thisStart.isAfter(otherStart) || thisStart.equals(otherStart)) && (thisEnd.isBefore(otherEnd) || thisEnd.equals(otherEnd)) ){
            return true;
        }

        if ( (thisStart.isBefore(otherStart) || thisStart.equals(otherStart)) && (thisEnd.isAfter(otherStart) || thisEnd.equals(otherEnd)) ){
            return true;
        }

        if ( (otherStart.isBefore(thisStart) || otherStart.equals(thisStart)) && (otherEnd.isAfter(thisStart) || otherEnd.equals(thisEnd)) ){
            return true;
        }


        // for other cases, return false
        return false;
    }

    /**
     * check if the courses has the same date
     * @param other course
     * @return true if the courses have the same date
     */
    private boolean hasSameWeekDay(Course other){
        // traverse weekday string
        for(int i = 0; i < this.weekDay.length(); i++){
            for(int j = 0; j < other.weekDay.length(); j++){
                if(this.weekDay.charAt(i) == other.weekDay.charAt(j)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * show the complete course information
     */
    @Override
    public String toString(){
        return id + "|" + name + ", " + startTime + "-" + endTime + " on " + weekDay +
                ", with course capacity: " + capacity + "students: " + getStudents().size();
    }
}
