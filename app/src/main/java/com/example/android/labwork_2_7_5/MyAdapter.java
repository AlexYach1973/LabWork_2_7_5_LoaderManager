package com.example.android.labwork_2_7_5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    List<List<String>> list;
    Context context;

    // Constructor
    public MyAdapter(Context context, List<List<String>> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.contacts_list_item, null);
        } else {
            view = convertView;
        }
        TextView textName = view.findViewById(R.id.item_name);
        TextView textPhone = view.findViewById(R.id.item_telephone);
        List<String> list1;
        list1 = list.get(position);
        textName.setText(list1.get(0));
        textPhone.setText(list1.get(1));

        return view;
    }
}
