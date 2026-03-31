#!/bin/bash

# 批量替换所有 Java 文件中的 import 语句

echo "Migrating import statements from Android Support Library to AndroidX..."

# 查找所有 .java 文件
find . -name "*.java" -type f | while read file; do
    if [ -f "$file" ]; then
        # 备份原文件（可选）
        # cp "$file" "${file}.bak"

        # 批量替换 import 语句
        # AppCompat
        sed -i 's/import android\.support\.v7\.app\./import androidx.appcompat.app./g' "$file"

        # RecyclerView
        sed -i 's/import android\.support\.v7\.widget\.RecyclerView/import androidx.recyclerview.widget.RecyclerView/g' "$file"
        sed -i 's/import android\.support\.v7\.widget\.LinearLayoutManager/import androidx.recyclerview.widget.LinearLayoutManager/g' "$file"
        sed -i 's/import android\.support\.v7\.widget\.GridLayoutManager/import androidx.recyclerview.widget.GridLayoutManager/g' "$file"
        sed -i 's/import android\.support\.v7\.widget\.StaggeredGridLayoutManager/import androidx.recyclerview.widget.StaggeredGridLayoutManager/g' "$file"
        sed -i 's/import android\.support\.v7\.widget\.DefaultItemAnimator/import androidx.recyclerview.widget.DefaultItemAnimator/g' "$file"
        sed -i 's/import android\.support\.v7\.widget\.DividerItemDecoration/import androidx.recyclerview.widget.DividerItemDecoration/g' "$file"

        # CardView
        sed -i 's/import android\.support\.v7\.widget\.CardView/import androidx.cardview.widget.CardView/g' "$file"

        # Design / Material
        sed -i 's/import android\.support\.design\./import com.google.android.material./g' "$file"

        # ConstraintLayout
        sed -i 's/import android\.support\.constraint\./import androidx.constraintlayout.widget./g' "$file"

        # Support v4
        sed -i 's/import android\.support\.v4\.app\.Fragment/import androidx.fragment.app.Fragment/g' "$file"
        sed -i 's/import android\.support\.v4\.app\.FragmentManager/import androidx.fragment.app.FragmentManager/g' "$file"
        sed -i 's/import android\.support\.v4\.app\.FragmentTransaction/import androidx.fragment.app.FragmentTransaction/g' "$file"
        sed -i 's/import android\.support\.v4\.app\.FragmentActivity/import androidx.fragment.app.FragmentActivity/g' "$file"
        sed -i 's/import android\.support\.v4\.app\.FragmentPagerAdapter/import androidx.fragment.app.FragmentPagerAdapter/g' "$file"
        sed -i 's/import android\.support\.v4\.app\.FragmentStatePagerAdapter/import androidx.fragment.app.FragmentStatePagerAdapter/g' "$file"
        sed -i 's/import android\.support\.v4\.view\.ViewPager/import androidx.viewpager.widget.ViewPager/g' "$file"
        sed -i 's/import android\.support\.v4\.view\.PagerAdapter/import androidx.viewpager.widget.PagerAdapter/g' "$file"
        sed -i 's/import android\.support\.v4\.widget\.DrawerLayout/import androidx.drawerlayout.widget.DrawerLayout/g' "$file"
        sed -i 's/import android\.support\.v4\.widget\.SwipeRefreshLayout/import androidx.swiperefreshlayout.widget.SwipeRefreshLayout/g' "$file"
        sed -i 's/import android\.support\.v4\.content\.ContextCompat/import androidx.core.content.ContextCompat/g' "$file"
        sed -i 's/import android\.support\.v4\.content\.FileProvider/import androidx.core.content.FileProvider/g' "$file"
        sed -i 's/import android\.support\.v4\.app\.ActivityCompat/import androidx.core.app.ActivityCompat/g' "$file"
        sed -i 's/import android\.support\.v4\.app\.NotificationCompat/import androidx.core.app.NotificationCompat/g' "$file"
        sed -i 's/import android\.support\.v4\.content\.LocalBroadcastManager/import androidx.localbroadcastmanager.content.LocalBroadcastManager/g' "$file"
        sed -i 's/import android\.support\.v4\.graphics\.drawable\.DrawableCompat/import androidx.core.graphics.drawable.DrawableCompat/g' "$file"

        # Annotations
        sed -i 's/import android\.support\.annotation\./import androidx.annotation./g' "$file"

        # Test
        sed -i 's/import android\.support\.test\./import androidx.test./g' "$file"
    fi
done

echo "Import migration completed!"
