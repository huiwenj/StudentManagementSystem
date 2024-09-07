package processor;

import courses.Course;
import database.Database;
import constant.Constant;
import files.FileInfoReader;
import roles.*;

import java.util.Map;

/**
 * process level of the management system
 * @author Xuanhe Zhang, Huiwen Jia, Haoran Hua
 */
public class Processor {

    // instance variables
    private User user;
    private final Database database;
    FileInfoReader fileInfoReader;

    // constructor
    public Processor(){
        this.database = Database.getInstance();
        fileInfoReader = new FileInfoReader();
        fileInfoReader.readFiles();
    }
    
    /**
     *  user authentication
     * @param type of the user
     * @param username  of the user
     * @param password  of the user
     * @return  true if the user could successfully login, false otherwise
     */
    public boolean login(String type, String username, String password){
        // return false if input is invalid
        if(type == null || username == null || password == null){
            return false;
        }

        // if the user is an admin, check if the password is correct        
        if(type.equals(Constant.ADMIN_TYPE)){
            Admin admin = database.getAdminByUsername(username);
            if(admin != null && admin.getPassword().equals(password)){
                user = admin;
                return true;
            }
        // if the user is a professor, check if the password is correct
        } else if (type.equals(Constant.PROFESSOR_TYPE)){
            Professor professor = database.getProfessorByUsername(username);
            if(professor != null && professor.getPassword().equals(password)){
                user = professor;
                return true;
            }

        // if the user is a student, check if the password is correct
        } else if (type.equals(Constant.STUDENT_TYPE)){
            Student student = database.getStudentByUsername(username);
            if(student != null && student.getPassword().equals(password)){
                user = student;
                return true;
            }
        }
        return false;
    }

    /**
     * getter for the user
     * @return  the user
     */    
    public User getUser(){
        return this.user;
    }

    /**
     * get all courses information
     * @return all courses information
     */
    public Map<String, Course> getAllCourses(){
        return database.getAllCourses();
    }

    /**
     * get course by its id
     * @param courseId of the course
     * @return the course
     */
    public Course getCourseById(String courseId){
        if(courseId == null || courseId.isEmpty()){
            return null;
        }
        return database.getCourseById(courseId);
    }

    /**
     * get student by their id
     * @param studentId of student
     * @return the student
     */
    public Student getStudentById(String studentId){
        if(studentId == null || studentId.isEmpty()){
            return null;
        }
        return database.getStudentById(studentId);
    }

    

}
