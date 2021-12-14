package com.example.memo.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.memo.MyAdapter;
import com.example.memo.MyAdapter2;
import com.example.memo.MyDataBase;
import com.example.memo.R;
import com.example.memo.domain.City;
import com.example.memo.domain.Memo;
import com.example.memo.util.NetUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tv_cityName, tv_temp, tv_weather, tv_pmLevel, tv_wind, tv_date;
    ListView lv_main;
    MyDataBase mdb;
    LayoutInflater inflater;
    ArrayList<Memo> memoList;
    LinearLayout bottomScrollView;
    ArrayList<City> weathers;

    //Looper.getMainLooper()
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                City city = (City) msg.obj;
                tv_cityName.setText(city.getCityName());
                tv_temp.setText(city.getCurrTemp().concat("°"));
                tv_weather.setText(city.getWeather());
                tv_pmLevel.setText(city.getPmLevel());
                tv_wind.setText(city.getWind().concat(city.getWindSpeed()));
                tv_date.setText(city.getDate().concat(city.getWeek()));

                addBottomWeather();

            }
        }
    };

    private void addBottomWeather() {
        bottomScrollView.removeAllViews();
        for (int i = 0; i < weathers.size(); i++) {
            City city = weathers.get(i);
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.weather_item, null);

            //绑定组件
            TextView tv_week = view.findViewById(R.id.tv_week);
            TextView tv_weather = view.findViewById(R.id.tv_weather);
            TextView tv_temp = view.findViewById(R.id.tv_temp);

            //设置组件文本
            tv_week.setText(city.getWeek());
            tv_weather.setText(city.getWeather());
            tv_temp.setText(city.getTempRange());

            //将底部显示周一到周五的组件添加视图
            bottomScrollView.addView(view);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_cityName = findViewById(R.id.tv_cityName);
        tv_temp = findViewById(R.id.tv_temp);
        tv_weather = findViewById(R.id.tv_weather);
        tv_pmLevel = findViewById(R.id.tv_pmLevel);
        tv_wind = findViewById(R.id.tv_wind);
        tv_date = findViewById(R.id.tv_date);
        lv_main = findViewById(R.id.lv_main);
        bottomScrollView = findViewById(R.id.bottomScrollView);

        mdb = new MyDataBase(this);
        inflater = getLayoutInflater();
        memoList = mdb.getMemoWithLimit();
        MyAdapter2 myAdapter2 = new MyAdapter2(inflater, memoList);
        lv_main.setAdapter(myAdapter2);

       //点击listView的item,修改提醒
        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(), NewNoteActivity.class);
                intent.putExtra("id", memoList.get(position).getId());
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        /*
         * 长点后来判断是否删除数据
         */
        lv_main.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("删除")
                        .setMessage("是否删除笔记")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mdb.deleteMemo(memoList.get(position).getId());
                                memoList = mdb.getMemoList();
                                MyAdapter2 adapter = new MyAdapter2(inflater, memoList);
                                lv_main.setAdapter(adapter);
                            }
                        })
                        .create().show();
                return true;
            }
        });
    }

    //解析天气数据
    public void getWeatherData(String weatherUrl) {
        City city = new City();
        String result = NetUtils.getNetData(weatherUrl);
        Log.i("天气数据------->", result);
        String data = result.substring(result.indexOf("(") + 1, result.indexOf(")"));

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String currTemp = weatherInfo.getString("temp");
            String tempRange = weatherInfo.getString("temp1");
            String weather = weatherInfo.getString("weather1");
            String pmLevel = weatherInfo.getString("pm-level");
            String wind = weatherInfo.getString("wd");
            String windSpeed = weatherInfo.getString("wind1");
            String date = weatherInfo.getString("date_y");
            String week = weatherInfo.getString("week");

            city.setCityName(cityName);
            city.setCurrTemp(currTemp);
            city.setTempRange(tempRange);
            city.setWeather(weather);
            city.setPmLevel(pmLevel);
            city.setWind(wind);
            city.setWindSpeed(windSpeed);
            city.setDate(date);
            city.setWeek(week);
            weathers = new ArrayList<>();
            for (int i = 1; i <= 6; i++) {
                City city1 = new City();
                city1.setWeather(weatherInfo.getString("weather" + i));
                city1.setTempRange(weatherInfo.getString("temp" + i));
                city1.setWeek(initWeek(i, weatherInfo.getString("week")));
                weathers.add(city1);
            }

            Message message = new Message();
            message.what = 1;
            message.obj = city;
            handler.sendMessage(message);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //搜索切换城市
    public void changeCity(View view) {
        Intent intent = new Intent(this, CityActivity.class);
        startActivityForResult(intent, 1);
    }

    //重写方法获取返回的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 2) {
            City city = (City) data.getSerializableExtra("city");
            final String weatherUrl = city.getUrl();
            System.out.println(weatherUrl);

            //获取天气信息线程
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    //解析天气数据
                    getWeatherData(weatherUrl);
                }
            });
            thread.start();
        }
    }

    //菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //菜单点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(getApplicationContext(), NewNoteActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.item2:
                this.finish();
            case R.id.item3:
                Intent intent2 = new Intent(MainActivity.this, NoteActivity.class);
                startActivity(intent2);
                this.finish();
                break;
            default:
                break;
        }
        return true;
    }

    // i是下标，weekTemp是当天是星期几
    //i是从2-6，weekTemp是当天是星期三
    private String initWeek(int i, String weekTemp) {
        List<String> listString = new ArrayList<String>();
        String[] weeks = new String[]{"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
        //算出weekTemp在集合中的位置
        int weekTempInt = 0;
        for (int j = 0; j < weeks.length; j++) {
            if (weeks[j].equals(weekTemp)) {
                weekTempInt = j;
                break;
            }
        }
        //weekTempInt是数字，就可以在他们的基础上向后加五个数字，得到五个下标
        //当前是星期三，它的下标是2，
        //如果传递过来的是2，weekTempInt是2, 2+2-1,能得到“星期四”
        //如果传递过来的是3， weekTempInt是2, 2+3-1,能得到“星期五”
        //有可能相加之后算出来的数据大于星期日
        int index = weekTempInt + i - 1;
        if (index >= 7) {
            //超出下标
            index = index % 7;
        }
        return weeks[index];
    }

    // 主页的添加提醒按钮
    public void addNote(View view) {
        Intent intent = new Intent(getApplicationContext(), NewNoteActivity.class);
        startActivity(intent);
        this.finish();
    }

}