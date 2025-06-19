package ua.foxminded.school.menu;

import org.junit.jupiter.api.*;
import ua.foxminded.school.config.AppConfig;
import ua.foxminded.school.dao.jdbc.*;
import ua.foxminded.school.domain.Student;
import ua.foxminded.school.service.*;
import ua.foxminded.school.service.impl.*;
import ua.foxminded.school.util.DatabaseInitializer;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleMenuIntegrationTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream output;

    private StudentService studentService;
    private GroupService groupService;
    private CourseService courseService;

    private static final String TEST_COURSE_NAME = "Math";
    private static final String TEST_COURSE_DESC = "Basic algebra";
    private int courseId;

    @BeforeEach
    void setup() throws Exception {
        AppConfig config = new AppConfig();
        new DatabaseInitializer(config).initialize();

        Connection conn = DriverManager.getConnection(
            config.getDbUrl(),
            config.getDbUser(),
            config.getDbPassword()
        );

        CourseDaoImpl courseDao = new CourseDaoImpl(conn);
        StudentDaoImpl studentDao = new StudentDaoImpl(conn);
        GroupDaoImpl groupDao = new GroupDaoImpl(conn);

        courseService = new CourseServiceImpl(courseDao);
        studentService = new StudentServiceImpl(studentDao, groupDao, courseService);
        groupService = new GroupServiceImpl(groupDao);

        if (!courseService.existsByName(TEST_COURSE_NAME)) {
            courseService.createCourse(TEST_COURSE_NAME, TEST_COURSE_DESC);
        }

        courseId = courseService.findAllCourses().stream()
            .filter(c -> TEST_COURSE_NAME.equals(c.getCourseName()))
            .findFirst()
            .get()
            .getCourseId();

        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    private void runWithInput(String... lines) {
        String input = String.join(System.lineSeparator(), lines) + System.lineSeparator();
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ConsoleMenu menu = new ConsoleMenu(studentService, groupService, courseService);

        Thread thread = new Thread(menu::start);
        thread.start();

        try {
            thread.join(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted.");
        }
    }

    @Test
    void testAddStudent_successfullyAddsStudent() {
        runWithInput("add", "Alice", "Smith", "exit");

        boolean exists = studentService.findAllStudents()
            .stream()
            .anyMatch(s -> "Alice".equals(s.getFirstName()) && "Smith".equals(s.getLastName()));

        assertTrue(exists, "Student should be added");
    }

    @Test
    void testDeleteStudent_removesStudentById() {
        Student student = studentService.createStudent("Bob", "Miller");

        runWithInput("delete", String.valueOf(student.getStudentId()), "exit");

        boolean exists = studentService.findAllStudents()
            .stream()
            .anyMatch(s -> Objects.equals(s.getStudentId(), student.getStudentId()));

        assertFalse(exists, "Student should be deleted");
    }

    @Test
    void testAssignStudentToCourse_createsRelation() {
        Student student = studentService.createStudent("Tom", "Sawyer");

        runWithInput("assign",
                String.valueOf(student.getStudentId()),
                String.valueOf(courseId),
                "exit");

        List<Student> students = studentService.findStudentsByCourse(TEST_COURSE_NAME);
        assertTrue(students.stream()
            .anyMatch(s -> Objects.equals(s.getStudentId(), student.getStudentId())));
    }

    @Test
    void testRemoveStudentFromCourse_removesRelation() {
        Student student = studentService.createStudent("Ann", "Lee");
        studentService.assignToCourse(student.getStudentId(), courseId);

        runWithInput("remove",
                String.valueOf(student.getStudentId()),
                String.valueOf(courseId),
                "exit");

        List<Student> students = studentService.findStudentsByCourse(TEST_COURSE_NAME);
        assertFalse(students.stream()
            .anyMatch(s -> Objects.equals(s.getStudentId(), student.getStudentId())));
    }

    @Test
    void testFindStudentsByCourse_returnsExpectedStudents() {
        Student student = studentService.createStudent("Greg", "Stone");
        studentService.assignToCourse(student.getStudentId(), courseId);

        runWithInput("find", TEST_COURSE_NAME, "exit");

        String outputText = output.toString();
        assertTrue(outputText.contains("Greg") && outputText.contains("Stone"),
            "Expected student listed in output");
    }

    @Test
    void testUnknownCommand_printsHelp() {
        runWithInput("unknowncommand", "exit");

        String out = output.toString();
        assertTrue(out.contains("Available commands"), "Should print help");
    }
}
