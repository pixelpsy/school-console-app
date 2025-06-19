# 🎓 School Console App

School Console App is a simple, console-based Java application that manages students, groups, and courses using **plain JDBC** and **PostgreSQL**. It supports a rich set of operations via an intuitive command-line interface, including student enrollment, course assignments, and more.

---

## Technologies Used

| Technology     | Version     | Description                            |
|----------------|-------------|----------------------------------------|
| Java           | 8           | Programming language                   |
| Maven          | Latest      | Build and dependency management        |
| PostgreSQL     | 17          | Relational database                    |
| JDBC           | Built-in    | Direct connection to DB via driver     |
| JUnit 5        | 5.x         | Unit and integration testing           |
| Mockito        | 5.x         | Mocking framework for unit tests       |

---

## Project Structure

```plaintext
src/
├── main/
│   ├── java/
│   │   ├── ua.foxminded.school.app          # Entry point (SchoolApp.java)
│   │   ├── ua.foxminded.school.domain       # Data models (Student, Course, Group)
│   │   ├── ua.foxminded.school.dao          # DAO interfaces
│   │   ├── ua.foxminded.school.dao.jdbc     # JDBC implementations
│   │   ├── ua.foxminded.school.service      # Service layer interfaces
│   │   ├── ua.foxminded.school.service.impl # Service layer implementations
│   │   ├── ua.foxminded.school.menu         # ConsoleMenu UI
│   │   └── ua.foxminded.school.util/config  # Configuration and utilities
│   └── resources/
│       └── sql/                             # SQL schema/init scripts
├── test/
│   └── java/
│       └── ua.foxminded.school.*            # Unit & integration tests
```
## Features

- Add / delete students
- Assign students to courses
- Remove students from courses
- Find students by course name
- List groups with fewer or equal students than specified
- List all available courses
- Simple command-line interface:  
  Commands like `add`, `assign`, `delete`, `remove`, `find`, `groups`, `courses`, `help`, `exit`

## Custom Improvements

- Console UI reworked using `Map<String, Runnable>` for **SOLID-compliant** command handling
- Full **validation in Service Layer**, ensuring proper data integrity
- All `System.out` and `System.in` interactions **mocked** in integration tests
- Replaced risky `Integer == Integer` comparisons with `Objects.equals(...)` for reliability
- Database auto-initialization with SQL (`init.sql`)
- Flag-controlled test data generation (`java SchoolApp init`)
- Unit and full **integration tests** for console & DAO interactions
