package ua.foxminded.school.service.impl;

import ua.foxminded.school.service.CourseService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.school.dao.CourseDao;
import ua.foxminded.school.dao.GroupDao;
import ua.foxminded.school.dao.StudentDao;
import ua.foxminded.school.domain.Student;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceImplTest {

    private StudentDao studentDao;
    private CourseDao courseDao;
    private GroupDao groupDao;
    private StudentServiceImpl studentService;
    private CourseService courseService;

    @BeforeEach
    void setup() {
        studentDao = mock(StudentDao.class);
        courseDao = mock(CourseDao.class);
        groupDao = mock(GroupDao.class);
        courseService = mock(CourseService.class);

        studentService = new StudentServiceImpl(studentDao, groupDao, courseService);
    }


    @Test
    void createStudent_shouldReturnSavedStudent_whenValidNamesGiven() {
        Student expected = new Student(null, "John", "Doe");

        when(studentDao.existsByName("John", "Doe")).thenReturn(false);
        when(studentDao.save(any(Student.class))).thenReturn(expected);

        Student actual = studentService.createStudent("John", "Doe");

        assertNotNull(actual);
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        verify(studentDao, times(1)).save(any(Student.class));
    }

    @Test
    void deleteStudentById_shouldCallDaoOnce_whenValidIdGiven() {
        int studentId = 42;

        when(studentDao.existsById(studentId)).thenReturn(true);

        studentService.deleteStudentById(studentId);

        verify(studentDao, times(1)).deleteById(studentId);
    }

    @Test
    void assignToCourse_shouldDelegateToDao_whenStudentAndCourseIdsAreValid() {
        when(studentDao.existsById(10)).thenReturn(true);
        when(courseService.existsById(20)).thenReturn(true);


        studentService.assignToCourse(10, 20);

        verify(studentDao).addStudentToCourse(10, 20);
    }
}
