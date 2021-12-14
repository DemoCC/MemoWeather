package com.example.memo.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.memo.MyDataBase;
import com.example.memo.R;
import com.example.memo.domain.Memo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewNoteActivity extends AppCompatActivity {

    private EditText ed_title, ed_content;
    private ImageButton saveBtn;
    private MyDataBase myDatabase;
    private Memo memo;
    private Integer memoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        ed_title = findViewById(R.id.ed_title);
        ed_content = findViewById(R.id.ed_content);
        saveBtn = findViewById(R.id.saveBtn);
        myDatabase = new MyDataBase(this);

        Intent intent = getIntent();
        memoId = intent.getIntExtra("id", 0);
        System.out.println("-------id：" + memoId);
        //默认为0，如果不为0则为修改数据时跳转过来的
        if (memoId != 0) {
            memo = myDatabase.getMemoById(memoId);
            ed_title.setText(memo.getTitle());
            ed_content.setText(memo.getContent());
        }
        //保存按钮的点击事件，他和返回按钮是一样的功能，都调用保存的方法；
        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveMemo();
            }
        });
    }

    //保存提醒
    public void saveMemo() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        Date currTime = new Date(System.currentTimeMillis());//获取当前时间
        String time = formatter.format(currTime);
        String title = ed_title.getText().toString();
        String content = ed_content.getText().toString();
        //是要修改数据
        if (memoId != 0) {
            memo = new Memo(memoId, title, content, time);
            myDatabase.updateMemo(memo);
            Intent intent = new Intent(NewNoteActivity.this, NoteActivity.class);
            startActivity(intent);
        } else {  //新建提醒
            memo = new Memo(null, title, content, time);
            myDatabase.insertMemo(memo);
            Intent intent = new Intent(NewNoteActivity.this, NoteActivity.class);
            startActivity(intent);
        }
        NewNoteActivity.this.finish();
    }

    // 返回按钮调用的方法。
    @Override
    public void onBackPressed() {
        saveMemo();
    }

}
