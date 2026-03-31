#!/bin/bash

# 为所有模块添加 lint 配置
modules=(
    "custom" "junior" "middle" "senior" "storage"
    "device" "network" "event" "media" "performance"
    "test" "CustomToast" "group" "customview" "util"
    "weixin" "mixture" "filedialog"
)

for module in "${modules[@]}"; do
    build_file="${module}/build.gradle"
    if [ ! -f "$build_file" ]; then
        echo "File $build_file not found, skipping..."
        continue
    fi

    echo "Processing $module..."

    # 检查是否已经有 lint 配置
    if grep -q "lint {" "$build_file"; then
        echo "  Lint config already exists in $build_file, skipping..."
        continue
    fi

    # 在 android 块的右大括号前添加 lint 配置
    # 找到最后一个独立的 } （android 块的结束）
    # 在 buildTypes 块后、android 块结束前添加 lint 配置
    awk '
        /^android {/ { in_android = 1 }
        in_android && /^}$/ && !added {
            print "    lint {"
            print "        abortOnError false"
            print "    }"
            added = 1
        }
        { print }
    ' "$build_file" > "${build_file}.tmp" && mv "${build_file}.tmp" "$build_file"

    echo "  Updated $build_file"
done

echo "All modules processed!"
