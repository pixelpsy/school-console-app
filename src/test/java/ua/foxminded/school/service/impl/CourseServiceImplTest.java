package ua.foxminded.school.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.school.dao.CourseDao;
import ua.foxminded.school.domain.Course;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    private CourseDao courseDao;
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        courseDao = mock(CourseDao.class);
        courseService = new CourseServiceImpl(courseDao);
    }

    @Test
    void findAllCourses_shouldReturnList_whenCoursesExist() {
        List<Course> expected = Arrays.asList(
            new Course(1, "Math", "Algebra and Geometry"),
            new Course(2, "Biology", "Human Anatomy")
        );

        when(courseDao.findAll()).thenReturn(expected);

        List<Course> actual = courseService.findAllCourses();

        assertEquals(2, actual.size());
        assertEquals("Math", actual.get(0).getCourseName());
        verify(courseDao).findAll();
    }

    @Test
    void findAllCourses_shouldReturnEmptyList_whenNoCoursesExist() {
        when(courseDao.findAll()).thenReturn(Collections.emptyList());

        List<Course> result = courseService.findAllCourses();

        assertTrue(result.isEmpty());
        verify(courseDao).findAll();
    }

    @Test
    void existsById_shouldReturnTrue_whenDaoReturnsTrue() {
        when(courseDao.existsById(10)).thenReturn(true);

        assertTrue(courseService.existsById(10));
        verify(courseDao).existsById(10);
    }

    @Test
    void existsById_shouldReturnFalse_whenDaoReturnsFalse() {
        when(courseDao.existsById(99)).thenReturn(false);

        assertFalse(courseService.existsById(99));
        verify(courseDao).existsById(99);
    }
}
