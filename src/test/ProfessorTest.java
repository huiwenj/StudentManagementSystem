package test;

import courses.Course;
import database.Database;
import roles.Professor;
import roles.Student;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import java.util.List;
import java.time.LocalTime;

/**
 * @author Xuanhe Zhang, Huiwen Jia, Haoran Hua
 */
public class ProfessorTest {
    @Before
    public void setUp(){
        Database.reset();
    }


    @Test
    public void testViewGivenCourses() {
        Database database = Database.getInstance();
        // Test professor's courses
        Professor test = new Professor("test001", "test", "test", "test");
        Course course = new Course("course001", "testCourse", test, "test", LocalTime.of(3,3), LocalTime.of(4,4), 40);
        database.addUser(test);
        // add one course first
        database.addCourse(course);
        List<Course> courses = test.getGivenCourses();
        assertTrue(courses.contains(course));
        assertEquals(courses.size(), 1);
        // add more courses
        Course nextCourse = new Course("course002", "testCourse", test, "test", LocalTime.of(3,3), LocalTime.of(4,4), 40);
        database.addCourse(nextCourse);
        assertTrue(courses.contains(nextCourse));
        assertEquals(courses.size(), 2);

    }

    @Test
    public void testViewGivenCoursesNoCourses() {
        // Test professor with no courses
        Professor newProfessor = new Professor("Professor002", "Dr. Strange", "Magic", "newPassword");
        assertTrue(newProfessor.getGivenCourses().isEmpty());
    }

    @Test
    public void testViewStudentListOfGivenCourseNoCourse() {
        Professor professor = new Professor("Professor001", "Peter Parker", "Spiderman1", "123456");
        // Test student list for a non-existing course
        List<Student> students = professor.getStudentListOfGivenCourse("CIT999");
        assertTrue(students.isEmpty());
    }

    @Test
    public void testViewStudentListOfGivenCourseEmptyCourse() {
        Professor professor = new Professor("Professor001", "Peter Parker", "Spiderman1", "123456");
        // Test course with no students
        Course emptyCourse = new Course("CIT594", "No Students", professor, "MW",
                LocalTime.of(13, 00), LocalTime.of(14, 30), 30);
        Database database = Database.getInstance();
        database.addCourse(emptyCourse);
        List<Student> students = professor.getStudentListOfGivenCourse("CIT594");
        assertTrue(students.isEmpty());
    }

    @Test
    public void testViewStudentListOfGivenCourseNull() {
        // Test professor with no courses
        Professor newProfessor = new Professor("Professor003", "Dr. Strange", "Magic3", "newPassword");
        // Testing null
        assertTrue(newProfessor.getStudentListOfGivenCourse(null).isEmpty());
    }


}
