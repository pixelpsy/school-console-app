package ua.foxminded.school.service.impl;

import ua.foxminded.school.dao.CourseDao;
import ua.foxminded.school.domain.Course;
import ua.foxminded.school.service.CourseService;

import java.util.List;
import java.util.Objects;

public class CourseServiceImpl implements CourseService {

    private final CourseDao courseDao;

    public CourseServiceImpl(CourseDao courseDao) {
        this.courseDao = Objects.requireNonNull(courseDao);
    }

    @Override
    public List<Course> findAllCourses() {
        return courseDao.findAll();
    }

    @Override
    public boolean existsById(int courseId) {
        return courseDao.existsById(courseId);
    }

    @Override
    public void createCourse(String name, String description) {
        Course course = new Course(null, name, description);
        courseDao.save(course);
    }
    
    @Override
    public boolean existsByName(String courseName) {
        return courseDao.existsByName(courseName);
    }

}
