package com.example.memo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.memo.domain.Memo;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<Memo> memoList;

    public MyAdapter(LayoutInflater inflater, ArrayList<Memo> memoList) {
        this.inflater = inflater;
        this.memoList = memoList;
    }

    @Override
    public int getCount() {
        return memoList.size();
    }

    @Override
    public Object getItem(int position) {
        return memoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.listview_item, null);//注意导包，别导系统包
            vh.tv1 = convertView.findViewById(R.id.textView1);
            vh.tv2 = convertView.findViewById(R.id.textView2);
            convertView.setTag(vh);
        }
        vh = (ViewHolder) convertView.getTag();
        vh.tv1.setText(memoList.get(position).getTitle());
        vh.tv2.setText(memoList.get(position).getTime());
        return convertView;
    }

    class ViewHolder {
        TextView tv1, tv2;
    }
}
