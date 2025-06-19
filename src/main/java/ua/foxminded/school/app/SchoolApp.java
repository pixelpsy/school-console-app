package ua.foxminded.school.app;

import ua.foxminded.school.config.AppConfig;
import ua.foxminded.school.dao.jdbc.CourseDaoImpl;
import ua.foxminded.school.dao.jdbc.GroupDaoImpl;
import ua.foxminded.school.dao.jdbc.StudentDaoImpl;
import ua.foxminded.school.menu.ConsoleMenu;
import ua.foxminded.school.service.CourseService;
import ua.foxminded.school.service.GroupService;
import ua.foxminded.school.service.StudentService;
import ua.foxminded.school.service.impl.CourseServiceImpl;
import ua.foxminded.school.service.impl.GroupServiceImpl;
import ua.foxminded.school.service.impl.StudentServiceImpl;
import ua.foxminded.school.util.DatabaseInitializer;
import ua.foxminded.school.util.TestDataGenerator;

import java.sql.Connection;
import java.sql.DriverManager;

public class SchoolApp {

    public static void main(String[] args) {
        try {
            // 1. Load configuration
            AppConfig config = new AppConfig();
            String url = config.getDbUrl();
            String user = config.getDbUser();
            String pass = config.getDbPassword();

            // 2. Create DB connection
            Connection connection = DriverManager.getConnection(url, user, pass);

            // 3. Initialize DAOs
            StudentDaoImpl studentDao = new StudentDaoImpl(connection);
            GroupDaoImpl groupDao = new GroupDaoImpl(connection);
            CourseDaoImpl courseDao = new CourseDaoImpl(connection);

            // 4. Initialize Services
            CourseService courseService = new CourseServiceImpl(courseDao);
            StudentService studentService = new StudentServiceImpl(studentDao, groupDao, courseService);
            GroupService groupService = new GroupServiceImpl(groupDao);

            // 5. Check init flag
            boolean runInit = args.length > 0 && args[0].equalsIgnoreCase("init");
            if (runInit) {
                if (studentService.findAllStudents().isEmpty()) {
                    DatabaseInitializer dbInit = new DatabaseInitializer(config);
                    dbInit.initialize();

                    TestDataGenerator generator = new TestDataGenerator(studentService, groupService, courseService);
                    generator.generate();

                    System.out.println("Database initialized and test data inserted.");
                } else {
                    System.out.println("Database already has data. Skipping initialization.");
                }
            }

            // 6. Start console menu
            ConsoleMenu menu = new ConsoleMenu(studentService, groupService, courseService);
            menu.start();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fatal error: " + e.getMessage());
        }
    }
}
