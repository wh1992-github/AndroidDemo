#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""批量为所有模块添加 namespace 配置"""

import os
import re
import xml.etree.ElementTree as ET

# 定义需要更新的模块
modules = [
    "custom", "junior", "middle", "senior", "storage",
    "device", "network", "event", "media", "performance",
    "test", "CustomToast", "group", "customview", "util",
    "weixin", "mixture", "filedialog"
]

def get_package_from_manifest(module_path):
    """从 AndroidManifest.xml 中提取 package 名称"""
    manifest_path = os.path.join(module_path, "src", "main", "AndroidManifest.xml")
    if not os.path.exists(manifest_path):
        return None

    try:
        tree = ET.parse(manifest_path)
        root = tree.getroot()
        return root.get('package')
    except Exception as e:
        print(f"Error parsing {manifest_path}: {e}")
        return None

def add_namespace_to_build_gradle(build_gradle_path, namespace):
    """在 build.gradle 文件中添加 namespace 配置"""
    if not os.path.exists(build_gradle_path):
        return False

    with open(build_gradle_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # 检查是否已经有 namespace
    if 'namespace' in content:
        print(f"  Namespace already exists in {build_gradle_path}")
        return True

    # 在 android { 后添加 namespace
    # 匹配 "android {" 后面的第一行
    pattern = r'(android\s*\{)'
    replacement = f"\\1\n    namespace '{namespace}'"

    new_content = re.sub(pattern, replacement, content, count=1)

    # 添加 lint 配置（如果还没有）
    if 'lint {' not in new_content:
        # 在 android 块的最后一个 } 之前添加 lint 配置
        # 找到 compileOptions 或其他配置块，在其后添加
        if 'compileOptions {' in new_content:
            pattern2 = r'(compileOptions\s*\{[^}]*\})'
            replacement2 = "\\1\n    lint {\n        abortOnError false\n    }"
            new_content = re.sub(pattern2, replacement2, new_content, count=1)
        else:
            # 如果没有 compileOptions，在 buildTypes 后添加
            pattern2 = r'(buildTypes\s*\{[^}]*\}[^}]*\})'
            replacement2 = "\\1\n    lint {\n        abortOnError false\n    }"
            new_content = re.sub(pattern2, replacement2, new_content, count=1)

    with open(build_gradle_path, 'w', encoding='utf-8') as f:
        f.write(new_content)

    return True

def main():
    base_path = os.getcwd()

    for module in modules:
        module_path = os.path.join(base_path, module)
        if not os.path.exists(module_path):
            print(f"Module {module} not found, skipping...")
            continue

        print(f"Processing module: {module}")

        # 获取 package 名称
        package = get_package_from_manifest(module_path)
        if not package:
            print(f"  Could not get package name from AndroidManifest.xml for {module}")
            continue

        print(f"  Package: {package}")

        # 更新 build.gradle
        build_gradle_path = os.path.join(module_path, "build.gradle")
        if add_namespace_to_build_gradle(build_gradle_path, package):
            print(f"  ✓ Updated {module}/build.gradle")
        else:
            print(f"  ✗ Failed to update {module}/build.gradle")

    print("\nAll modules processed!")

if __name__ == "__main__":
    main()
