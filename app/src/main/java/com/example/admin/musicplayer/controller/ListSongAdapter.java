package com.example.admin.musicplayer.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.musicplayer.R;
import com.example.admin.musicplayer.model.SongModel;

import java.util.ArrayList;

/**
 * Created by ADMIN on 06/04/2017.
 */

public class ListSongAdapter extends BaseAdapter {
    private ArrayList<SongModel> arrayList= new ArrayList<>();
    private Context context;

    public ListSongAdapter(Context context,ArrayList<SongModel> arrayList){
        this.arrayList=arrayList;
        this.context=context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater= LayoutInflater.from(context);
        View rowView = layoutInflater.inflate(R.layout.song_item, parent,false);
        // get name of each song in list song
        TextView nameTV = (TextView) rowView.findViewById(R.id.song_name_tv);
        nameTV.setText(arrayList.get(position).title);
        return rowView;
    }
}
