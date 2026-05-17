@echo off
title SERVER - AUCA Mentorship Portal
color 0A
echo ===================================================
echo   AUCA MENTORSHIP PORTAL SYSTEM - SERVER RUNNER
echo ===================================================
echo.
echo [1/2] Changing directory...
cd AUCAMentorshipPortalServer27185
echo [2/2] Compiling and starting Mentorship RMI Server...
call mvn compile exec:java -Dexec.mainClass="controller.MentorshipServer"
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Failed to start Server. Please verify Java and Maven are in your PATH.
    pause
)
