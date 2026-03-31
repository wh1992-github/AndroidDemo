#!/bin/bash
# 批量更新所有 build.gradle 文件的依赖到 AndroidX

# 定义需要更新的模块
modules=(
    "custom"
    "junior"
    "middle"
    "senior"
    "storage"
    "device"
    "network"
    "event"
    "media"
    "performance"
    "test"
    "CustomToast"
    "group"
    "customview"
    "util"
    "weixin"
    "mixture"
    "filedialog"
)

for module in "${modules[@]}"; do
    build_file="${module}/build.gradle"
    if [ -f "$build_file" ]; then
        echo "Updating $build_file..."

        # 备份原文件
        cp "$build_file" "${build_file}.bak"

        # 使用 sed 批量替换依赖
        sed -i "s|com.android.support:appcompat-v7:28.0.0|androidx.appcompat:appcompat:1.6.1|g" "$build_file"
        sed -i "s|com.android.support:recyclerview-v7:28.0.0|androidx.recyclerview:recyclerview:1.3.2|g" "$build_file"
        sed -i "s|com.android.support:design:28.0.0|com.google.android.material:material:1.11.0|g" "$build_file"
        sed -i "s|com.android.support.constraint:constraint-layout:1.1.2|androidx.constraintlayout:constraintlayout:2.1.4|g" "$build_file"
        sed -i "s|com.android.support.constraint:constraint-layout:1.1.3|androidx.constraintlayout:constraintlayout:2.1.4|g" "$build_file"
        sed -i "s|com.android.support.constraint:constraint-layout:2.0.0|androidx.constraintlayout:constraintlayout:2.1.4|g" "$build_file"
        sed -i "s|com.android.support.constraint:constraint-layout:2.0.1|androidx.constraintlayout:constraintlayout:2.1.4|g" "$build_file"
        sed -i "s|com.android.support:cardview-v7:28.0.0|androidx.cardview:cardview:1.0.0|g" "$build_file"
        sed -i "s|com.android.support:palette-v7:28.0.0|androidx.palette:palette:1.0.0|g" "$build_file"
        sed -i "s|com.android.support:support-annotations:28.0.0|androidx.annotation:annotation:1.7.1|g" "$build_file"
        sed -i "s|com.android.support.test:runner:1.0.2|androidx.test:runner:1.5.2|g" "$build_file"
        sed -i "s|com.android.support.test.espresso:espresso-core:3.0.2|androidx.test.espresso:espresso-core:3.5.1|g" "$build_file"
        sed -i "s|junit:junit:4.12|junit:junit:4.13.2|g" "$build_file"

        echo "Updated $build_file"
    else
        echo "File $build_file not found, skipping..."
    fi
done

echo "All modules updated!"
