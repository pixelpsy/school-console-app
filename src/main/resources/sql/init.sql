DROP TABLE IF EXISTS students_courses, students, courses, groups CASCADE;

CREATE TABLE groups (
    group_id SERIAL PRIMARY KEY,
    group_name VARCHAR(10)
);

CREATE TABLE students (
    student_id SERIAL PRIMARY KEY,
    group_id INT REFERENCES groups(group_id),
    first_name VARCHAR(50),
    last_name VARCHAR(50)
);

CREATE TABLE courses (
    course_id SERIAL PRIMARY KEY,
    course_name VARCHAR(50),
    course_description TEXT
);

CREATE TABLE students_courses (
    student_id INT REFERENCES students(student_id),
    course_id INT REFERENCES courses(course_id),
    PRIMARY KEY (student_id, course_id)
);
