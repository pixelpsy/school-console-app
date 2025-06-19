package ua.foxminded.school.service;

import ua.foxminded.school.domain.Student;

import java.util.List;

public interface StudentService {
    Student createStudent(String firstName, String lastName);
    void deleteStudentById(int id);
    void assignToCourse(int studentId, int courseId);
    void removeFromCourse(int studentId, int courseId);
    List<Student> findStudentsByCourse(String courseName);
    List<Student> findAllStudents();
    boolean existsById(int studentId);
    void assignToGroup(int studentId, int groupId);
    
}
