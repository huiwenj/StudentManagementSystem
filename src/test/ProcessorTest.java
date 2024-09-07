package test;

import constant.Constant;
import courses.Course;
import database.Database;
import org.junit.Before;
import org.junit.Test;
import processor.Processor;
import roles.Admin;
import roles.Professor;
import roles.Student;

import java.time.LocalTime;

import static org.junit.Assert.*;

/**
 * @author Xuanhe Zhang, Huiwen Jia, Haoran Hua
 */
public class ProcessorTest {
    @Before
    public void setUp(){
        Database.reset();
    }

    @Test
    public void testLogin(){
        Processor processor = new Processor();
        Database database = Database.getInstance();
        database.addUser(new Student("999", "jos", "jos", "hi"));
        database.addUser(new Professor("998", "jos", "jos1", "hi"));
        database.addUser(new Admin("997", "jos", "jos2", "hi"));
        assertTrue(processor.login(Constant.STUDENT_TYPE, "jos", "hi"));
        assertFalse(processor.login(Constant.STUDENT_TYPE, "jos", "hi1"));
        assertTrue(processor.login(Constant.PROFESSOR_TYPE, "jos1", "hi"));
        assertFalse(processor.login(Constant.PROFESSOR_TYPE, "jos1", "hi1"));
        assertTrue(processor.login(Constant.ADMIN_TYPE, "jos2", "hi"));
        assertFalse(processor.login(Constant.ADMIN_TYPE, "jos2", "hi1"));

    }
    @Test
    public void testGetStudentById(){
        Processor processor = new Processor();
        Database database = Database.getInstance();

        database.addUser(new Student("299", "jos", "jos3", "hi"));
        assertEquals(processor.getStudentById("299").getName(), "jos");

        // test null
        Student nullPtr = processor.getStudentById(null);
        assertNull(nullPtr);
    }

    @Test
    public void testGetCourseById(){
        Processor processor = new Processor();
        Database database = Database.getInstance();

        database.addCourse(new Course("199", "jos", new Professor("101", "t", "t", "t"), "W", LocalTime.of(3, 03), LocalTime.of(4, 04), 30));
        assertEquals(processor.getCourseById("199").getName(), "jos");

        // test null
        Course nullPtr = processor.getCourseById(null);
        assertNull(nullPtr);
    }
}
