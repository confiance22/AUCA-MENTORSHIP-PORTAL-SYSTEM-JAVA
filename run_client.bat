@echo off
title CLIENT - AUCA Mentorship Portal
color 0E
echo ===================================================
echo   AUCA MENTORSHIP PORTAL SYSTEM - CLIENT RUNNER
echo ===================================================
echo.
echo [1/2] Changing directory...
cd AUCAMentorshipPortalClient27185
echo [2/2] Compiling and starting Mentorship Client UI...
call mvn compile exec:java -Dexec.mainClass="MentorshipPortalClient"
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Failed to start Client UI. Please verify Java and Maven are in your PATH.
    pause
)
