-- ============================================================
-- AUCA Mentorship Portal System
-- Database: auca_mentorship_portal_db
-- Student: Confiance UFITAMAHORO | ID: 27185
-- ============================================================
-- HOW TO USE:
--   1. Connect to PostgreSQL in DataGrip
--   2. Make sure the database exists:
--      CREATE DATABASE auca_mentorship_portal_db;
--   3. Connect to auca_mentorship_portal_db
--   4. Run this entire script (it is safe to re-run — it drops everything first)
--   5. Run GenerateAdminHash.java in IntelliJ, copy the hash printed,
--      then run the UPDATE at the bottom of this file
-- ============================================================


-- ============================================================
-- STEP 1: DROP EVERYTHING (safe re-run)
-- ============================================================

DROP TABLE IF EXISTS notifications         CASCADE;
DROP TABLE IF EXISTS program_enrollments   CASCADE;
DROP TABLE IF EXISTS mentorship_sessions   CASCADE;
DROP TABLE IF EXISTS mentorship_programs   CASCADE;
DROP TABLE IF EXISTS mentor_profiles       CASCADE;
DROP TABLE IF EXISTS users                 CASCADE;


-- ============================================================
-- STEP 2: COLUMN TYPES
-- ============================================================
-- NOTE: We use VARCHAR instead of PostgreSQL custom ENUM types.
-- Hibernate 6 sends enum values as plain strings (VARCHAR), and
-- PostgreSQL custom ENUMs are incompatible with that approach.
-- Validation is handled at the Java/Hibernate layer instead.


-- ============================================================
-- STEP 3: TABLES
-- ============================================================

-- TABLE: users
CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    first_name      VARCHAR(100)    NOT NULL,
    last_name       VARCHAR(100)    NOT NULL,
    email           VARCHAR(200)    NOT NULL UNIQUE,
    password        VARCHAR(255)    NOT NULL,
    phone_number    VARCHAR(15),
    role            VARCHAR(20)     NOT NULL DEFAULT 'MENTEE',
    is_active       BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- TABLE: mentor_profiles (One-to-One with users)
CREATE TABLE mentor_profiles (
    id                  BIGSERIAL PRIMARY KEY,
    user_id             BIGINT          NOT NULL UNIQUE,
    department          VARCHAR(150)    NOT NULL,
    expertise_areas     TEXT            NOT NULL,
    bio                 TEXT,
    years_of_experience INT             NOT NULL DEFAULT 0,
    is_available        BOOLEAN         NOT NULL DEFAULT TRUE,
    updated_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_mentor_profile_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
);

-- TABLE: mentorship_programs
CREATE TABLE mentorship_programs (
    id              BIGSERIAL PRIMARY KEY,
    title           VARCHAR(200)    NOT NULL,
    description     TEXT,
    max_capacity    INT             NOT NULL DEFAULT 10,
    status          VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE',
    start_date      DATE            NOT NULL,
    end_date        DATE            NOT NULL,
    created_by      BIGINT,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_program_created_by
        FOREIGN KEY (created_by) REFERENCES users(id)
        ON DELETE SET NULL,
    CONSTRAINT chk_program_dates
        CHECK (end_date > start_date),
    CONSTRAINT chk_max_capacity
        CHECK (max_capacity > 0)
);

-- TABLE: program_enrollments (Many-to-Many: users <-> mentorship_programs)
CREATE TABLE program_enrollments (
    program_id      BIGINT      NOT NULL,
    user_id         BIGINT      NOT NULL,
    enrolled_at     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (program_id, user_id),
    CONSTRAINT fk_enrollment_program
        FOREIGN KEY (program_id) REFERENCES mentorship_programs(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_enrollment_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
);

-- TABLE: mentorship_sessions
CREATE TABLE mentorship_sessions (
    id                  BIGSERIAL PRIMARY KEY,
    mentor_id           BIGINT          NOT NULL,
    mentee_id           BIGINT          NOT NULL,
    program_id          BIGINT,
    scheduled_at        TIMESTAMP       NOT NULL,
    duration_minutes    INT             NOT NULL DEFAULT 60,
    status              VARCHAR(20)     NOT NULL DEFAULT 'SCHEDULED',
    agenda              TEXT,
    notes               TEXT,
    mentor_feedback     TEXT,
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_session_mentor
        FOREIGN KEY (mentor_id) REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_session_mentee
        FOREIGN KEY (mentee_id) REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_session_program
        FOREIGN KEY (program_id) REFERENCES mentorship_programs(id)
        ON DELETE SET NULL,
    CONSTRAINT chk_no_self_session
        CHECK (mentor_id <> mentee_id),
    CONSTRAINT chk_duration_positive
        CHECK (duration_minutes > 0)
);

-- TABLE: notifications
CREATE TABLE notifications (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT      NOT NULL,
    message     TEXT        NOT NULL,
    type        VARCHAR(30) NOT NULL DEFAULT 'GENERAL',
    otp_code    VARCHAR(10),
    is_read     BOOLEAN     NOT NULL DEFAULT FALSE,
    is_used     BOOLEAN     NOT NULL DEFAULT FALSE,
    expires_at  TIMESTAMP,
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
);


-- ============================================================
-- STEP 4: INDEXES (for faster queries)
-- ============================================================

CREATE INDEX idx_users_email          ON users(email);
CREATE INDEX idx_users_role           ON users(role);
CREATE INDEX idx_mentor_profiles_user ON mentor_profiles(user_id);
CREATE INDEX idx_sessions_mentor      ON mentorship_sessions(mentor_id);
CREATE INDEX idx_sessions_mentee      ON mentorship_sessions(mentee_id);
CREATE INDEX idx_sessions_status      ON mentorship_sessions(status);
CREATE INDEX idx_sessions_scheduled   ON mentorship_sessions(scheduled_at);
CREATE INDEX idx_notifications_user   ON notifications(user_id);
CREATE INDEX idx_notifications_type   ON notifications(type);
CREATE INDEX idx_programs_status      ON mentorship_programs(status);


-- ============================================================
-- STEP 5: SEED DATA — Admin Account
-- ============================================================
-- NOTE: The admin account is automatically created and hashed by
--       util/DataSeeder.java when the server starts for the first time.
--       You do NOT need to manually insert a hash here.
--
-- Admin credentials (handled by DataSeeder.java):
--   Email    : admin@auca.ac.rw
--   Password : Admin@1234
--
-- If you want to manually seed (optional), run GenerateAdminHash.java
-- in IntelliJ, copy the printed hash, then run:
--
-- INSERT INTO users (first_name, last_name, email, password, phone_number, role, is_active)
-- VALUES ('System', 'Admin', 'admin@auca.ac.rw', '$2a$10$YOUR_REAL_HASH', '0780000000', 'ADMIN', TRUE);


-- ============================================================
-- STEP 6: VERIFICATION
-- ============================================================

SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'public'
ORDER BY table_name;

SELECT id, first_name, last_name, email, role, is_active, created_at
FROM users;
