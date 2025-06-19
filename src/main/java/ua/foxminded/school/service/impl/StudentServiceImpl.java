package ua.foxminded.school.service.impl;

import ua.foxminded.school.dao.GroupDao;
import ua.foxminded.school.dao.StudentDao;
import ua.foxminded.school.domain.Student;
import ua.foxminded.school.service.CourseService;
import ua.foxminded.school.service.StudentService;

import java.util.List;

public class StudentServiceImpl implements StudentService {

    private final StudentDao studentDao;
    private final GroupDao groupDao;
    private final CourseService courseService;

    public StudentServiceImpl(StudentDao studentDao, GroupDao groupDao, CourseService courseService) {
        this.studentDao = studentDao;
        this.groupDao = groupDao;
        this.courseService = courseService;
    }

    @Override
    public Student createStudent(String firstName, String lastName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name must not be empty.");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name must not be empty.");
        }
        if (studentDao.existsByName(firstName.trim(), lastName.trim())) {
            throw new IllegalArgumentException("Student already exists");
        }
        return studentDao.save(new Student(0, null, firstName.trim(), lastName.trim()));
    }

    @Override
    public void deleteStudentById(int id) {
        if (!studentDao.existsById(id)) {
            throw new IllegalArgumentException("Student with ID " + id + " does not exist.");
        }
        studentDao.deleteById(id);
    }

    @Override
    public void assignToCourse(int studentId, int courseId) {
        if (!studentDao.existsById(studentId)) {
            throw new IllegalArgumentException("Student with ID " + studentId + " does not exist.");
        }
        if (!courseService.existsById(courseId)) {
            throw new IllegalArgumentException("Course with ID " + courseId + " does not exist.");
        }
        studentDao.addStudentToCourse(studentId, courseId);
    }

    @Override
    public void removeFromCourse(int studentId, int courseId) {
        if (!studentDao.existsById(studentId)) {
            throw new IllegalArgumentException("Student with ID " + studentId + " does not exist.");
        }
        if (!courseService.existsById(courseId)) {
            throw new IllegalArgumentException("Course with ID " + courseId + " does not exist.");
        }
        studentDao.removeStudentFromCourse(studentId, courseId);
    }

    @Override
    public List<Student> findStudentsByCourse(String courseName) {
        if (!courseService.existsByName(courseName)) {
            throw new IllegalArgumentException("Course '" + courseName + "' does not exist.");
        }
        return studentDao.findByCourseName(courseName);
    }

    @Override
    public List<Student> findAllStudents() {
        return studentDao.findAll();
    }

    @Override
    public boolean existsById(int studentId) {
        return studentDao.existsById(studentId);
    }

    @Override
    public void assignToGroup(int studentId, int groupId) {
        if (!studentDao.existsById(studentId)) {
            throw new IllegalArgumentException("Student with ID " + studentId + " does not exist.");
        }
        // Можно добавить проверку группы, если необходимо
        studentDao.assignToGroup(studentId, groupId);
    }
}
