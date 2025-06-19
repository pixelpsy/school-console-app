package ua.foxminded.school.dao;

import ua.foxminded.school.domain.Course;

import java.util.List;

public interface CourseDao {
    List<Course> findAll();
    boolean existsById(int courseId);
    Course save(Course course);
    boolean existsByName(String courseName);
}
