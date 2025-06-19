package ua.foxminded.school.util;

import ua.foxminded.school.domain.Course;
import ua.foxminded.school.domain.Group;
import ua.foxminded.school.service.CourseService;
import ua.foxminded.school.service.GroupService;
import ua.foxminded.school.service.StudentService;

import java.util.*;

public class TestDataGenerator {

    private final StudentService studentService;
    private final GroupService groupService;
    private final CourseService courseService;

    private static final String[] FIRST_NAMES = {
        "Alice", "Bob", "Charlie", "David", "Emma", "Frank", "Grace",
        "Hannah", "Ivan", "Jack", "Kate", "Liam", "Mia", "Noah", "Olivia",
        "Paul", "Quinn", "Rose", "Sam", "Tina"
    };

    private static final String[] LAST_NAMES = {
        "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller",
        "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Jackson",
        "White", "Harris", "Martin", "Thompson", "Garcia", "Martinez", "Robinson"
    };

    private static final String[] COURSE_NAMES = {
        "Math", "Biology", "Physics", "Chemistry", "Literature",
        "History", "Geography", "Programming", "Art", "Philosophy"
    };

    public TestDataGenerator(StudentService studentService,
                             GroupService groupService,
                             CourseService courseService) {
        this.studentService = studentService;
        this.groupService = groupService;
        this.courseService = courseService;
    }

    public void generate() {
        Random random = new Random();

        // 1. Generate groups
        for (int i = 0; i < 10; i++) {
            String groupName = generateGroupName(random);
            groupService.createGroup(groupName);
        }

        // 2. Generate courses
        for (String course : COURSE_NAMES) {
            courseService.createCourse(course, course + " course description");
        }

        List<Group> groups = groupService.findAllGroups();
        List<Course> courses = courseService.findAllCourses();

        // 3. Generate students
        for (int i = 0; i < 200; i++) {
            String first = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
            String last = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
            studentService.createStudent(first, last);
        }

        List<Integer> studentIds = new ArrayList<>();
        studentService.findAllStudents().forEach(s -> studentIds.add(s.getStudentId()));

        // 4. Randomly assign students to groups
        for (Integer studentId : studentIds) {
            Group group = groups.get(random.nextInt(groups.size()));
            studentService.assignToGroup(studentId, group.getGroupId());
        }

        // 5. Assign courses to students
        for (Integer studentId : studentIds) {
            int count = 1 + random.nextInt(3);
            Set<Integer> assigned = new HashSet<>();
            while (assigned.size() < count) {
                Course course = courses.get(random.nextInt(courses.size()));
                if (assigned.add(course.getCourseId())) {
                    studentService.assignToCourse(studentId, course.getCourseId());
                }
            }
        }

        System.out.println("Test data generated successfully.");
    }

    private String generateGroupName(Random random) {
        char first = (char) ('A' + random.nextInt(26));
        char second = (char) ('A' + random.nextInt(26));
        int num = 10 + random.nextInt(90);
        return "" + first + second + "-" + num;
    }
}
