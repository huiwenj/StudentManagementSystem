/**
 * read and parse provided files in Java
 */
package files;

import courses.Course;
import database.Database;
import roles.Admin;
import roles.User;
import roles.Student;
import roles.Professor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * read and parse provided files to database
 * @author Xuanhe Zhang, Huiwen Jia, Haoran Hua
 */
public class FileInfoReader {
	
	// instance variables
    private final Database database;
    private final String COURSE_FILE_PATH = "courseinfo.txt";
    private final String STUDENT_FILE_PATH = "studentinfo.txt";
    private final String PROFESSOR_FILE_PATH = "profinfo.txt";
    private final String ADMIN_FILE_PATH = "admininfo.txt";

    // constructor
    /**
     * initialize a single instance of database to be populated with data
     */
    public FileInfoReader(){
        database = Database.getInstance();
    }

    /**
     * reads file, parse file path
     */
    public void readFiles(){
        // parse files in order
        parseAdmin(ADMIN_FILE_PATH);
        parseProfessor(PROFESSOR_FILE_PATH);
        parseCourse(COURSE_FILE_PATH);
        parseStudent(STUDENT_FILE_PATH);
    }


    /**
     * parse and add student information to the database
     * @param path of student data
     */
    private void parseStudent(String path){
        File file = new File(path);
        try(FileReader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                // split text to tokens
                String[] tokens = line.split(";");
                // parse each filed
                String id = tokens[0].trim();
                String name = tokens[1].trim();
                String username = tokens[2].trim();
                String password = tokens[3].trim();
                Student student = new Student(id, name, username, password);
                // add user to database
                database.addUser(student);
                // process student's grade
                Map<String, String> courseMap = parseStudentCourses(tokens[4]);
                for(String courseId: courseMap.keySet()){
                    String grade = courseMap.get(courseId);
                    student.addCourseWithGrade(courseId, grade);
                }
                // read next line
                line = bufferedReader.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * parse students data from tokens array
     * @param token containing course data of student
     * @return a map of course IDs and grades
     */
    private Map<String, String> parseStudentCourses(String token){
        // split course
        String[] courses = token.split(",");
        // parse courses
        List<String[]> courseList = new ArrayList<>();

        for (String course : courses) {
            courseList.add(course.trim().split(":"));
        }

        Map<String, String> courseMap = new HashMap<>();

        for (String[] strings : courseList) {
            courseMap.put(strings[0].trim(), strings[1].trim());
        }
        return courseMap;
    }

    /**
     * parse and add professor's information to the database
     * @param path of professor data
     */
    private void parseProfessor(String path){
        File file = new File(path);
        try(FileReader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] tokens = line.split(";");
                String name = tokens[0].trim();
                String id = tokens[1].trim();
                String username = tokens[2].trim();
                String password = tokens[3].trim();
                Professor professor = new Professor(id, name, username, password);
                database.addUser(professor);
                line = bufferedReader.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * parse and add administrators' information to the database
     * @param path of administrator data
     */
    private void parseAdmin(String path){
        File file = new File(path);
        try(FileReader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] tokens = line.split(";");
                String id = tokens[0].trim();
                String name = tokens[1].trim();
                String username = tokens[2].trim();
                String password = tokens[3].trim();
                Admin admin = new Admin(id, name, username, password);
                database.addUser(admin);
                line = bufferedReader.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * parse and add courses' information to the database
     * @param path of course list data
     */
    private void parseCourse(String path){
        File file = new File(path);
        try(FileReader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] tokens = line.split(";");
                String id = tokens[0].trim();
                String name = tokens[1].trim();
                String professorName = tokens[2].trim();
                String weekDay = tokens[3].trim();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
                LocalTime startTime = LocalTime.parse(tokens[4].trim(), formatter);
                LocalTime endTime = LocalTime.parse(tokens[5].trim(), formatter);
                int capacity = Integer.parseInt(tokens[6].trim());

                Professor professor = database.getProfessorByName(professorName);
                Course course = new Course(id, name, professor, weekDay, startTime, endTime, capacity);
                database.addCourse(course);
                line = bufferedReader.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
