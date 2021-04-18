DROP TABLE IF EXISTS students_courses;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS courses;
DROP SEQUENCE IF EXISTS groups_id_seq;
DROP SEQUENCE IF EXISTS students_id_seq;
DROP SEQUENCE IF EXISTS courses_id_seq;

CREATE SEQUENCE groups_id_seq START 101;

CREATE TABLE groups
(
    id   int        NOT NULL PRIMARY KEY,
    name varchar(5) NOT NULL
);

ALTER TABLE groups
    ALTER COLUMN id
        SET DEFAULT NEXTVAL('groups_id_seq');

CREATE SEQUENCE students_id_seq START 1;

CREATE TABLE students
(
    id   int        NOT NULL PRIMARY KEY,
    group_id   int,
    first_name varchar(50) NOT NULL,
    last_name  varchar(50) NOT NULL,
    FOREIGN KEY (group_id)
        REFERENCES groups (id)
        ON DELETE CASCADE
);

ALTER TABLE students
    ALTER COLUMN id
        SET DEFAULT NEXTVAL('students_id_seq');

CREATE SEQUENCE courses_id_seq START 1;

CREATE TABLE courses
(
    id   int        NOT NULL PRIMARY KEY,
    name        varchar(50) NOT NULL,
    description text        NOT NULL
);

ALTER TABLE courses
    ALTER COLUMN id
        SET DEFAULT NEXTVAL('courses_id_seq');

CREATE TABLE students_courses
(
    student_id int NOT NULL,
    course_id  int NOT NULL,
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id)
        REFERENCES students (id)
        ON DELETE CASCADE,
    FOREIGN KEY (course_id)
        REFERENCES courses (id)
        ON DELETE CASCADE
);
