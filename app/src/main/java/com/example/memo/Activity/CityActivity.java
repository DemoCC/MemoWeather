package com.example.memo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.memo.R;
import com.example.memo.domain.City;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CityActivity extends AppCompatActivity {
    ListView cityViewList;
    List<City> cityList;
    //城市数据
    City[] cityArr = {
            new City("合肥", "http://weather.123.duba.net/static/weather_info/101220101.html")
            , new City("芜湖", "http://weather.123.duba.net/static/weather_info/101220301.html")
            , new City("蚌埠", "http://weather.123.duba.net/static/weather_info/101220201.html")
            , new City("安庆", "http://weather.123.duba.net/static/weather_info/101220601.html")
            , new City("马鞍山", "http://weather.123.duba.net/static/weather_info/101220501.html")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_list);
        cityViewList = findViewById(R.id.cityViewList);

        //初始化
        initCity();
        //设置适配器
        setAdapter();

    }

    //初始化数据
    private void initCity() {
        cityList = new ArrayList<>();
        cityList.addAll(Arrays.asList(cityArr));
    }

    //设置适配器
    private void setAdapter() {
        //获取城市名称
        String[] cityNames = new String[cityList.size()];
        for (int i = 0; i < cityList.size(); i++) {
            cityNames[i] = cityList.get(i).getCityName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CityActivity.this, android.R.layout.simple_list_item_1, cityNames);

        //每一项的点击事件
        cityViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("city", cityList.get(position)); //此传输方法需要City类实现Serializable接口
                setResult(2, intent);
                finish();
            }
        });

        cityViewList.setAdapter(adapter);
    }


}