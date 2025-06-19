package ua.foxminded.school.domain;

public class Course {
    private Integer courseId;
    private String courseName;
    private String courseDescription;

    public Course(Integer courseId, String courseName, String courseDescription) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    @Override
    public String toString() {
        return "Course{id=" + courseId + ", name=" + courseName + ", description=" + courseDescription + "}";
    }
}
