DROP TABLE IF EXISTS students_courses;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS courses;

CREATE TABLE groups(
	id serial PRIMARY KEY,
	name varchar(5) NOT NULL,
);

CREATE TABLE students(
	id serial PRIMARY KEY,
	group_id int,
	first_name varchar(50) NOT NULL,
	last_name varchar(50) NOT NULL,
	FOREIGN KEY(group_id)
		REFERENCES groups(id)
);

CREATE TABLE courses(
	id serial PRIMARY KEY,
	name varchar(50) NOT NULL,
	description text NOT NULL,
);

CREATE TABLE students_courses(
	student_id int NOT NULL,
	course_id int NOT NULL,
	PRIMARY KEY(student_id, course_id), 
	FOREIGN KEY(student_id)
		REFERENCES students(id)
		ON DELETE CASCADE,
	FOREIGN KEY(course_id)
		REFERENCES courses(id)
		ON DELETE CASCADE
);
