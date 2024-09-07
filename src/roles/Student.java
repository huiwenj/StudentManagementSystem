package roles;

import courses.Course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Student class is used to store information of a student user
 * @author Xuanhe Zhang, Huiwen Jia, Haoran Hua
 */
public class Student extends UserBase{
    // student enrolled courses
    List<String> coursesList;
    // grade map for courses
    Map<String, String> grades;
    public Student(String id, String name, String username, String password) {
        super(id, name, username, password);
        coursesList = new ArrayList<>();
        grades = new HashMap<>();
    }



    /**
     * add course based on the course ID
     * @param courseId of the course
     * @return true if the course if added successfully
     */
    public boolean enrollCourse(String courseId){
        // if course id is empty, return false
        if(courseId == null || courseId.isEmpty()){
            System.out.println("course cannot be empty");
            return false;
        }
        // finding course from database
        Course addCourse = database.getCourseById(courseId);
        // if failed, throw exception
        if(addCourse == null){
            System.out.println("no such course: " + courseId);
            return false;
        }
        // check whether student can enroll that course
        if(canEnroll(addCourse)){
            // if could, add student to course
            addCourse.addStudent(this);
            // add course to the list, return whether success
            return coursesList.add(courseId);
        }
        // otherwise return false
        return false;
    }

    /**
     * add course based on its ID and grade
     *
     * @param courseId of the added course
     * @param grade    of the added course
     */
    public void addCourseWithGrade(String courseId, String grade){
        // if course id or grade is empty, return false
        if(courseId == null || courseId.isEmpty() || grade == null || grade.isEmpty()){
            return;
        }

        grades.put(courseId, grade);


//        // try to enroll that course
//        if(enrollCourse(courseId)){
//            // add course grade to grade map
//            grades.put(courseId, grade);
//            return true;
//        }
//        // else return false
//        return false;
    }



    /**
     * make sure the course can be enrolled
     * @param course that needs to be enrolled
     * @return true if the course can be enrolled
     */
    private boolean canEnroll(Course course){
        // if course is null, return false
        if(course == null){
            return false;
        }
        // if course is duplicated, or course is conflict with another course, or courses have same name, return false
        for(String courseId: coursesList){
            Course c = database.getCourseById(courseId);
            if(c.getId().equals(course.getId())){
                System.out.println("Course ID conflicted");
                return false;
            } else if(course.conflictWith(c)){
                System.out.println("Course schedule conflicted");
                return false;
            } else if(course.getName().equals(c.getName())){
                System.out.println("Same course name conflicted");
            }
        }
        if(isFinished(course.getId())){
            System.out.println("Course already graded");
            return false;
        }
        // else return true
        return true;
    }


    /**
     * helper function for test whether course if finished
     * @param courseId  course id
     * @return  true if course is finished
     */
    private boolean isFinished(String courseId){
        if(courseId == null || courseId.isEmpty()){
            return false;
        }
        if(grades.containsKey(courseId)){
            System.out.println(courseId);
            return true;
        }

        Course course = database.getCourseById(courseId);
        if(course == null){
            return false;
        }

        Map<String, Course> allCourseMap = database.getAllCourses();

        for(Course c: allCourseMap.values()){
            if(course.getName().equals(c.getName()) && grades.containsKey(c.getId())){
                return true;
            }
        }
        return false;

    }

    /**
     * drop the course based on the course ID
     * @param courseId of the course
     * @return true if the course is dropped successfully
     */
    public boolean dropCourse(String courseId){
        if(courseId == null || courseId.isEmpty()){
            return false;
        }
        Course dropCourse = database.getCourseById(courseId);
        if(dropCourse == null){
            return false;
        }
        dropCourse.removeStudent(this);
        return coursesList.remove(courseId);
    }


    /**
     * get the list of courses
     * @return the list of courses
     */
    public List<String> getCoursesList(){
        return coursesList;
    }


    /**
     * get the map of grades
     * @return map of grades
     */
    public Map<String, String> getGrades(){
        return grades;
    }

    @Override
    public String toString(){
        return "Student: " + getName();
    }


}
