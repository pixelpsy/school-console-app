package ua.foxminded.school.dao.jdbc;

import ua.foxminded.school.dao.CourseDao;
import ua.foxminded.school.domain.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDaoImpl implements CourseDao {

    private static final String COURSE_ID = "course_id";
    private static final String COURSE_NAME = "course_name";
    private static final String COURSE_DESCRIPTION = "course_description";

    private final Connection connection;

    public CourseDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT course_id, course_name, course_description FROM courses";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                courses.add(new Course(
                    rs.getInt(COURSE_ID),
                    rs.getString(COURSE_NAME),
                    rs.getString(COURSE_DESCRIPTION)
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch courses", e);
        }

        return courses;
    }

    @Override
    public boolean existsById(int courseId) {
        String sql = "SELECT 1 FROM courses WHERE course_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check course existence", e);
        }
    }

    @Override
    public Course save(Course course) {
        String sql = "INSERT INTO courses (course_name, course_description) VALUES (?, ?) RETURNING course_id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, course.getCourseName());
            stmt.setString(2, course.getCourseDescription());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    course.setCourseId(rs.getInt(COURSE_ID));
                    return course;
                } else {
                    throw new SQLException("Course insert failed, no ID returned.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert course", e);
        }
    }
    
    @Override
    public boolean existsByName(String courseName) {
        String sql = "SELECT 1 FROM courses WHERE LOWER(course_name) = LOWER(?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, courseName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check course by name", e);
        }
    }

}
