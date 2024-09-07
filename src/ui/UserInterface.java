package ui;

import constant.Constant;
import courses.Course;
import processor.Processor;
import roles.Admin;
import roles.Professor;
import roles.Student;
import roles.User;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * ui level of the management system
 * @author Xuanhe Zhang, Huiwen Jia
 */
public class UserInterface {

    // instance variables
    Processor processor;
    Scanner scanner;

    // constructor
    /**
     * Construct the user interface
     */
    public UserInterface(){
        this.processor = new Processor();
        this.scanner = new Scanner(System.in);
    }

    /**
     * show welcome based on the user type
     */
    public void start(){
        System.out.println("Starting the Student Management System...");
        showLoginSelection();
    }

    /**
     * quit the Student Management System
     */
    private void quit(){
        System.out.println("Shutting down the Student Management System...");
        System.exit(0);
    }

    /**
     * show login selection by the user
     */
    private void showLoginSelection(){
        System.out.println(Constant.BANNER);
        System.out.println("Students Management System");
        System.out.println(Constant.BANNER);
        System.out.println("1 -- Login as a student");
        System.out.println("2 -- Login as a professor");
        System.out.println("3 -- Login as an admin");
        System.out.println("4 -- Quit the system");
        System.out.println();

        String option = askFor("Please enter your option, eg. '1'.").trim();

        while(!"1".equals(option) && !"2".equals(option) && !"3".equals(option) && !"4".equals(option)){
            option = askFor("Invalid option. Please try again. Please enter your option, eg. '1'.").trim();
        }

        switch(option){
            case "1":
                showLogin(Constant.STUDENT_TYPE);
                break;
            case "2":
                showLogin(Constant.PROFESSOR_TYPE);
                break;
            case "3":
                showLogin(Constant.ADMIN_TYPE);
                break;
            case "4":
                quit();
                break;
        }

    }

    /**
     * show login questions, including the username and password
     */

    private void showLogin(String type){
        String username = "";
        String password = "";


        username = askFor("Please enter your username, or type 'q' to quit");
        if("q".equals(username)){
            showLoginSelection();
        }
        password = askFor("Please enter your password, or type 'q' to quit");
        if("q".equals(password)){
            showLoginSelection();
        }
        if(processor.login(type, username, password)){
            showWelcome();
        } else{
            System.out.println("Wrong username or password!");
            showLoginSelection();
        }
    }

    /**
     * print the welcome message based on the user type
     */
    private void showWelcome(){
        User user = processor.getUser();

        if(user == null){
            throw new RuntimeException("User is null");
        }

        System.out.println(Constant.BANNER);
        System.out.printf(" Welcome, %s\n", user.getName());
        System.out.println(Constant.BANNER);

        if(user instanceof Student){
            showStudentMenu((Student) user);
        } else if(user instanceof Professor){
            showProfessorMenu((Professor) user);
        } else if(user instanceof Admin){
            showAdminMenu((Admin) user);
        } else {
            throw new RuntimeException("User type is not supported");
        }
    }

    /**
     * show the student menu
     */
    private void showStudentMenu(Student student){
        boolean keepRunning = true;


        System.out.println("1 -- View all courses");
        System.out.println("2 -- Add courses to your list");
        System.out.println("3 -- View selected courses");
        System.out.println("4 -- Drop courses in your list");
        System.out.println("5 -- View grades");
        System.out.println("6 -- Return to previous menu");

        while (keepRunning) {
            System.out.println();
            String option = askFor("Please enter your option, eg. '1'.");
            switch(option){
                case "1":
                    viewAllCourses();
                    break;
                case "2":
                    addCoursesToYourList(student);
                    break;
                case "3":
                    viewEnrolledCourses(student);
                    break;
                case "4":
                    dropCoursesInYourList(student);
                    break;
                case "5":
                    viewGrades(student);
                    break;
                case "6":
                    keepRunning = false;
                    showLoginSelection();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }


    /**
     * view all enrolled courses of the student
     */
    public void viewEnrolledCourses(Student student){
        List<String> coursesList = student.getCoursesList();
        // show all enrolled courses
        System.out.println("The courses in your list:");
        for(String id: coursesList) {
            Course course = processor.getCourseById(id);
            System.out.println(course.getId() + " " + course.getName());
        }
    }



    /**
     * view the grades
     */
    public void viewGrades(Student student){
        Map<String, String> grades = student.getGrades();

        if (grades == null || grades.isEmpty()) {
            System.out.println("No grades available.");
            return;
        }

        System.out.println("Here are the courses you already taken, with your grade in a letter format:");
        for (Map.Entry<String, String> entry : grades.entrySet()) {
            String courseId = entry.getKey();
            String grade = entry.getValue();
            Course course = processor.getCourseById(courseId);
            if (course != null) {
                System.out.println("Grade of " + course.getName() + ": " + grade);
            } else {
                System.out.println("Course with ID " + courseId + " not found.");
            }
        }
    }

    /**
     * View all courses
     */
    public void viewAllCourses(){
        // get all courses from database
        Map<String, Course> courses = processor.getAllCourses();
        for(String courseId : courses.keySet()){
            Course course = processor.getCourseById(courseId);
            System.out.println(course);
        }
    }

    /**
     * add the course to the student list
     * @param student who add the course 
     */
    private void addCoursesToYourList(Student student){
        String courseId;
        do {
            courseId = askFor("Please select the course ID" +
                    " you want to add to your list, eg. 'CIT590'. " +
                    "or enter 'q' to return to the previous menu.");
            if ("q".equals(courseId)) {
                return;
            }
            boolean ok = student.enrollCourse(courseId);
            if (ok) {
                System.out.println("Course enrolled successfully");
            }
        } while (true);
    }

    /**
     * drop the course from the list of student
     * @param student who drop the course
     */
    private void dropCoursesInYourList(Student student){
        String courseId;
        do {
            courseId = askFor("Please select the course ID" +
                    " you want to drop from your list, eg. 'CIT590'. " +
                    "or enter 'q' to return to the previous menu.");
            if ("q".equals(courseId)) {
                return;
            }
            boolean ok = student.dropCourse(courseId);
            if (ok) {
                System.out.println("Course dropped successfully");
            } else {
                System.out.println("The course isn't in your schedule.");
            }
        } while (true);
    }

    /**
     * show the professor menu
     */
    private void showProfessorMenu(Professor professor) {
        boolean keepRunning = true;

        System.out.println("1 -- View given courses");
        System.out.println("2 -- View student list of the given course");
        System.out.println("3 -- Return to the previous menu");

        while (keepRunning) {

            System.out.println();
            String option = askFor("Please enter your option, eg. '1'.");
            System.out.println();
            switch (option) {
                case "1":
                    viewGivenCourses(professor);
                    break;
                case "2":
                    viewStudentListOfTheGivenCourse(professor);
                    break;
                case "3":
                    keepRunning = false;
                    showLoginSelection();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * view the course of the professor
     * @param professor who takes the course
     */
    private void viewGivenCourses(Professor professor){
        List<Course> courseList = professor.getGivenCourses();
        if(courseList == null || courseList.isEmpty()){
            System.out.println("No given courses");
            return;
        }
        for(Course course : courseList){
            System.out.println(course.getId() + " " + course.getName());
        }
    }

    /**
     * view student list of the given course by professor
     * @param professor who view the list
     */
    private void viewStudentListOfTheGivenCourse(Professor professor){
        String courseId;
        Course course;
        while(true){
            courseId = askFor("Please enter the course ID to view students, eg. 'CIT590'.");
            if ("q".equals(courseId)) {
                return;
            }
            course  = processor.getCourseById(courseId);
            if(course == null){
                System.out.println("Invalid course ID");
            } else{
                break;
            }
        }

        List<Student> studentList = professor.getStudentListOfGivenCourse(courseId);

        System.out.printf("Students in your course %s %s:\n", courseId, course.getName());
        for(Student student : studentList){
            System.out.println(student.getId() + " " + student.getName());
        }
    }

    /**
     * show the admin menu
     */
    private void showAdminMenu(Admin admin) {
        boolean keepRunning = true;

        System.out.println("1 -- View all courses");
        System.out.println("2 -- Add new courses");
        System.out.println("3 -- Delete courses");
        System.out.println("4 -- Add new professor");
        System.out.println("5 -- Delete professor");
        System.out.println("6 -- Add new student");
        System.out.println("7 -- Delete student");
        System.out.println("8 -- Return to previous menu");

        while (keepRunning) {

            System.out.println();
            String option = askFor("Please enter your option, eg. '1'.");
            System.out.println();
            switch (option) {
                case "1" : {
                    viewAllCourses();
                    break;
                }
                case "2" : {
                    addNewCourses(admin);
                    break;
                }
                case "3" : {
                    deleteCourses(admin);
                    break;
                }
                case "4" : {
                    addNewProfessor(admin);
                    break;
                }
                case "5" : {
                    deleteProfessor(admin);
                    break;
                }
                case "6" : {
                    addNewStudent(admin);
                    break;
                }
                case "7" : {
                    deleteStudent(admin);
                    break;
                }
                case "8" : {
                    keepRunning = false;
                    showLoginSelection();
                    break;
                }
                default : {
                    System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }

    /**
     * add new courses
     * @param admin object
     */
    private void addNewCourses(Admin admin){
        while(true){
            String id = askFor("Please enter the course ID" +
                    " you want to add to your list, eg. 'CIT590'. " +
                    "or enter 'q' to return to the previous menu.");
            if ("q".equals(id)) {
                return;
            }

            String name = askFor("Please enter the course name, or type 'q' to end.");
            if ("q".equals(name)) {
                return;
            }

            String startTimeString = askFor("Please enter the course start time, or type 'q' to end. eg. '19:00'");
            LocalTime startTime;

            while(true){
                if ("q".equals(startTimeString)) {
                    return;
                }
                try{
                    startTime = LocalTime.parse(startTimeString);
                    break;
                } catch(Exception e){
                    startTimeString = askFor("Invalid time format");
                    continue;
                }
            }

            String endTimeString = askFor("Please enter the course end time, or type 'q' to end. eg. '20:00'");

            LocalTime endTime;

            while(true){
                if ("q".equals(endTimeString)) {
                    return;
                }
                try{
                    endTime = LocalTime.parse(endTimeString);
                    break;
                } catch(Exception e){
                    endTimeString = askFor("Invalid time format");
                    continue;
                }
            }

            String weekDay = askFor("Please enter the course date, or type 'q' to end. eg. 'MW'");
            if ("q".equals(weekDay)) {
                return;
            }

            String capacityInput = askFor("Please enter the course capacity, or type 'q' to end. eg. '72'");
            if ("q".equals(capacityInput)) {
                return;
            }
            int capacity = Integer.parseInt(capacityInput);

            String professorId = askFor("Please enter the course lecturer's id, or type 'q' to end. eg. '001'");
            if ("q".equals(professorId)) {
                return;
            }

            // add the course
            boolean ok = admin.addNewCourse(id, name, professorId, weekDay, startTime, endTime, capacity);
            if(ok) {
                System.out.println("Course with ID " + id + " has been successfully added.");
            }
        }
    }

    /**
     * delete the courses by admin
     * @param admin who delete the course
     */
    private void deleteCourses(Admin admin){
        while(true){
            String courseId = askFor("Please enter the course ID" +
                    " you want to delete, eg. 'CIT590'. " +
                    "or enter 'q' to return to the previous menu.");

            if ("q".equals(courseId)) {
                return;
            } 

            boolean ok = admin.deleteCourse(courseId);
            if(ok){
                System.out.println("Course with ID " + courseId + " has been successfully deleted.");
                System.out.println();
            } else {
                System.out.println("You enter a wrong course ID, try again.");
            }

        }
    }

    /**
     * add new professor by admin
     * @param admin who add the professor
     */
    private void addNewProfessor(Admin admin){
        String id;
        String name;
        String username;
        String password;

        id = askFor("Please enter the professor's id, or type 'q' to quit");
        if ("q".equals(id)) {
            return;
        }
        
        name = askFor("Please enter professor's name, or type 'q' to end");
        if ("q".equals(name)) {
            return;
        }

        username = askFor("Please enter the username");
        if ("q".equals(username)) {
            return;
        }

        password = askFor("Please enter a password");
        if ("q".equals(password)) {
            return;
        }

        boolean ok = admin.addNewProfessor(name, username, id, password);
        if(ok){
            System.out.println("Add new professor success");
        }
    }

    /**
     * delete the professor by admin
     * @param admin who delete the professor
     */
    private void deleteProfessor(Admin admin){
        String professorId = askFor("Please enter the ID of the professor you want to delete, or type 'q' to quit.");
        if ("q".equals(professorId)) {
            return;
        }
        boolean ok = admin.deleteProfessor(professorId);
        if(ok){
            System.out.println("Professor with ID " + professorId + " has been successfully deleted.");
        }
    }

    /**
     * add new student by admin
     * @param admin who add the new student
     */
    private void addNewStudent(Admin admin){
        String id;
        String name;
        String username;
        String password;

        id = askFor("Please enter the student's id, or type 'q' to quit");
        if ("q".equals(id)) {
            return;
        }
        
        name = askFor("Please enter student's name, or type 'q' to end");
        if ("q".equals(name)) {
            return;
        }

        username = askFor("Please enter the username");
        if ("q".equals(username)) {
            return;
        }

        password = askFor("Please enter a password");
        if ("q".equals(password)) {
            return;
        }

        boolean ok = admin.addNewStudent(name, username, id, password);
        if(ok){
            System.out.println("Student with ID " + id + " has been successfully added.");
        }

        System.out.println("Please enter ID of a course which this student already took, one in a time");
        String courseId = askFor("Type 'q' to quit, type 'n' to stop adding.");
        while(!"n".equals(courseId)){
            if("q".equals(courseId)){
                return;
            }

            String grade = askFor("Please enter the grade, eg,'A");
            Student student = processor.getStudentById(id);
            student.addCourseWithGrade(courseId, grade);

            courseId = askFor("Type 'q' to quit, type 'n' to stop adding.");
        }



    }

    /**
     * delete the student by admin
     * @param admin who delete the student
     */
    private void deleteStudent(Admin admin){
        String studentId = askFor("Please enter the ID of the student you want to delete, or type 'q' to quit.");
        if ("q".equals(studentId)) {
            return;
        }
        boolean ok = admin.deleteStudent(studentId);
        if(ok){
            System.out.println("Student with ID " + studentId + " has been successfully deleted.");
        }
    }

    /**
     * ask for the user input
     * @param message of the question
     * @return the user input if the message is not empty
     */
    private String askFor(String message){
        if(message == null || message.isEmpty()){
            return "";
        }
        System.out.println(message);
        return scanner.nextLine();
    }

}
