@echo off
title AUCA Mentorship Portal - Master Console
color 0B

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

:menu
cls
echo =====================================================================
echo    AUCA MENTORSHIP PORTAL SYSTEM - DEVELOPER CONSOLE
echo =====================================================================
echo.
echo    [1] Start Mentorship SERVER
echo    [2] Start Mentorship CLIENT (UI)
echo    [3] Start BOTH (Server first, then Client UI after 4s)
echo    [4] Exit
echo.
echo =====================================================================
set /p choice="Enter your choice (1-4): "

if "%choice%"=="1" goto start_server
if "%choice%"=="2" goto start_client
if "%choice%"=="3" goto start_both
if "%choice%"=="4" goto exit
goto menu

:start_server
cls
echo [INFO] Starting AUCA Mentorship Server...
:: Pass our validated environment block down to the subshell
start cmd /k "title SERVER - AUCA Mentorship Portal && set JAVA_HOME=%JAVA_HOME% && set PATH=%PATH% && cd AUCAMentorshipPortalServer27185 && mvn compile exec:java -Dexec.mainClass=controller.MentorshipServer"
goto menu

:start_client
cls
echo [INFO] Starting AUCA Mentorship Client UI...
start cmd /k "title CLIENT - AUCA Mentorship Portal && set JAVA_HOME=%JAVA_HOME% && set PATH=%PATH% && cd AUCAMentorshipPortalClient27185 && mvn compile exec:java -Dexec.mainClass=MentorshipPortalClient"
goto menu

:start_both
cls
echo [INFO] Step 1: Launching RMI/Database Server...
start cmd /k "title SERVER - AUCA Mentorship Portal && set JAVA_HOME=%JAVA_HOME% && set PATH=%PATH% && cd AUCAMentorshipPortalServer27185 && mvn compile exec:java -Dexec.mainClass=controller.MentorshipServer"
echo.
echo [INFO] Waiting 4 seconds for Server registry binding...
timeout /t 4 > nul
echo.
echo [INFO] Step 2: Launching Client UI...
start cmd /k "title CLIENT - AUCA Mentorship Portal && set JAVA_HOME=%JAVA_HOME% && set PATH=%PATH% && cd AUCAMentorshipPortalClient27185 && mvn compile exec:java -Dexec.mainClass=MentorshipPortalClient"
goto menu

:exit
exit
