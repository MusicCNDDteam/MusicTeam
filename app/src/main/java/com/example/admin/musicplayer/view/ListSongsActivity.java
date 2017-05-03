package com.example.admin.musicplayer.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.admin.musicplayer.R;
import com.example.admin.musicplayer.controller.ListSongAdapter;
import com.example.admin.musicplayer.controller.SongManager;
import com.example.admin.musicplayer.model.SongModel;
/*Create by Team TPQ
*/
public class ListSongsActivity extends AppCompatActivity  implements AdapterView.OnItemClickListener{

    private Toolbar toolBar;
    private ListView listSongLV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_songs);

        // find views
        listSongLV=(ListView) findViewById(R.id.list_song_lv);
        //get action bar
        toolBar = (Toolbar) findViewById(R.id.my_toolbar);
        toolBar.setTitle("Title songs");
        toolBar.setTitleTextColor(Color.BLACK);
        toolBar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolBar);

        //create adapter
        ListSongAdapter adapter = new ListSongAdapter(this, PlayerActivity.arrSongs);
        listSongLV.setAdapter(adapter);
        listSongLV.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SongModel songModel= PlayerActivity.arrSongs.get(position);

        // create intent to paste song to PlayerActivity
        Intent intent = new Intent();
        intent.putExtra("id",position);

        //set result
        setResult(RESULT_OK,intent);
        finish();
    }
}
