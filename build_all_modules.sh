#!/bin/bash

echo "========================================"
echo "Starting build for all modules"
echo "========================================"
echo ""

# List of all modules from settings.gradle
modules=(
    "CustomToast"
    "util"
    "app"
    "junior"
    "middle"
    "storage"
    "senior"
    "custom"
    "group"
    "test"
    "device"
    "network"
    "event"
    "animation"
    "media"
    "weixin"
    "performance"
    "filedialog"
    "mixture"
    "customview"
    "hezi"
)

# Create log file
LOG_FILE="build_all_modules_$(date +%Y%m%d_%H%M%S).log"
echo "Build started at $(date)" > "$LOG_FILE"
echo "" >> "$LOG_FILE"

# Initialize counters
SUCCESS_COUNT=0
FAIL_COUNT=0
SKIP_COUNT=0

# Build each module
for module in "${modules[@]}"; do
    echo ""
    echo "========================================"
    echo "Building module: $module"
    echo "========================================"

    echo "" >> "$LOG_FILE"
    echo "========================================" >> "$LOG_FILE"
    echo "Building module: $module" >> "$LOG_FILE"
    echo "========================================" >> "$LOG_FILE"

    # Check if module directory exists
    if [ ! -d "$module" ]; then
        echo "[SKIP] Module directory $module does not exist"
        echo "[SKIP] Module directory $module does not exist" >> "$LOG_FILE"
        ((SKIP_COUNT++))
        continue
    fi

    # Build Debug variant
    echo ""
    echo "[INFO] Building $module:assembleDebug..."
    echo "[INFO] Building $module:assembleDebug..." >> "$LOG_FILE"

    if ./gradlew.bat ":$module:assembleDebug" --continue >> "$LOG_FILE" 2>&1; then
        echo "[SUCCESS] $module:assembleDebug completed successfully"
        echo "[SUCCESS] $module:assembleDebug completed successfully" >> "$LOG_FILE"
        ((SUCCESS_COUNT++))
    else
        echo "[FAIL] $module:assembleDebug failed with error code $?"
        echo "[FAIL] $module:assembleDebug failed with error code $?" >> "$LOG_FILE"
        ((FAIL_COUNT++))
    fi

    # Build Release variant
    echo ""
    echo "[INFO] Building $module:assembleRelease..."
    echo "[INFO] Building $module:assembleRelease..." >> "$LOG_FILE"

    if ./gradlew.bat ":$module:assembleRelease" --continue >> "$LOG_FILE" 2>&1; then
        echo "[SUCCESS] $module:assembleRelease completed successfully"
        echo "[SUCCESS] $module:assembleRelease completed successfully" >> "$LOG_FILE"
        ((SUCCESS_COUNT++))
    else
        echo "[FAIL] $module:assembleRelease failed with error code $?"
        echo "[FAIL] $module:assembleRelease failed with error code $?" >> "$LOG_FILE"
        ((FAIL_COUNT++))
    fi
done

echo ""
echo "========================================"
echo "Build Summary"
echo "========================================"
echo "Total Successful builds: $SUCCESS_COUNT"
echo "Total Failed builds: $FAIL_COUNT"
echo "Total Skipped builds: $SKIP_COUNT"
echo ""
echo "Log file: $LOG_FILE"
echo "========================================"

echo "" >> "$LOG_FILE"
echo "========================================" >> "$LOG_FILE"
echo "Build Summary" >> "$LOG_FILE"
echo "========================================" >> "$LOG_FILE"
echo "Total Successful builds: $SUCCESS_COUNT" >> "$LOG_FILE"
echo "Total Failed builds: $FAIL_COUNT" >> "$LOG_FILE"
echo "Total Skipped builds: $SKIP_COUNT" >> "$LOG_FILE"
echo "Build completed at $(date)" >> "$LOG_FILE"
