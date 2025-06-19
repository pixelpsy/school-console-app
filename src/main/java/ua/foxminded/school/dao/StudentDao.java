package ua.foxminded.school.dao;

import ua.foxminded.school.domain.Student;

import java.util.List;

public interface StudentDao {
    Student save(Student student);
    void deleteById(int id);
    void addStudentToCourse(int studentId, int courseId);
    void removeStudentFromCourse(int studentId, int courseId);
    List<Student> findByCourseName(String courseName);
    List<Student> findAll();
    boolean existsById(int studentId);
    void assignToGroup(int studentId, int groupId);
    boolean existsByName(String firstName, String lastName);

}
