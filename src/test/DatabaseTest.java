package test;

import database.Database;
import courses.Course;
import processor.Processor;
import roles.Professor;
import roles.Student;
import roles.User;
import roles.Admin;

import java.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Xuanhe Zhang, Huiwen Jia, Haoran Hua
 */
public class DatabaseTest {
    @Before
    public void setUp(){
        Database.reset();
        Processor processor = new Processor();
    }


    @Test
    public void testAddCourse() {
        Database database = Database.getInstance();
        Professor professorTest =  new Professor("001", "james", "jm", "001");
        Course course = new Course("CIT5920", "Math", professorTest, "MW",
                LocalTime.of(11, 0), LocalTime.of(12, 30), 30);
        assertTrue(database.addCourse(course));

        Course retrievedCourse = database.getCourseById("CIT5920");
        assertNotNull(retrievedCourse);
        assertEquals("Math", retrievedCourse.getName());

        // Adding the same course again should return false
        assertFalse(database.addCourse(course));
    }

    @Test
    public void testAddCourseWithConflict() {
        Database database = Database.getInstance();
        Professor professor = new Professor("2", "Dr. Johnson", "johnson456", "password2");

        // Create course1 and course2
        Course course1 = new Course("MATH202", "Advanced Mathematics2", professor, "Wednesday", LocalTime.of(14, 0), LocalTime.of(16, 0), 25);
        Course course2 = new Course("PHYS101", "Physics 101", professor, "Wednesday", LocalTime.of(15, 0), LocalTime.of(17, 0), 30);

        // Add course1 and course2
        assertTrue("Failed to add course1", database.addCourse(course1));
        assertTrue("Failed to add course2", database.addCourse(course2));

        // Attempt to add a conflicting course
        Course conflictingCourse = new Course("CHEM201", "Chemistry 201", professor, "Wednesday", LocalTime.of(14, 30), LocalTime.of(16, 30), 20);

        // Delete course1 and course2 to simulate conflict
        assertTrue(database.deleteCourse("MATH202"));
        assertTrue(database.deleteCourse("PHYS101"));

        // Attempt to add a conflicting course
        assertTrue("Expected to fail adding conflictingCourse", database.addCourse(conflictingCourse));


    }


    @Test
    public void testAddMultipleCourses() {
        Database database = Database.getInstance();
        Professor professor = new Professor("3", "Dr. Brown", "brown789", "password3");
        //List<Course> courses = new ArrayList<>();

        Course course1 = new Course("MATH101", "Mathematics 101", professor, "Monday", LocalTime.of(10, 0), LocalTime.of(12, 0), 25);
        Course course2 = new Course("MATH201", "Mathematics 201", professor, "Tuesday", LocalTime.of(14, 0), LocalTime.of(16, 0), 30);
        Course course3 = new Course("MATH301", "Mathematics 301", professor, "Wednesday", LocalTime.of(9, 0), LocalTime.of(11, 0), 20);

        assertTrue(database.addCourse(course1));
        assertTrue(database.addCourse(course2));
        assertTrue(database.addCourse(course3));

        // Adding the same courses again should return false
        assertFalse(database.addCourse(course1));

        // Check that the courses have been added
        assertNotNull(database.getCourseById("MATH101"));
        assertNotNull(database.getCourseById("MATH201"));
        assertNotNull(database.getCourseById("MATH301"));
    }


    @Test
    public void testAddUsers() {
        Database database = Database.getInstance();
        User user1 = new Student("1", "John", "john123", "password1");
        User user2 = new Professor("2", "Dr. Smith", "smith456", "password2");
        User user3 = new Admin("3", "AdminUser", "admin789", "password3");

        assertTrue(database.addUser(user1));
        assertTrue(database.addUser(user2));
        assertTrue(database.addUser(user3));
        // Adding the same users again should return false
        assertFalse(database.addUser(user1));
        // test null pointer
        assertFalse(database.addUser(null));


        // Check that the users have been added
        assertNotNull(database.getStudentById("1"));
        assertNotNull(database.getProfessorById("2"));
        assertNotNull(database.getAdminById("3"));
    }

    @Test
    public void testGetStudentById(){
        Database database = Database.getInstance();
        User user1 = new Student("1999", "S John", "sjohn1235", "password1");
        database.addUser(user1);

        assertEquals(database.getStudentById("1999").getName(), "S John");
        // test null
        assertNull(database.getStudentById(null));
    }

    @Test
    public void testGetProfessorById(){
        Database database = Database.getInstance();
        User user1 = new Professor("999", "P John", "pjohn1235", "password1");
        database.addUser(user1);

        assertEquals(database.getProfessorById("999").getName(), "P John");
        // test null
        assertNull(database.getProfessorById(null));
    }

    @Test
    public void testGetAdminById(){
        Database database = Database.getInstance();
        User user1 = new Admin("999", "A John", "ajohn1235", "password1");
        database.addUser(user1);

        assertEquals(database.getAdminById("999").getName(), "A John");
        // test null
        assertNull(database.getAdminById(null));
    }

    @Test
    public void testDeleteCourse() {
        Database database = Database.getInstance();
        Professor professorTest = new Professor("984", "name", "username984", "password");
        Course course2 = new Course("CIT592", "Math II", professorTest, "MW",
                LocalTime.of(11, 0), LocalTime.of(12, 30), 30);
        database.addCourse(course2);

        assertTrue(database.deleteCourse("CIT592"));

        // Attempt to delete the same course again should return false
        assertFalse(database.deleteCourse("CIT592"));

        // Make sure the course is deleted
        assertNull(database.getCourseById("CIT592"));
    }

    @Test
    public void testDeleteStudent() {
        Database database = Database.getInstance();
        Student student = new Student("7", "John", "john1237", "password1");
        database.addUser(student);

        assertTrue(database.deleteStudent("7"));

        // Attempt to delete the same student again should return false
        assertFalse(database.deleteStudent("7"));

        // Make sure the student is deleted
        assertNull(database.getStudentById("7"));
    }

    @Test
    public void testDeleteProfessor() {
        Database database = Database.getInstance();
        Professor professor = new Professor("27", "Dr. Smith", "smith4567", "password2");
        database.addUser(professor);

        assertTrue(database.deleteProfessor("27"));

        // Attempt to delete the same professor again should return false
        assertFalse(database.deleteProfessor("27"));

        // Make sure the professor is deleted
        assertNull(database.getProfessorById("27"));
    }

    @Test
    public void testDeleteAdmin() {
        Database database = Database.getInstance();
        Admin admin = new Admin("30", "AdminUser", "admin7890", "password3");
        database.addUser(admin);

        assertTrue(database.deleteAdmin("30"));

        // Attempt to delete the same admin again should return false
        assertFalse(database.deleteAdmin("30"));

        // Make sure the admin is deleted
        assertNull(database.getAdminById("30"));
    }


}

