#!/bin/bash

# 为每个模块添加 namespace 配置
# 格式: module_name:package_name

declare -A modules=(
    ["custom"]="com.example.custom"
    ["junior"]="com.example.junior"
    ["middle"]="com.example.middle"
    ["senior"]="com.example.senior"
    ["storage"]="com.example.storage"
    ["device"]="com.example.device"
    ["network"]="com.example.network"
    ["event"]="com.example.event"
    ["media"]="com.example.media"
    ["performance"]="com.example.performance"
    ["test"]="com.example.test"
    ["CustomToast"]="com.example.customtoast"
    ["group"]="com.example.group"
    ["customview"]="com.example.customview"
    ["util"]="com.example.util"
    ["weixin"]="com.example.weixin"
    ["mixture"]="com.example.mixture"
    ["filedialog"]="com.afollestad.materialdialogs.sample"
)

for module in "${!modules[@]}"; do
    build_file="${module}/build.gradle"
    if [ ! -f "$build_file" ]; then
        echo "File $build_file not found, skipping..."
        continue
    fi

    package="${modules[$module]}"
    echo "Processing $module with package $package..."

    # 检查是否已经有 namespace
    if grep -q "namespace" "$build_file"; then
        echo "  Namespace already exists in $build_file, skipping..."
        continue
    fi

    # 在 android { 后添加 namespace
    sed -i "/^android {/a\\    namespace '$package'" "$build_file"

    # 添加 lint 配置（如果还没有）
    if ! grep -q "lint {" "$build_file"; then
        # 找到最后一个 } 之前，添加 lint 配置
        # 在 compileOptions 后添加
        if grep -q "compileOptions {" "$build_file"; then
            sed -i "/compileOptions {/,/}/a\\    lint {\n        abortOnError false\n    }" "$build_file"
        fi
    fi

    echo "  Updated $build_file"
done

echo "All modules processed!"
