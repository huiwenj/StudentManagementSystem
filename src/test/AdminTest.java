package test;
import database.Database;
import processor.Processor;

import roles.Admin;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import java.time.LocalTime;


/**
 * @author Xuanhe Zhang, Huiwen Jia, Haoran Hua
 */
public class AdminTest {
    @Before
    public void setUp(){
        Database.reset();
        Processor processor = new Processor();
    }


    @Test
    public void testAddNewProfessor() {

        // Initialize Admin and Professor
        Admin admin = new Admin("001", "adminTest", "admin001", "adminPassword");
        // Test adding a new professor
        assertTrue(admin.addNewProfessor("New Professor", "newuser", "Prof002", "newpass"));

        // Test trying to add an existing professor
        assertFalse(admin.addNewProfessor("Insup Lee", "Lee", "030", "password590"));

        // Test adding a professor with incomplete information
        assertFalse(admin.addNewProfessor("", "", "", ""));

        // Test adding a professor with null values
        assertFalse(admin.addNewProfessor(null, null, null, null));
    }


    @Test
    public void testAddNewStudent() {
        // Initialize Admin and Professor
        Admin admin = new Admin("001", "adminTest", "admin001", "adminPassword");

        // add new student
        String studentName = "test student";
        String studentUsername = "test1";
        String studentId = "123";
        String studentPassword = "password";
        assertTrue(admin.addNewStudent(studentName, studentUsername, studentId, studentPassword));

        // add exist student
        String studentNameExisting = "StudentName1";
        String studentUsernameExisting = "testStudent01";
        String studentIdExisting = "001";
        String studentPasswordExisting = "password590";
        assertFalse(admin.addNewStudent(studentNameExisting, studentUsernameExisting, studentIdExisting, studentPasswordExisting));

        // testing empty
        assertFalse(admin.addNewStudent("", "test2", "124", "password"));
        assertFalse(admin.addNewStudent("Empty Name", "", "125", "password"));
        assertFalse(admin.addNewStudent("Empty ID", "test3", "", "password"));
        assertFalse(admin.addNewStudent("Empty Password", "test4", "126", ""));

        // testing null
        assertFalse(admin.addNewStudent(null, "testNull1", "135", "password"));
        assertFalse(admin.addNewStudent("Null Name", null, "136", "password"));
        assertFalse(admin.addNewStudent("Null ID", "testNull2", null, "password"));
        assertFalse(admin.addNewStudent("Null Password", "testNull3", "137", null));
    }

    @Test
    public void testDeleteProfessor() {
        // Initialize Admin and Professor
        Admin admin = new Admin("001", "adminTest", "admin001", "adminPassword");
        // deleting existing prof
        admin.addNewProfessor("1234", "test", "1234", "1234");
        assertTrue(admin.deleteProfessor("1234"));
        // deleting non-existing prof
        assertFalse(admin.deleteProfessor("NonExistentProf"));
        // deleting empty
        assertFalse(admin.deleteProfessor(""));
        // test null pointer
        assertFalse(admin.deleteProfessor(null));
    }

    @Test
    public void testDeleteStudent() {
        // Initialize Admin and Professor
        Admin admin = new Admin("001", "adminTest", "admin001", "adminPassword");

        // deleting existing stu
        assertTrue(admin.deleteStudent("001"));

        // deleting non-existing stu
        assertFalse(admin.deleteStudent("000"));

        // deleting empty
        assertFalse(admin.deleteStudent(""));

        // test delete invalid id
        assertFalse(admin.deleteStudent("999"));

        // test null pointer
        assertFalse(admin.deleteStudent(null));
    }


    @Test
    public void testAddNewCourse() {
        // Initialize Admin and Professor
        Admin admin = new Admin("001", "adminTest", "admin001", "adminPassword");

        // adding
        assertTrue(admin.addNewCourse("CIT600", "test course", "029", "MW",
                LocalTime.of(16, 0), LocalTime.of(16, 30), 30));
        // Adding conflict
        assertFalse(admin.addNewCourse("CIT594", "Conflict Course", "Prof001", "MW", LocalTime.of(9, 0), LocalTime.of(10, 30), 15));
        // trying to add non-existing prof
        assertFalse(admin.addNewCourse("CIT595", "NonExistent Prof Course", "NonExistentProf", "TTh", LocalTime.of(14, 0), LocalTime.of(15, 30), 25));
        // test null pointer
        assertFalse(admin.addNewCourse(null, null, null, null, null, null, 10));
    }

    @Test
    public void testDeleteCourse() {
        // Initialize Admin and Professor
        Admin admin = new Admin("001", "adminTest", "admin001", "adminPassword");

        // deleting existing course
        assertTrue(admin.deleteCourse("CIT591"));

        // deleting non-existing course
        assertFalse(admin.deleteCourse("NonExistentCourse"));

        // deleting empty
        assertFalse(admin.deleteCourse(""));

        // test null pointer
        assertFalse(admin.deleteCourse(null));


    }
}
