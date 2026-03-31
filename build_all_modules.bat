@echo off
setlocal enabledelayedexpansion

echo ========================================
echo Starting build for all modules
echo ========================================
echo.

:: List of all modules from settings.gradle
set "modules=CustomToast util app junior middle storage senior custom group test device network event animation media weixin performance filedialog mixture customview hezi"

:: Create log file
set "LOG_FILE=build_all_modules_%date:~0,4%%date:~5,2%%date:~8,2%_%time:~0,2%%time:~3,2%%time:~6,2%.log"
set "LOG_FILE=%LOG_FILE: =0%"
echo Build started at %date% %time% > "%LOG_FILE%"
echo. >> "%LOG_FILE%"

:: Initialize counters
set /a SUCCESS_COUNT=0
set /a FAIL_COUNT=0
set /a SKIP_COUNT=0

:: Build each module
for %%m in (%modules%) do (
    echo.
    echo ========================================
    echo Building module: %%m
    echo ========================================

    echo. >> "%LOG_FILE%"
    echo ======================================== >> "%LOG_FILE%"
    echo Building module: %%m >> "%LOG_FILE%"
    echo ======================================== >> "%LOG_FILE%"

    :: Check if module directory exists
    if not exist "%%m" (
        echo [SKIP] Module directory %%m does not exist
        echo [SKIP] Module directory %%m does not exist >> "%LOG_FILE%"
        set /a SKIP_COUNT+=1
    ) else (
        :: Build Debug variant
        echo.
        echo [INFO] Building %%m:assembleDebug...
        echo [INFO] Building %%m:assembleDebug... >> "%LOG_FILE%"

        call gradlew.bat :%%m:assembleDebug --continue >> "%LOG_FILE%" 2>&1
        if !errorlevel! equ 0 (
            echo [SUCCESS] %%m:assembleDebug completed successfully
            echo [SUCCESS] %%m:assembleDebug completed successfully >> "%LOG_FILE%"
            set /a SUCCESS_COUNT+=1
        ) else (
            echo [FAIL] %%m:assembleDebug failed with error code !errorlevel!
            echo [FAIL] %%m:assembleDebug failed with error code !errorlevel! >> "%LOG_FILE%"
            set /a FAIL_COUNT+=1
        )

        :: Build Release variant
        echo.
        echo [INFO] Building %%m:assembleRelease...
        echo [INFO] Building %%m:assembleRelease... >> "%LOG_FILE%"

        call gradlew.bat :%%m:assembleRelease --continue >> "%LOG_FILE%" 2>&1
        if !errorlevel! equ 0 (
            echo [SUCCESS] %%m:assembleRelease completed successfully
            echo [SUCCESS] %%m:assembleRelease completed successfully >> "%LOG_FILE%"
            set /a SUCCESS_COUNT+=1
        ) else (
            echo [FAIL] %%m:assembleRelease failed with error code !errorlevel!
            echo [FAIL] %%m:assembleRelease failed with error code !errorlevel! >> "%LOG_FILE%"
            set /a FAIL_COUNT+=1
        )
    )
)

echo.
echo ========================================
echo Build Summary
echo ========================================
echo Total Successful builds: %SUCCESS_COUNT%
echo Total Failed builds: %FAIL_COUNT%
echo Total Skipped builds: %SKIP_COUNT%
echo.
echo Log file: %LOG_FILE%
echo ========================================

echo. >> "%LOG_FILE%"
echo ======================================== >> "%LOG_FILE%"
echo Build Summary >> "%LOG_FILE%"
echo ======================================== >> "%LOG_FILE%"
echo Total Successful builds: %SUCCESS_COUNT% >> "%LOG_FILE%"
echo Total Failed builds: %FAIL_COUNT% >> "%LOG_FILE%"
echo Total Skipped builds: %SKIP_COUNT% >> "%LOG_FILE%"
echo Build completed at %date% %time% >> "%LOG_FILE%"

endlocal
pause
