package ua.foxminded.school.service;

import ua.foxminded.school.domain.Course;

import java.util.List;

public interface CourseService {
    List<Course> findAllCourses();
    boolean existsById(int courseId);
    void createCourse(String name, String description);
    boolean existsByName(String courseName);
}
