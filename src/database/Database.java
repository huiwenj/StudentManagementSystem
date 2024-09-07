package database;

import courses.Course;
import roles.Admin;
import roles.Professor;
import roles.Student;
import roles.User;

import java.util.*;

/**
 * Store data for the management system.
 * This class acts as a centralized database for storing and managing
 * courses, students, professors, and administrators.
 * @author Xuanhe Zhang, Huiwen Jia, Haoran Hua
 */
public class Database {
	
	// instance variables
	
	/**
	 * each stores course, student, professor, and admin objects
	 * keyed by strings, such as course ID, student ID, etc.
	 */

    // map for search course or users efficiently
    private Map<String, Course> courseMap;
    private Map<String, Student> studentMap;
    private Map<String, Professor> professorMap;
    private Map<String, Admin> adminMap;
    // map for convert username to id efficiently
    private Map<String, String> studentUsernameToId;
    private Map<String, String> professorUsernameToId;
    private Map<String, String> adminUsernameToId;
    // map for record each professor's course list
    private Map<String, List<Course>> professorCourseList;

    /**
     * private constructor: makes sure only one database exists throughout the system
     */
    private static Database instance = null;

    /**
     * prevents other methods create new instances of database
     */
    private Database(){
    }

    /**
     * provides global access to single Database instance
     * @return instance of database
     */
    public static Database getInstance(){
    	// if no instance exists, create one and initialize maps
        if(instance == null){
            instance = new Database();
            instance.courseMap = new LinkedHashMap<>();
            instance.studentMap = new HashMap<>();
            instance.adminMap = new HashMap<>();
            instance.professorMap = new HashMap<>();
            instance.studentUsernameToId = new HashMap<>();
            instance.professorUsernameToId = new HashMap<>();
            instance.adminUsernameToId = new HashMap<>();
            instance.professorCourseList = new HashMap<>();
        }
        return instance;
    }

    public static void reset(){
        instance = null;
    }

    /**
     * adding course to courseMap
     * @param course to be added
     * @return true if successfully add course
     */
    public boolean addCourse(Course course){
        // if course is null, return false
        if(course == null){
            return false;
        }

        // if the course is conflicted with another course, return false
        if(courseMap.containsKey(course.getId())){
            return false;
        }
        // get professor of new course
        Professor courseProfessor = course.getProfessor();
        if(courseProfessor == null){
            System.out.println("Professor is null");
            return false;
        }
        //String professorId = courseProfessor.getId();
        // get course list from professor
        List<Course> courseList = professorCourseList.getOrDefault(courseProfessor.getId(), new ArrayList<>());

        courseMap.put(course.getId(), course);
        courseList.add(course);
        professorCourseList.put(courseProfessor.getId(), courseList);
        return true;
    }

    /**
     * get the professor course list from the professor ID
     * @param professorId of the professor
     * @return the course list
     */
    public List<Course> getProfessorCourseList(String professorId){
        if(professorId == null || professorId.isEmpty()){
            return new ArrayList<>();
        }
        return professorCourseList.getOrDefault(professorId, new ArrayList<>());
    }

    /**
     * delete courses through courseID
     * @param courseId of the course being deleted
     * @return the courseMap that has course removed
     */
    public boolean deleteCourse(String courseId){
        // return false if input is invalid
        if(courseId == null || courseId.isEmpty()){
            return false;
        }
        // remove course from database
        return courseMap.remove(courseId) != null;
    }

    /**
     * return the courseMap of courses
     * @return courseMap of courses
     */
    public Map<String, Course> getCourseMap(){
        return courseMap;
    }

    /**
     *  return the professorMap of professors
     * @return  the professorMap of professors
     */
    public Map<String, Professor> getProfessorMap(){
        return professorMap;
    }

    /**
     *  return the studentMap of students
     * @return  the studentMap of students
     */
    public Map<String, Student> getStudentMap(){
        return studentMap;
    }

    /**
     *  return the adminMap of administrators
     * @return  the adminMap of administrators
     */
    public Map<String, Admin> getAdminMap(){
        return adminMap;
    }

    /**
     * print out the map of courses
     */
    public Map<String, Course> getAllCourses(){
        return courseMap;
    }

    /**
     * Add a user to the appropriate map based on their type (Student, Professor, Admin).
     * @param user The user to be added.
     * @return True if the user is successfully added, false if the user already exists.
     */
    public boolean addUser(User user){
        // return false if input is invalid
        if(user == null){
            return false;
        }
        if(user instanceof Student) {
            if (studentMap.containsKey(user.getId())) {
                System.out.printf("Add new %s failed: duplicated ID\n", user);
                return false;
            }
            if(studentUsernameToId.containsKey(user.getUsername())){
                System.out.printf("Add new %s failed: duplicated Username\n", user);
                return false;
            }
            studentMap.put(user.getId(), (Student) user);
            studentUsernameToId.put(user.getUsername(), user.getId());
        }
        else if(user instanceof Professor){
            if (professorMap.containsKey(user.getId())) {
                System.out.printf("Add new %s failed: duplicated ID\n", user);
                return false;
            }
            if(professorUsernameToId.containsKey(user.getUsername())){
                System.out.printf("Add new %s failed: duplicated Username\n", user);
                return false;
            }
            professorMap.put(user.getId(), (Professor) user);
            professorUsernameToId.put(user.getUsername(), user.getId());
        }
        else if(user instanceof Admin){
            if (adminMap.containsKey(user.getId())) {
                System.out.printf("Add new %s failed: duplicated ID\n", user);
                return false;
            }
            if(adminUsernameToId.containsKey(user.getUsername())){
                System.out.printf("Add new %s failed: duplicated Username\n", user);
                return false;
            }
            adminMap.put(user.getId(), (Admin) user);
            adminUsernameToId.put(user.getUsername(), user.getId());
        }
        return true;
    }


    /**
     * get courses by their IDs
     * @param id of the course
     * @return the course object
     */
    public Course getCourseById(String id){
        // return null if input is invalid
        if(id == null || id.isEmpty()){
            return null;
        }
        return courseMap.getOrDefault(id, null);
    }

    /**
     * get professors by their IDs
     * @param id of professors
     * @return the professor object
     */
    public Professor getProfessorById(String id){
        // return null if input is invalid
        if(id == null || id.isEmpty()){
            return null;
        }
        return professorMap.getOrDefault(id, null);
    }

    /**
     * Retrieve a professor by their name.
     * @param username The name of the professor.
     * @return The professor object if found, null otherwise.
     */
    public Professor getProfessorByUsername(String username){
        // return null if input is invalid
        if(username == null || username.isEmpty()){
            return null;
        }
        // get professor id
        String id = professorUsernameToId.get(username);
        if(id == null){
            return null;
        }
        return getProfessorById(id);
    }

    /**
     * get professor by their names
     * @param name of professor
     * @return the professor object
     */
    public Professor getProfessorByName(String name){
        // return null if input is invalid
        if(name == null || name.isEmpty()){
            return null;
        }
        for(Professor professor : professorMap.values()){
            if (professor.getName().equals(name)){
                return professor;
            }
        }
        return null;
    }

    /**
     * Retrieve a course by its ID.
     * @param id The ID of the course.
     * @return The course object if found, null otherwise.
     */
    public Student getStudentById(String id){
        // return null if input is invalid
        if(id == null || id.isEmpty()){
            return null;
        }
        return studentMap.getOrDefault(id, null);
    }

    /**
     * Retrieve a student by their name.
     * @param username The name of the student.
     * @return The student object if found, null otherwise.
     */
    public Student getStudentByUsername(String username){
        // return null if input is invalid
        if(username == null || username.isEmpty()){
            return null;
        }
        // get student id
        String id = studentUsernameToId.get(username);
        if(id == null){
            return null;
        }
        return getStudentById(id);
    }

    /**
     * Retrieve an admin by their ID.
     * @param id The ID of the admin.
     * @return The admin object if found, null otherwise.
     */
    public Admin getAdminById(String id){
        // return null if input is invalid
        if(id == null || id.isEmpty()){
            return null;
        }
        return adminMap.getOrDefault(id, null);
    }

    /**
     * Retrieve an admin by their name.
     * @param username The name of the admin.
     * @return The admin object if found, null otherwise.
     */
    public Admin getAdminByUsername(String username){
        if(username == null || username.isEmpty()){
            return null;
        }
        String id = adminUsernameToId.get(username);
        if(id == null){
            return null;
        }
        return getAdminById(id);
    }



    /**
     *  Retrieve an admin by their id.
     * @param id The ID of the admin
     * @return  The admin object if found, null otherwise.
     */
    public boolean deleteProfessor(String id){
        if(id == null || id.isEmpty()){
            return false;
        }
        return professorMap.remove(id) != null;
    }

    /**
     *  Delete a student by their ID
     * @param id The ID of the student
     * @return  The student object if found, null otherwise.
     */
    public boolean deleteStudent(String id){
        if(id == null || id.isEmpty()){
            return false;
        }
        return studentMap.remove(id) != null;
    }

    /**
     *  Delete an admin by their ID
     * @param id The ID of the admin
     * @return  The admin object if found, null otherwise.
     */
    public boolean deleteAdmin(String id){
        if(id == null || id.isEmpty()){
            return false;
        }
        return adminMap.remove(id) != null;
    }

    /**
     *  Retrieve a list of courses by their professor
     * @param professorId The professor id whose courses are being retrieved
     * @return  A list of courses
     */
    public List<Course> getCoursesByProfessor(String professorId){
        if(professorId == null || professorId.isEmpty()){
            return new ArrayList<>();
        }
        return professorCourseList.getOrDefault(professorId, new ArrayList<>());
    }


}
