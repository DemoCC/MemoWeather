package com.example.memo.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.memo.MyAdapter;
import com.example.memo.MyDataBase;
import com.example.memo.R;
import com.example.memo.domain.Memo;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {

    ListView listView;
    LayoutInflater inflater;
    ArrayList<Memo> memoList;
    MyDataBase mdb;
    ImageButton imgBtn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list);
        listView = findViewById(R.id.lv_note);
        inflater = getLayoutInflater();
        imgBtn_add = findViewById(R.id.imgBtn_add);
        mdb = new MyDataBase(this);
        memoList = mdb.getMemoList();
        MyAdapter adapter = new MyAdapter(inflater, memoList);
        listView.setAdapter(adapter);
        /*
         * 点击listView里面的item,修改待办
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(), NewNoteActivity.class);
                intent.putExtra("id", memoList.get(position).getId());
                startActivity(intent);
                NoteActivity.this.finish();
            }
        });

        /*
         * 长点后来判断是否删除数据
         */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(NoteActivity.this)
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
                                MyAdapter adapter = new MyAdapter(inflater, memoList);
                                listView.setAdapter(adapter);
                            }
                        })
                        .create().show();
                return true;
            }
        });

        //添加按钮
        imgBtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    //返回键
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}