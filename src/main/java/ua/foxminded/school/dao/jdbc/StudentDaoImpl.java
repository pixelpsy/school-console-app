package ua.foxminded.school.dao.jdbc;

import ua.foxminded.school.dao.StudentDao;
import ua.foxminded.school.domain.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao {

    private static final String STUDENT_ID = "student_id";
    private static final String GROUP_ID = "group_id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";

    private final Connection connection;

    public StudentDaoImpl(Connection connection) {
        this.connection = connection;
    }
    
    @Override
    public boolean existsByName(String firstName, String lastName) {
        String sql = "SELECT 1 FROM students WHERE LOWER(first_name) = LOWER(?) AND LOWER(last_name) = LOWER(?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check if student exists by name", e);
        }
    }


    @Override
    public Student save(Student student) {
        String sql = "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?) RETURNING student_id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (student.getGroupId() != null) {
                stmt.setInt(1, student.getGroupId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getLastName());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    student.setStudentId(rs.getInt(STUDENT_ID));
                    return student;
                }
            }

            throw new SQLException("Failed to insert student.");

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save student", e);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete student", e);
        }
    }

    @Override
    public List<Student> findByCourseName(String courseName) {
        List<Student> students = new ArrayList<>();
        String sql =
            "SELECT s.student_id, s.group_id, s.first_name, s.last_name " +
            "FROM students s " +
            "JOIN students_courses sc ON s.student_id = sc.student_id " +
            "JOIN courses c ON c.course_id = sc.course_id " +
            "WHERE c.course_name = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, courseName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(new Student(
                        rs.getInt(STUDENT_ID),
                        rs.getObject(GROUP_ID) != null ? rs.getInt(GROUP_ID) : null,
                        rs.getString(FIRST_NAME),
                        rs.getString(LAST_NAME)
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find students by course", e);
        }

        return students;
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) {
        String sql = "INSERT INTO students_courses (student_id, course_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to assign student to course", e);
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        String sql = "DELETE FROM students_courses WHERE student_id = ? AND course_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove student from course", e);
        }
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT student_id, group_id, first_name, last_name FROM students";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                students.add(new Student(
                    rs.getInt(STUDENT_ID),
                    rs.getObject(GROUP_ID) != null ? rs.getInt(GROUP_ID) : null,
                    rs.getString(FIRST_NAME),
                    rs.getString(LAST_NAME)
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all students", e);
        }

        return students;
    }

    @Override
    public boolean existsById(int studentId) {
        String sql = "SELECT 1 FROM students WHERE student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check if student exists", e);
        }
    }

    @Override
    public void assignToGroup(int studentId, int groupId) {
        String sql = "UPDATE students SET group_id = ? WHERE student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, groupId);
            stmt.setInt(2, studentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to assign student to group", e);
        }
    }
}
