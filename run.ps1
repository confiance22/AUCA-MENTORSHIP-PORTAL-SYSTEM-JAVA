# PowerShell Dev Console for AUCA Mentorship Portal
$Host.UI.RawUI.WindowTitle = "AUCA Mentorship Portal - PowerShell Master Console"

# 🔍 Intelligent JDK Auto-Detector
$JdkPath = "C:\Program Files\Apache NetBeans\jdk"
$ValidJdk = $false
if ($env:JAVA_HOME -and (Test-Path "$env:JAVA_HOME\bin\javac.exe")) {
    $ValidJdk = $true
}
if (-not $ValidJdk) {
    if (Test-Path $JdkPath) {
        $env:JAVA_HOME = $JdkPath
        $env:Path = "$JdkPath\bin;" + $env:Path
    }
}

# 🔍 Intelligent Maven Auto-Detector
$MvnPath = "C:\Program Files\Apache NetBeans\java\maven\bin"
if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
    if (Test-Path $MvnPath) {
        $env:Path += ";$MvnPath"
    } else {
        Clear-Host
        Write-Host "=====================================================================" -ForegroundColor Red
        Write-Host "[ERROR] Maven (mvn) is not recognized on your system." -ForegroundColor Red
        Write-Host ""
        Write-Host "Please ensure Apache NetBeans or Maven is installed." -ForegroundColor Yellow
        Write-Host "If already installed, add Maven's 'bin' folder to your system PATH." -ForegroundColor Yellow
        Write-Host "=====================================================================" -ForegroundColor Red
        Write-Host ""
        Read-Host "Press Enter to exit..."
        exit
    }
}

function Show-Menu {
    Clear-Host
    Write-Host "=====================================================================" -ForegroundColor Cyan
    Write-Host "    AUCA MENTORSHIP PORTAL SYSTEM - POWERSHELL DEV CONSOLE" -ForegroundColor Cyan
    Write-Host "=====================================================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "    [1] Start Mentorship SERVER" -ForegroundColor Gray
    Write-Host "    [2] Start Mentorship CLIENT (UI)" -ForegroundColor Gray
    Write-Host "    [3] Start BOTH (Server first, then Client UI after 4s)" -ForegroundColor Gray
    Write-Host "    [4] Exit" -ForegroundColor Gray
    Write-Host ""
    Write-Host "=====================================================================" -ForegroundColor Cyan
}

do {
    Show-Menu
    $choice = Read-Host "Enter your choice (1-4)"
    
    switch ($choice) {
        "1" {
            Clear-Host
            Write-Host "[INFO] Starting AUCA Mentorship Server..." -ForegroundColor Green
            Start-Process cmd -ArgumentList "/k title SERVER && cd AUCAMentorshipPortalServer27185 && set JAVA_HOME=$env:JAVA_HOME && set PATH=%PATH%;$MvnPath && mvn compile exec:java -Dexec.mainClass=controller.MentorshipServer"
            break
        }
        "2" {
            Clear-Host
            Write-Host "[INFO] Starting AUCA Mentorship Client UI..." -ForegroundColor Green
            Start-Process cmd -ArgumentList "/k title CLIENT && cd AUCAMentorshipPortalClient27185 && set JAVA_HOME=$env:JAVA_HOME && set PATH=%PATH%;$MvnPath && mvn compile exec:java -Dexec.mainClass=MentorshipPortalClient"
            break
        }
        "3" {
            Clear-Host
            Write-Host "[INFO] Step 1: Launching RMI/Database Server..." -ForegroundColor Green
            Start-Process cmd -ArgumentList "/k title SERVER && cd AUCAMentorshipPortalServer27185 && set JAVA_HOME=$env:JAVA_HOME && set PATH=%PATH%;$MvnPath && mvn compile exec:java -Dexec.mainClass=controller.MentorshipServer"
            Write-Host ""
            Write-Host "[INFO] Waiting 4 seconds for registry binding..." -ForegroundColor Yellow
            Start-Sleep -Seconds 4
            Write-Host ""
            Write-Host "[INFO] Step 2: Launching Client UI..." -ForegroundColor Green
            Start-Process cmd -ArgumentList "/k title CLIENT && cd AUCAMentorshipPortalClient27185 && set JAVA_HOME=$env:JAVA_HOME && set PATH=%PATH%;$MvnPath && mvn compile exec:java -Dexec.mainClass=MentorshipPortalClient"
            break
        }
        "4" {
            exit
        }
    }
    Write-Host ""
    Read-Host "Press Enter to return to menu..."
} while ($true)
