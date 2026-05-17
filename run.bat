@echo off
title AUCA Mentorship Portal - Master Console
:: Set elegant developer cyan colors
color 0B

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
echo [INFO] Starting AUCA Mentorship Server in a new window...
start cmd /k "title SERVER - AUCA Mentorship Portal && cd AUCAMentorshipPortalServer27185 && mvn compile exec:java -Dexec.mainClass="controller.MentorshipServer""
goto menu

:start_client
cls
echo [INFO] Starting AUCA Mentorship Client UI in a new window...
start cmd /k "title CLIENT - AUCA Mentorship Portal && cd AUCAMentorshipPortalClient27185 && mvn compile exec:java -Dexec.mainClass="MentorshipPortalClient""
goto menu

:start_both
cls
echo [INFO] Step 1: Launching RMI/Database Server...
start cmd /k "title SERVER - AUCA Mentorship Portal && cd AUCAMentorshipPortalServer27185 && mvn compile exec:java -Dexec.mainClass="controller.MentorshipServer""
echo.
echo [INFO] Waiting 4 seconds for Server registry binding to complete...
timeout /t 4 > nul
echo.
echo [INFO] Step 2: Launching Client UI...
start cmd /k "title CLIENT - AUCA Mentorship Portal && cd AUCAMentorshipPortalClient27185 && mvn compile exec:java -Dexec.mainClass="MentorshipPortalClient""
goto menu

:exit
exit
