package roles;
import courses.Course;

import java.time.LocalTime;
import java.util.Map;
import java.util.*;


/**
 * Admin class is used to store information of an admin user
 * @author Xuanhe Zhang, Huiwen Jia, Haoran Hua
 */
public class Admin extends UserBase{

    // constructor
    /**
     * admin constructor
     * @param id of admin
     * @param name of admin
     * @param username of admin
     * @param password of admin
     */
    public Admin(String id, String name, String username, String password) {
        super(id, name, username, password);
    }


    /**
     * add new course
     * @param id of the new course
     * @param name of the new course
     * @param professorId of the new course
     * @param weekDay of the new course
     * @param startTime of the new course
     * @param endTime of the new course
     * @param capacity of the new course
     * @return true when the new course being added successfully
     */
    public boolean addNewCourse(String id, String name, String professorId, String weekDay, LocalTime startTime, LocalTime endTime, int capacity){
        // if input is invalid, return false
        if(id == null || id.isEmpty() || name == null || name.isEmpty() || professorId == null || professorId.isEmpty() || weekDay == null || weekDay.isEmpty() || startTime == null || endTime == null){
            return false;
        }
        Professor professor = database.getProfessorById(professorId);
        // if professor doesn't exist, return false
        if(professor == null){
            System.out.println("Professor doesn't exist");
            return false;
        }
        // create new course
        Course course = new Course(id, name, professor, weekDay, startTime, endTime, capacity);

        List<Course> courseList = database.getProfessorCourseList(professorId);

        // traverse professor's course list
        for(Course c : courseList){
            // if new course has conflict with other course, return false
            if (course.conflictWith(c)){
                System.out.println("The new added course has time conflict with course: " +
                        c.getId() + "|" + c.getName() + ", " + c.getStartTime() + " on " + c.getWeekDay() + ", " +
                        " with course capacity: " + c.getCapacity() + ", " + "students: " + c.getStudents().size() + ", lecturer: Professor " +
                        c.getProfessor().getName());
                return false;
            }
        }


        // add new course into database
        return database.addCourse(course);
    }

    /**
     * delete course
     * @param courseId  course id
     * @return true if the operation is successful,false otherwise
     */
    public boolean deleteCourse(String courseId){
        // return false if input is invalid
        if(courseId == null || courseId.isEmpty()){
            return false;
        }
        return database.deleteCourse(courseId);
    }


    /**
     *  add new professor to the database
     * @param name  of the new professor
     * @param username  of the new professor
     * @param id of the new professor
     * @param password of the new professor
     * @return  true if the operation is successful, false otherwise
     */
    public boolean addNewProfessor(String name, String username, String id, String password){
        if(name == null || name.isEmpty() || username == null || username.isEmpty() || id == null || id.isEmpty() || password == null || password.isEmpty()){
            return false;
        }
        Professor professor = new Professor(id, name, username, password);
        return database.addUser(professor);
    }

    /**
     *  add new student to the database
     * @param name of the new student
     * @param username  of the new student
     * @param id of the new student
     * @param password of the new student
     * @return  true if the operation is successful, false otherwise
     */
    public boolean addNewStudent(String name, String username, String id, String password){
        if(name == null || name.isEmpty() || username == null || username.isEmpty() || id == null || id.isEmpty() || password == null || password.isEmpty()){
            return false;
        }
        Student student = new Student(id, name, username, password);
        return database.addUser(student);
    }

    /**
     *  remove the professor from the database with ID
     * @param id of the professor
     * @return true if the operation is successful, false otherwise
     */
    public boolean deleteProfessor(String id){
        // return false if input is invalid
        if(id == null || id.isEmpty()){
            return false;
        }
        return database.deleteProfessor(id);
    }

    /**
     *  remove the student from the database with ID
     * @param id of the student
     * @return  true if the operation is successful, false otherwise
     */
    public boolean deleteStudent(String id){
        // return false if input is invalid
        if(id == null || id.isEmpty()){
            return false;
        }
        return database.deleteStudent(id);
    }

    /**
     * override the default with admin + getname
     */
    @Override
    public String toString(){
        return "Admin: " + getName();
    }
}
