package com.example.custom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.custom.adapter.PlanetListAdapter;
import com.example.custom.bean.Planet;
import com.example.custom.widget.NoScrollListView;

/**
 * Created by test on 2017/9/17.
 */
public class OnMeasureActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_measure2);

        PlanetListAdapter adapter = new PlanetListAdapter(this, Planet.getDefaultList());
        //从布局文件中获取名叫nslv_planet的不滚动列表视图
        //nslv_planet是自定义控件NoScrollListView,会显示所有行
        NoScrollListView nslv_planet = findViewById(R.id.nslv_planet);
        nslv_planet.setAdapter(adapter);
        nslv_planet.setOnItemClickListener(adapter);
        nslv_planet.setOnItemLongClickListener(adapter);
    }
}

