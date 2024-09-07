package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.time.LocalTime;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import database.Database;
import files.FileInfoReader;
import courses.Course;
import roles.Admin;
import roles.Professor;
import roles.Student;

/**
 * @author Xuanhe Zhang, Huiwen Jia, Haoran Hua
 */
public class FileInfoReaderTest {
    @Before
    public void setUp(){
        Database database = Database.getInstance();
        Database.reset();
    }

    @Test
    public void testReadFiles(){
        FileInfoReader fileInfoReader = new FileInfoReader();
        fileInfoReader.readFiles();
        Database database = Database.getInstance();
        // test parse admin
        Admin admin = database.getAdminById("001");

        assertNotNull(admin);
        assertEquals("001", admin.getId());
        assertEquals("admin", admin.getName());
        assertEquals("admin01", admin.getUsername());
        assertEquals("password590", admin.getPassword());

        // test parse professor
        Professor professor = database.getProfessorById("001");

        assertNotNull(professor);
        assertEquals("001", professor.getId());
        assertEquals("Clayton Greenberg", professor.getName());
        assertEquals("Greenberg", professor.getUsername());
        assertEquals("password590", professor.getPassword());

        // test parse student
        Student student = database.getStudentById("001");

        assertNotNull(student);
        assertEquals("001", student.getId());
        assertEquals("StudentName1", student.getName());
        assertEquals("testStudent01", student.getUsername());
        assertEquals("password590", student.getPassword());

        // course map from the database
        Map<String, Course> courseMap = database.getCourseMap();

        assertNotNull(courseMap);

        // Update the expected value based on your actual number of courses
        assertEquals(50, courseMap.size());

        // Get and validate information for a specific course
        Course courseCit590 = courseMap.get("CIT590");
        assertNotNull(courseCit590);
        assertEquals("CIT590", courseCit590.getId());
        assertEquals("Programming Languages and Techniques", courseCit590.getName());
        assertEquals("Brandon L Krakowsky", courseCit590.getProfessor().getName());
        assertEquals("MW", courseCit590.getWeekDay());
        assertEquals(LocalTime.parse("16:30"), courseCit590.getStartTime());
        assertEquals(LocalTime.parse("18:00"), courseCit590.getEndTime());
        assertEquals(110, courseCit590.getCapacity());


    }
}
