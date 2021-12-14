package com.example.memo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.memo.domain.Memo;

import java.util.ArrayList;

public class MyDataBase {

    Context context;
    MyOpenHelper myHelper;
    SQLiteDatabase myDatabase;

    //别的类实例化这个类的同时，创建数据库
    public MyDataBase(Context context) {
        this.context = context;
        myHelper = new MyOpenHelper(context);
    }

    // 得到ListView的数据，从数据库里查找后解析
    public ArrayList<Memo> getMemoList() {
        ArrayList<Memo> memoList = new ArrayList<>();
        ArrayList<Memo> memoList1 = new ArrayList<>();
        myDatabase = myHelper.getWritableDatabase();

        Cursor cursor = myDatabase.rawQuery("select id,title,time from mynote", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Integer id = cursor.getInt(cursor.getColumnIndex("id"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            Memo memo = new Memo();
            memo.setId(id);
            memo.setTitle(title);
            memo.setTime(time);
            memoList.add(memo);
            cursor.moveToNext();
        }
        myDatabase.close();
        for (int i = memoList.size(); i > 0; i--) {
            memoList1.add(memoList.get(i - 1));
        }
        return memoList1;
    }

    // 得到ListView的数据，从数据库里查找后解析
    public ArrayList<Memo> getMemoWithLimit() {
        ArrayList<Memo> memoList = new ArrayList<>();
        ArrayList<Memo> memoList1 = new ArrayList<>();
        myDatabase = myHelper.getWritableDatabase();

        Cursor cursor = myDatabase.rawQuery("select id,title,time from mynote limit 2", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            Memo memo = new Memo();
            memo.setId(id);
            memo.setTitle(title);
            memo.setTime(time);
            memoList.add(memo);
            cursor.moveToNext();
        }
        myDatabase.close();
        for (int i = memoList.size(); i > 0; i--) {
            memoList1.add(memoList.get(i - 1));
        }
        return memoList1;
    }

    // 回显
    public Memo getMemoById(int id) {
        myDatabase = myHelper.getWritableDatabase();
        Cursor cursor = myDatabase.rawQuery("select title, content from mynote where id='" + id + "'", null);
        cursor.moveToFirst();
        String title = cursor.getString(cursor.getColumnIndex("title"));
        String content = cursor.getString(cursor.getColumnIndex("content"));
        Memo memo = new Memo(title, content);
        myDatabase.close();
        return memo;
    }

    //修改提醒
    public void updateMemo(Memo memo) {
        myDatabase = myHelper.getWritableDatabase();
        myDatabase.execSQL(
                "update mynote set title='" + memo.getTitle() +
                        "',time='" + memo.getTime() +
                        "',content='" + memo.getContent() +
                        "' where id='" + memo.getId() + "'");
        myDatabase.close();
    }

    //添加提醒
    public void insertMemo(Memo memo) {
        myDatabase = myHelper.getWritableDatabase();
        myDatabase.execSQL("insert into mynote(title,content,time)values('"
                + memo.getTitle() + "','"
                + memo.getContent() + "','"
                + memo.getTime()
                + "')");
        myDatabase.close();
    }

    //长按点击后选择删除提醒
    public void deleteMemo(int id) {
        myDatabase = myHelper.getWritableDatabase();
        myDatabase.execSQL("delete from mynote where id=" + id + "");
        myDatabase.close();
    }
}
