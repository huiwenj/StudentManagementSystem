package test;

import courses.Course;
import database.Database;
import processor.Processor;
import roles.Professor;
import roles.Student;

import java.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Xuanhe Zhang, Huiwen Jia, Haoran Hua
 */
public class CourseTest {

    @Before
    public void setUp(){
        Database.reset();
        Processor processor = new Processor();
    }

    @Test
    public void testAddStudent() {
        Professor professorTest = new Professor("Professor001", "Peter Parker", "Spiderman", "123456");
        Student student1 = new Student("Student001", "Tobey Maguire", "Spiderman", "123456");
        Student student2 = new Student("Student002", "Andrew Garfield", "Spiderman", "123456");
        Student student3 = new Student("Student003", "Tom Holland", "Spiderman", "123456");

        Course course1 = new Course("CIT591", "Introduction to Programming", professorTest, "MW",
                LocalTime.of(9, 0), LocalTime.of(10, 30), 30);
        Course course2 = new Course("CIT592", "Math", professorTest, "MW",
                LocalTime.of(11, 0), LocalTime.of(12, 30), 30);


        // Test adding students to the course
        assertTrue(course1.addStudent(student1));
        assertEquals(1, course1.getStudents().size());

        assertTrue(course1.addStudent(student2));
        assertEquals(2, course1.getStudents().size());

        // Test adding student who already in the course
        assertFalse(course1.addStudent(student1)); // Should return false as student1 is already added
        assertEquals(2, course1.getStudents().size()); // Size should remain 2

        // Test adding the third student
        assertTrue(course1.addStudent(student3));
        assertEquals(3, course1.getStudents().size());

        // test null pointer
        assertFalse(course1.addStudent(null));
        assertEquals(3, course1.getStudents().size());
    }

    @Test
    public void testRemoveStudent() {
        Professor professorTest = new Professor("Professor001", "Peter Parker", "Spiderman", "123456");
        Student student1 = new Student("Student001", "Tobey Maguire", "Spiderman", "123456");
        Student student2 = new Student("Student002", "Andrew Garfield", "Spiderman", "123456");
        Student student3 = new Student("Student003", "Tom Holland", "Spiderman", "123456");

        Course course1 = new Course("CIT591", "Introduction to Programming", professorTest, "MW",
                LocalTime.of(9, 0), LocalTime.of(10, 30), 30);
        Course course2 = new Course("CIT592", "Math", professorTest, "MW",
                LocalTime.of(11, 0), LocalTime.of(12, 30), 30);

        // Add students to the course
        course1.addStudent(student1);
        course1.addStudent(student2);
        course1.addStudent(student3);

        // Testing remove a student
        assertTrue(course1.removeStudent(student1));
        assertEquals(2, course1.getStudents().size());

        // Testing remove a student that is not in the course
        assertFalse(course1.removeStudent(student1)); // Should return false

        // Testing keep removing student
        assertTrue(course1.removeStudent(student2));
        assertEquals(1, course1.getStudents().size());

        // test null pointer
        assertFalse(course1.removeStudent(null));
        assertEquals(1, course1.getStudents().size());


    }

    @Test
    public void testConflictWith() {
        Professor professorTest = new Professor("Professor001", "Peter Parker", "Spiderman", "123456");
        Student student1 = new Student("Student001", "Tobey Maguire", "Spiderman", "123456");
        Student student2 = new Student("Student002", "Andrew Garfield", "Spiderman", "123456");
        Student student3 = new Student("Student003", "Tom Holland", "Spiderman", "123456");

        Course course1 = new Course("CIT591", "Introduction to Programming", professorTest, "MW",
                LocalTime.of(9, 0), LocalTime.of(10, 30), 30);
        Course course2 = new Course("CIT592", "Math", professorTest, "MW",
                LocalTime.of(11, 0), LocalTime.of(12, 30), 30);

        // Testing no conflict for different times
        assertFalse(course1.conflictWith(course2));

        // Testing conflict for overlapping
        Course course3 = new Course("CIT593", "Computer Systems", professorTest, "MW",
                LocalTime.of(10, 00), LocalTime.of(11, 45), 25);
        assertTrue(course1.conflictWith(course3));

        // Testing no conflict for different days
        Course course4 = new Course("CIT594", "Software Engineering", professorTest, "TTh",
                LocalTime.of(9, 0), LocalTime.of(10, 30), 30);
        assertFalse(course1.conflictWith(course4));

        // Create a course with overlapping time on the same day
        Course course5 = new Course("CIT595", "Physics", professorTest, "MW",
                LocalTime.of(10, 15), LocalTime.of(11, 45), 30);

        // Test if course1 conflicts with course5
        assertTrue(course1.conflictWith(course5));

        // Testing conflict with a course taught by the same professor
        Course course6 = new Course("CIT596", "Chemistry", professorTest, "MW",
                LocalTime.of(9, 30), LocalTime.of(11, 0), 30);
        assertTrue(course1.conflictWith(course6));

        // test different weekday format
        Course course7 = new Course("CIT900", "test", professorTest, "MW", LocalTime.of(3, 00), LocalTime.of(4, 00), 30);
        Course course8 = new Course("CIT901", "test", professorTest, "M", LocalTime.of(3, 00), LocalTime.of(4, 00), 30);
        Course course9 = new Course("CIT902", "test", professorTest, "W", LocalTime.of(3, 00), LocalTime.of(4, 00), 30);

        assertTrue(course7.conflictWith(course8));
        assertTrue(course8.conflictWith(course7));

        assertTrue(course7.conflictWith(course9));
        assertTrue(course9.conflictWith(course7));

        assertFalse(course8.conflictWith(course9));
        assertFalse(course9.conflictWith(course8));

        // test different time range
        Course course10 = new Course("CIT903", "test", professorTest, "W", LocalTime.of(4, 00), LocalTime.of(5, 00), 30);
        Course course11 = new Course("CIT904", "test", professorTest, "W", LocalTime.of(2, 00), LocalTime.of(3, 00), 30);

        Course course12 = new Course("CIT905", "test", professorTest, "W", LocalTime.of(3, 01), LocalTime.of(3, 59), 30);
        Course course13 = new Course("CIT905", "test", professorTest, "W", LocalTime.of(2, 59), LocalTime.of(4, 01), 30);
        Course course14 = new Course("CIT905", "test", professorTest, "W", LocalTime.of(3, 01), LocalTime.of(4, 00), 30);
        Course course15 = new Course("CIT905", "test", professorTest, "W", LocalTime.of(3, 00), LocalTime.of(4, 01), 30);

        // test next course
        // 9 10
        assertFalse(course10.conflictWith(course9));
        assertFalse(course9.conflictWith(course10));
        // 9 11
        assertFalse(course11.conflictWith(course9));
        assertFalse(course9.conflictWith(course11));

        // 9 12
        assertTrue(course12.conflictWith(course9));
        assertTrue(course9.conflictWith(course12));

        // 9 13
        assertTrue(course9.conflictWith(course13));
        assertTrue(course13.conflictWith(course9));

        // 9 14
        assertTrue(course14.conflictWith(course9));
        assertTrue(course9.conflictWith(course14));

        // 9 15
        assertTrue(course15.conflictWith(course9));
        assertTrue(course9.conflictWith(course15));




        // 10 15
        assertTrue(course15.conflictWith(course10));
        assertTrue(course10.conflictWith(course15));
        // 10 14
        assertFalse(course14.conflictWith(course10));
        assertFalse(course10.conflictWith(course14));
        // 10 13
        assertTrue(course13.conflictWith(course10));
        assertTrue(course10.conflictWith(course13));
        // 10 12
        assertFalse(course12.conflictWith(course10));
        assertFalse(course10.conflictWith(course12));
        // 10 11
        assertFalse(course11.conflictWith(course10));
        assertFalse(course10.conflictWith(course11));


        // 11 12
        assertFalse(course11.conflictWith(course12));
        assertFalse(course12.conflictWith(course11));

        // 11 13
        assertTrue(course11.conflictWith(course13));
        assertTrue(course13.conflictWith(course11));

        // 11 14
        assertFalse(course11.conflictWith(course14));
        assertFalse(course14.conflictWith(course11));

        // 11 15
        assertFalse(course15.conflictWith(course11));
        assertFalse(course11.conflictWith(course15));





    }


}
