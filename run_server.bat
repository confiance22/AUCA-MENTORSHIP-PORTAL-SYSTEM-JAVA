@echo off
title SERVER - AUCA Mentorship Portal
color 0A

:: 🔍 Intelligent JDK Auto-Detector
set "VALID_JDK=0"
if not "%JAVA_HOME%"=="" (
    if exist "%JAVA_HOME%\bin\javac.exe" (
        set "VALID_JDK=1"
    )
)
if "%VALID_JDK%"=="0" (
    if exist "C:\Program Files\Apache NetBeans\jdk" (
        set "JAVA_HOME=C:\Program Files\Apache NetBeans\jdk"
        set "PATH=%JAVA_HOME%\bin;%PATH%"
    )
)

:: 🔍 Intelligent Maven Auto-Detector
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    if exist "C:\Program Files\Apache NetBeans\java\maven\bin" (
        set "PATH=%PATH%;C:\Program Files\Apache NetBeans\java\maven\bin"
    ) else (
        echo =====================================================================
        echo [ERROR] Maven (mvn) is not recognized on your system.
        echo.
        echo Please ensure Apache NetBeans or Maven is installed.
        echo If already installed, add Maven's 'bin' folder to your system PATH.
        echo =====================================================================
        echo.
        pause
        exit
    )
)

echo ===================================================
echo   AUCA MENTORSHIP PORTAL SYSTEM - SERVER RUNNER
echo ===================================================
echo.
echo [1/2] Changing directory...
cd AUCAMentorshipPortalServer27185
echo [2/2] Compiling and starting Mentorship RMI Server...
call mvn compile exec:java -Dexec.mainClass=controller.MentorshipServer
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Failed to start Server. Please verify Java and Maven setup.
    pause
)
