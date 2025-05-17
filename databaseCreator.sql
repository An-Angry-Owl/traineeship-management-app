CREATE DATABASE IF NOT EXISTS `myy803_traineshipApp`;
USE `myy803_traineshipApp`;

-- Create the USERS table
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role ENUM('STUDENT','COMPANY','PROFESSOR','COMMITTEE') NOT NULL,
                       enabled BOOLEAN DEFAULT TRUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create the STUDENTS table (one-to-one with users)
CREATE TABLE students (
                          id BIGINT PRIMARY KEY,
                          full_name VARCHAR(100) NOT NULL,
                          university_id VARCHAR(50),
                          interests TEXT,
                          skills TEXT,
                          preferred_location VARCHAR(100),
                          CONSTRAINT fk_students_users FOREIGN KEY (id) REFERENCES users(id)
);

-- Create the COMPANIES table (one-to-one with users)
CREATE TABLE companies (
                           id BIGINT PRIMARY KEY,
                           company_name VARCHAR(100) NOT NULL,
                           location VARCHAR(100),
                           CONSTRAINT fk_companies_users FOREIGN KEY (id) REFERENCES users(id)
);

-- Create the PROFESSORS table (one-to-one with users)
CREATE TABLE professors (
                            id BIGINT PRIMARY KEY,
                            full_name VARCHAR(100) NOT NULL,
                            interests TEXT,
                            CONSTRAINT fk_professors_users FOREIGN KEY (id) REFERENCES users(id)
);

CREATE TABLE committee (
                           id BIGINT PRIMARY KEY,
                           committee_name VARCHAR(100) NOT NULL,
                           CONSTRAINT fk_committees_users FOREIGN KEY (id) REFERENCES users(id)
);

-- Create the TRAINEESHIP_POSITIONS table
CREATE TABLE traineeship_positions (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       company_id BIGINT NOT NULL,
                                       student_id BIGINT UNIQUE , -- Nullable: position may not be assigned yet, each student can be assigned to 1 position
                                       professor_id BIGINT, -- Nullable: supervisor may not be assigned yet
                                       start_date DATE,
                                       end_date DATE,
                                       description TEXT,
                                       required_skills TEXT,
                                       topics TEXT,
                                       status ENUM('CLOSED', 'OPEN', 'ASSIGNED', 'IN_PROGRESS', 'COMPLETED'),
                                       CONSTRAINT fk_positions_company FOREIGN KEY (company_id) REFERENCES companies(id),
                                       CONSTRAINT fk_positions_student FOREIGN KEY (student_id) REFERENCES students(id),
                                       CONSTRAINT fk_positions_professor FOREIGN KEY (professor_id) REFERENCES professors(id)
);

-- Create the EVALUATIONS table (one-to-0..1 relationship with traineeship_positions)
CREATE TABLE evaluations (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             traineeship_position_id BIGINT NOT NULL UNIQUE,
                             prof_motivation_rating TINYINT,
                             prof_effectiveness_rating TINYINT,
                             prof_efficiency_rating TINYINT,
                             comp_motivation_rating TINYINT,
                             comp_effectiveness_rating TINYINT,
                             comp_efficiency_rating TINYINT,
                             final_mark ENUM('PASS','FAIL','PENDING') DEFAULT 'PENDING',
                             CONSTRAINT fk_evaluations_position FOREIGN KEY (traineeship_position_id) REFERENCES traineeship_positions(id)
);

-- Create the TRAINEESHIP_APPLICATIONS table
CREATE TABLE traineeship_applications (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          student_id BIGINT NOT NULL,
                                          position_id BIGINT NOT NULL,
                                          application_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          status ENUM('PENDING','ACCEPTED','REJECTED') DEFAULT 'PENDING',
                                          CONSTRAINT fk_applications_student FOREIGN KEY (student_id) REFERENCES students(id),
                                          CONSTRAINT fk_applications_position FOREIGN KEY (position_id) REFERENCES traineeship_positions(id)
);

-- Create the LOGBOOK_ENTRIES table
CREATE TABLE logbook_entries (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 student_id BIGINT NOT NULL,
                                 position_id BIGINT NOT NULL,
                                 entry_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 content TEXT,
                                 CONSTRAINT fk_logbook_student FOREIGN KEY (student_id) REFERENCES students(id),
                                 CONSTRAINT fk_logbook_position FOREIGN KEY (position_id) REFERENCES traineeship_positions(id)
);
