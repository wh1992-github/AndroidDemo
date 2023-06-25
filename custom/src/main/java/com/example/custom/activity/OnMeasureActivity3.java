package com.example.custom.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.custom.R;
import com.example.custom.adapter.PlanetListAdapter;
import com.example.custom.bean.Planet;

/**
 * Created by test on 2017/9/17.
 */
public class OnMeasureActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_measure3);

        PlanetListAdapter adapter = new PlanetListAdapter(this, Planet.getDefaultList());
        //从布局文件中获取名叫lv_planet的列表视图
        //lv_planet是系统自带的ListView,被ScrollView嵌套只能显示一行
        ListView lv_planet = findViewById(R.id.lv_planet);
        lv_planet.setAdapter(adapter);
        lv_planet.setOnItemClickListener(adapter);
        lv_planet.setOnItemLongClickListener(adapter);
    }
}

