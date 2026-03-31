#!/bin/bash

echo "=== 修复剩余模块的 AndroidX 迁移问题 ==="

# 定义需要修复的模块
MODULES="customview group performance senior util weixin mixture"

echo "步骤 1: 修复 Java 文件中的 import 语句..."
for module in $MODULES; do
    if [ -d "$module/src" ]; then
        echo "处理模块: $module"
        find "$module/src" -name "*.java" -type f -exec sed -i \
            -e 's/import android\.support\.v4\.hardware\.fingerprint\.FingerprintManagerCompat/import androidx.core.hardware.fingerprint.FingerprintManagerCompat/g' \
            -e 's/import android\.support\.v4\.os\.CancellationSignal/import androidx.core.os.CancellationSignal/g' \
            -e 's/import android\.support\.v7\.widget\.Toolbar/import androidx.appcompat.widget.Toolbar/g' \
            -e 's/import android\.support\.v4\.widget\.DrawerLayout/import androidx.drawerlayout.widget.DrawerLayout/g' \
            -e 's/import android\.arch\.lifecycle\./import androidx.lifecycle./g' \
            -e 's/import android\.support\.v7\.widget\./import androidx.appcompat.widget./g' \
            -e 's/import android\.support\.v4\.app\.ActivityCompat/import androidx.core.app.ActivityCompat/g' \
            -e 's/import android\.support\.v4\.content\.ContextCompat/import androidx.core.content.ContextCompat/g' \
            {} \;
    fi
done

echo "步骤 2: 修复 XML 文件中的 support 库引用..."
for module in $MODULES; do
    if [ -d "$module/src" ]; then
        echo "处理模块 XML: $module"
        find "$module/src" -name "*.xml" -type f -exec sed -i \
            -e 's|<android\.support\.v7\.widget\.Toolbar|<androidx.appcompat.widget.Toolbar|g' \
            -e 's|</android\.support\.v7\.widget\.Toolbar>|</androidx.appcompat.widget.Toolbar>|g' \
            -e 's|android\.support\.v7\.widget\.SearchView|androidx.appcompat.widget.SearchView|g' \
            -e 's|android\.support\.design\.widget\.|com.google.android.material.|g' \
            -e 's|android\.support\.v4\.widget\.NestedScrollView|androidx.core.widget.NestedScrollView|g' \
            -e 's|android\.support\.v4\.view\.ViewPager|androidx.viewpager.widget.ViewPager|g' \
            -e 's|android\.support\.v7\.widget\.RecyclerView|androidx.recyclerview.widget.RecyclerView|g' \
            -e 's|android\.support\.constraint\.|androidx.constraintlayout.widget.|g' \
            {} \;
    fi
done

echo "步骤 3: 清理 build 目录..."
./gradlew clean

echo "步骤 4: 尝试编译所有模块..."
./gradlew assembleDebug --continue

echo "=== 修复完成！==="
echo "请查看编译结果，对于仍然失败的模块，可能需要手动检查代码。"
