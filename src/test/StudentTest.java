package test;

import courses.Course;
import database.Database;
import processor.Processor;
import roles.Professor;
import roles.Student;
import java.time.LocalTime;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Xuanhe Zhang, Huiwen Jia, Haoran Hua
 */
public class StudentTest {
    @Before
    public void setUp(){
        Database.reset();
        Processor processor = new Processor();
    }


    @Test
    public void testEnrollCourse() {
        Professor professorTest = new Professor("Professor001", "Peter Parker", "Spiderman", "123456");
        Student student = new Student("001", "student test", "studenttest", "password");
        Course course1 = new Course("CIT591", "Introduction to Programming", professorTest, "MW",
                LocalTime.of(9, 0), LocalTime.of(10, 30), 30);
        Course course2 = new Course("CIT592", "Math", professorTest, "MW",
                LocalTime.of(11, 0), LocalTime.of(12, 30), 30);

        student.enrollCourse(course1.getId());

        // Test case 1: Enroll in a duplicated course
        assertFalse(student.enrollCourse(course1.getId()));

        // Test case 2: Enroll in a valid course
        assertTrue(student.enrollCourse(course2.getId()));

        // Test case 3: Enroll in a course that does not exist
        assertFalse(student.enrollCourse("CIS999"));
    }

    @Test
    public void testAddCourseWithGrade() {
        Professor professorTest = new Professor("Professor001", "Peter Parker", "Spiderman", "123456");
        Student student = new Student("001", "student test", "studenttest", "password");
        Course course1 = new Course("CIT591", "Introduction to Programming", professorTest, "MW",
                LocalTime.of(9, 0), LocalTime.of(10, 30), 30);
        Course course2 = new Course("CIT592", "Math", professorTest, "MW",
                LocalTime.of(11, 0), LocalTime.of(12, 30), 30);
        Course  course3 = new Course("CIS105", "Introduction to Computer Science", professorTest, "TR",
                LocalTime.of(14, 0), LocalTime.of(15, 30), 30);
        student.enrollCourse(course1.getId());
        // Enroll in course1
        student.enrollCourse(course1.getId());
        Map<String, String> grades = student.getGrades();

        // Test case 1: Add a valid grade "A" to course1
        student.addCourseWithGrade(course1.getId(), "A");
        assertEquals(grades.get(course1.getId()), "A");
        // Test case 2: Add an empty grade to course2
        student.addCourseWithGrade(course2.getId(), "");
        assertEquals(grades.get(course2.getId()), null);
        // Test case 3: Add a grade to course3, which was not enrolled
        student.addCourseWithGrade(course3.getId(), "B");
        assertEquals(grades.get(course3.getId()), "B");

    }
    @Test
    public void testDropCourse() {
        Professor professorTest = new Professor("Professor001", "Peter Parker", "Spiderman", "123456");
        Student student = new Student("001", "student test", "studenttest", "password");
        Course course1 = new Course("CIT591", "Introduction to Programming", professorTest, "MW",
                LocalTime.of(9, 0), LocalTime.of(10, 30), 30);
        Course course2 = new Course("CIT592", "Math", professorTest, "MW",
                LocalTime.of(11, 0), LocalTime.of(12, 30), 30);
        student.enrollCourse(course1.getId());

        // // Test case 1: Drop an enrolled course
        // assertTrue(student.dropCourse(course1.getId()));

        // Test case 2: Drop a course that was not enrolled
        assertFalse(student.dropCourse(course2.getId()));

        // Test case 3: Drop a course that does not exist
        assertFalse(student.dropCourse("CIS999"));
    }
}
