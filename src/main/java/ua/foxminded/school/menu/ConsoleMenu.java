package ua.foxminded.school.menu;

import ua.foxminded.school.domain.Student;
import ua.foxminded.school.service.CourseService;
import ua.foxminded.school.service.GroupService;
import ua.foxminded.school.service.StudentService;

import java.util.*;

public class ConsoleMenu {

    private final StudentService studentService;
    private final GroupService groupService;
    private final CourseService courseService;
    private final Scanner scanner;
    private final Map<String, Runnable> commandMap = new LinkedHashMap<>();
    private final Map<String, String> commandDescriptions = new HashMap<>();
    private boolean running = true;

    public ConsoleMenu(StudentService studentService,
                       GroupService groupService,
                       CourseService courseService) {
        this.studentService = studentService;
        this.groupService = groupService;
        this.courseService = courseService;
        this.scanner = new Scanner(System.in);
        initializeCommands();
    }

    private void initializeCommands() {
        register("add", this::addStudent, "Add a new student");
        register("a", this::addStudent, null);

        register("delete", this::deleteStudentById, "Delete student by ID");
        register("d", this::deleteStudentById, null);

        register("assign", this::assignStudentToCourse, "Assign student to course");
        register("as", this::assignStudentToCourse, null);

        register("remove", this::removeStudentFromCourse, "Remove student from course");
        register("r", this::removeStudentFromCourse, null);

        register("find", this::findStudentsByCourse, "Find students by course name");
        register("f", this::findStudentsByCourse, null);

        register("groups", this::findGroupsByStudentCount, "List groups by student count");
        register("g", this::findGroupsByStudentCount, null);

        register("courses", this::listCourses, "List all courses");
        register("c", this::listCourses, null);

        register("help", this::printHelp, "Show this help");
        register("exit", () -> {
            System.out.println("Exiting...");
            running = false;
        }, "Exit program");
    }

    private void register(String command, Runnable action, String description) {
        commandMap.put(command, action);
        if (description != null) {
            commandDescriptions.put(command, description);
        }
    }

    public void start() {
        running = true;
        while (running) {
            System.out.print("School App > ");
            String input = scanner.nextLine().trim().toLowerCase();

            Runnable action = commandMap.get(input);
            if (action != null) {
                try {
                    action.run();
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                System.out.println("Unknown command: '" + input + "'");
                printHelp();
            }
        }
    }

    private void printHelp() {
        System.out.println("Available commands:");
        Set<String> printed = new HashSet<>();
        for (Map.Entry<String, String> entry : commandDescriptions.entrySet()) {
            String alias = entry.getKey();
            String desc = entry.getValue();
            if (!printed.contains(desc)) {
                System.out.printf("%-10s - %s%n", alias, desc);
                printed.add(desc);
            }
        }
    }

    private void addStudent() {
        System.out.print("Enter first name: ");
        String first = scanner.nextLine();
        System.out.print("Enter last name: ");
        String last = scanner.nextLine();
        studentService.createStudent(first, last);
        System.out.println("Student added.");
    }

    private void deleteStudentById() {
        int id = readPositiveInt("Enter student ID: ");
        studentService.deleteStudentById(id);
        System.out.println("Student deleted.");
    }

    private void assignStudentToCourse() {
        int studentId = readPositiveInt("Enter student ID: ");
        int courseId = readPositiveInt("Enter course ID: ");
        studentService.assignToCourse(studentId, courseId);
        System.out.println("Assigned.");
    }

    private void removeStudentFromCourse() {
        int studentId = readPositiveInt("Enter student ID: ");
        int courseId = readPositiveInt("Enter course ID: ");
        studentService.removeFromCourse(studentId, courseId);
        System.out.println("Removed.");
    }

    private void findStudentsByCourse() {
        System.out.print("Enter course name: ");
        String name = scanner.nextLine().trim();

        try {
            List<Student> students = studentService.findStudentsByCourse(name);
            if (students.isEmpty()) {
                System.out.println("No students enrolled in course: " + name);
            } else {
                students.forEach(s -> System.out.printf("ID: %d | %s %s%n",
                        s.getStudentId(), s.getFirstName(), s.getLastName()));
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void findGroupsByStudentCount() {
        int count = readPositiveInt("Enter max number of students: ");
        groupService.findGroupsWithLessOrEqualStudents(count)
            .forEach(g -> System.out.printf("ID: %d | %s%n", g.getGroupId(), g.getGroupName()));
    }

    private void listCourses() {
        courseService.findAllCourses()
            .forEach(c -> System.out.printf("ID: %d | %s - %s%n",
                c.getCourseId(), c.getCourseName(), c.getCourseDescription()));
    }

    private int readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                int value = Integer.parseInt(input);
                if (value < 0) {
                    System.out.println("Value must not be negative.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid integer.");
            }
        }
    }
}
