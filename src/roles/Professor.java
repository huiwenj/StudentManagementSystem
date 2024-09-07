package roles;

import courses.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Admin class is used to store information of an admin user
 * @author Xuanhe Zhang, Huiwen Jia, Haoran Hua
 */
public class Professor extends UserBase{
    /**
     *  constructor
     * @param id of the professor
     * @param name of the professor
     * @param username of the professor
     * @param password of the professor
     */
    public Professor(String id, String name, String username, String password) {
        super(id, name, username, password);
    }

    /**
     *  view all courses
     * @return the list of all courses
     */
    public List<Course> getGivenCourses(){
        return database.getCoursesByProfessor(this.getId());
    }

    /**
     *  view all students of a given course
     * @param courseId of the course
     * @return the list of all students of the given course
     */
    public List<Student> getStudentListOfGivenCourse(String courseId){
        if(courseId == null || courseId.isEmpty()){
            return new ArrayList<>();
        }
        Course course = database.getCourseById(courseId);
        if(course == null){
            return new ArrayList<>();
        }
        return course.getStudents();
    }

    /**
     * override by giving professor + get their name
     */
    @Override
    public String toString(){
        return "Professor: " + getName();
    }

}
