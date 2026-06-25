#!/bin/bash
# 全量构建脚本：清理后按依赖顺序构建所有模块的 Debug 包。
# 说明：
#   - clean 与 assemble 分两次调用，避免清理与构建在同一任务图中冲突。
#   - 使用 --continue 保证单个模块失败不会中断其余模块。
#   - 复用 Gradle daemon（不加 --no-daemon），避免频繁启停导致中间产物文件锁冲突。
#   - 如需重新拉取依赖，去掉下面 --refresh-dependencies 前的注释。

echo "========================================"
echo "[1/2] Cleaning project..."
echo "========================================"
./gradlew clean --no-daemon || exit $?

echo ""
echo "========================================"
echo "[2/2] Assembling all modules (debug)..."
echo "========================================"
./gradlew assembleDebug --continue --no-daemon
RESULT=$?

echo ""
if [ $RESULT -eq 0 ]; then
    echo "BUILD SUCCESS"
else
    echo "BUILD FINISHED WITH ERRORS (exit=$RESULT). Check task output above for failed modules."
fi
exit $RESULT
