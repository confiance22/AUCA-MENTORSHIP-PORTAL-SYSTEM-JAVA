-- AUCA Mentorship Portal System - Database Schema
-- Student: Confiance UFITAMAHORO
-- Student ID: 27185
-- Course: Java Programming
-- Date: May 2026

-- NOTE: Run this on PostgreSQL using DataGrip or pgAdmin
-- Make sure you have created the database first:
--   CREATE DATABASE auca_mentorship_portal_db;
-- Then connect to it and run this whole script.
-- It is safe to re-run because we drop everything at the top.

-- -------------------------------------------------------
-- Drop tables if they already exist (for clean re-run)
-- -------------------------------------------------------

DROP TABLE IF EXISTS notifications CASCADE;
DROP TABLE IF EXISTS program_enrollments CASCADE;
DROP TABLE IF EXISTS mentorship_sessions CASCADE;
DROP TABLE IF EXISTS mentorship_programs CASCADE;
DROP TABLE IF EXISTS mentor_profiles CASCADE;
DROP TABLE IF EXISTS users CASCADE;


-- -------------------------------------------------------
-- users table
-- This stores all users: admins, mentors, and mentees
-- -------------------------------------------------------

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(200) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15),
    role VARCHAR(20) NOT NULL DEFAULT 'MENTEE',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Note: I'm using VARCHAR for role instead of a PostgreSQL ENUM
-- because Hibernate sends enum values as plain strings and it causes
-- errors if we use custom ENUM types in PostgreSQL.


-- -------------------------------------------------------
-- mentor_profiles table
-- One mentor can have one profile (one-to-one with users)
-- -------------------------------------------------------

CREATE TABLE mentor_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    department VARCHAR(150) NOT NULL,
    expertise_areas TEXT NOT NULL,
    bio TEXT,
    years_of_experience INT NOT NULL DEFAULT 0,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


-- -------------------------------------------------------
-- mentorship_programs table
-- Programs created by admins that mentors and mentees join
-- -------------------------------------------------------

CREATE TABLE mentorship_programs (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    max_capacity INT NOT NULL DEFAULT 10,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_by BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    CHECK (end_date > start_date),
    CHECK (max_capacity > 0)
);


-- -------------------------------------------------------
-- program_enrollments table
-- This is a join table for users enrolled in programs
-- (many-to-many relationship)
-- -------------------------------------------------------

CREATE TABLE program_enrollments (
    program_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    enrolled_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (program_id, user_id),
    FOREIGN KEY (program_id) REFERENCES mentorship_programs(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


-- -------------------------------------------------------
-- mentorship_sessions table
-- A session is a meeting between a mentor and a mentee
-- -------------------------------------------------------

CREATE TABLE mentorship_sessions (
    id BIGSERIAL PRIMARY KEY,
    mentor_id BIGINT NOT NULL,
    mentee_id BIGINT NOT NULL,
    program_id BIGINT,
    scheduled_at TIMESTAMP NOT NULL,
    duration_minutes INT NOT NULL DEFAULT 60,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    agenda TEXT,
    notes TEXT,
    mentor_feedback TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (mentor_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (mentee_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (program_id) REFERENCES mentorship_programs(id) ON DELETE SET NULL,
    CHECK (mentor_id <> mentee_id),   -- a mentor cannot have a session with themselves
    CHECK (duration_minutes > 0)
);


-- -------------------------------------------------------
-- notifications table
-- Used for general notifications and also OTP verification
-- -------------------------------------------------------

CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(30) NOT NULL DEFAULT 'GENERAL',
    otp_code VARCHAR(10),        -- only used when type = 'OTP'
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    is_used BOOLEAN NOT NULL DEFAULT FALSE,
    expires_at TIMESTAMP,        -- for OTP expiry
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


-- -------------------------------------------------------
-- Indexes to make queries faster
-- -------------------------------------------------------

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_mentor_profiles_user ON mentor_profiles(user_id);
CREATE INDEX idx_sessions_mentor ON mentorship_sessions(mentor_id);
CREATE INDEX idx_sessions_mentee ON mentorship_sessions(mentee_id);
CREATE INDEX idx_sessions_status ON mentorship_sessions(status);
CREATE INDEX idx_notifications_user ON notifications(user_id);


-- -------------------------------------------------------
-- Admin account
-- The admin is created automatically when the server starts
-- (see util/DataSeeder.java). No need to insert manually.
--
-- Default credentials:
--   Email    : admin@auca.ac.rw
--   Password : Admin@1234
--
-- If you want to insert manually, first run GenerateAdminHash.java
-- to get the bcrypt hash, then use:
--
-- INSERT INTO users (first_name, last_name, email, password, phone_number, role, is_active)
-- VALUES ('System', 'Admin', 'admin@auca.ac.rw', '<paste hash here>', '0780000000', 'ADMIN', TRUE);
-- -------------------------------------------------------


-- Quick check: list all tables that were created
SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'public'
ORDER BY table_name;

-- Check if admin was seeded
SELECT id, first_name, last_name, email, role, is_active, created_at FROM users;
