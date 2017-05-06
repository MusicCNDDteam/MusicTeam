package com.example.admin.musicplayer.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.admin.musicplayer.R;
import com.example.admin.musicplayer.controller.ListSongAdapter;
import com.example.admin.musicplayer.controller.SongManager;
import com.example.admin.musicplayer.model.SongModel;
import com.example.admin.musicplayer.utility.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener,
        MediaPlayer.OnCompletionListener{
    public static final int SELECT_SONG_REQUEST = 0;
    private Toolbar toolBar;
    private SongModel playingSong;
    public static ArrayList<SongModel> arrSongs = new ArrayList<>();
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime=5000; // 5000 milliseconds
    private int currentSongIndex = 0;
    // Media Player
    private  MediaPlayer mediaPlayer;
    //Handler to update UI timer, progress bar etc....
    private Handler mHandler = new Handler();
    private ImageView playButton;
    private ImageView forwardButton;
    private ImageView backwardButton;
    private ImageView nextButton;
    private ImageView previousButton;
    private SeekBar songProgressBar;
    private TextView currentDurationTextView;
    private TextView totalDurationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        //get action bar
        toolBar = (Toolbar) findViewById(R.id.my_toolbar);
        toolBar.setTitle("Mp3 Player");
        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.setNavigationIcon(R.drawable.icon_app);
        setSupportActionBar(toolBar);
        //get all song from sd card

        SongManager songManager = new SongManager();
        arrSongs = songManager.getPlayList();

        //Media player is used to play music
        mediaPlayer = new MediaPlayer();

        // map views to player_activity_xml
        playButton=(ImageView) findViewById(R.id.btn_play);
        nextButton=(ImageView) findViewById(R.id.btn_next_song);
        previousButton=(ImageView) findViewById(R.id.btn_back_song);
        forwardButton=(ImageView) findViewById(R.id.btn_forward);
        backwardButton=(ImageView) findViewById(R.id.btn_rewind);
        songProgressBar=(SeekBar) findViewById(R.id.progress_seekbar);
        currentDurationTextView=(TextView) findViewById(R.id.tv_current_time);
        totalDurationTextView=(TextView) findViewById(R.id.tv_total_time);
        // call event listener of each view
        playButton.setOnClickListener(this);
        forwardButton.setOnClickListener(this);
        backwardButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        
        songProgressBar.setOnSeekBarChangeListener(this);// bat su kien de user biet bai hat dang tua den dau
        mediaPlayer.setOnCompletionListener(this);// su kien de biet khi nao mediaplayer choi xong 1 bai nhac
        
        playSong(currentSongIndex);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_SONG_REQUEST && resultCode == RESULT_OK){
            currentSongIndex = data.getExtras().getInt("id");
            playSong(currentSongIndex);
        }
    }


    private void playSong(int currentSongIndex) {

        try{
            mediaPlayer.reset();
            mediaPlayer.setDataSource(arrSongs.get(currentSongIndex).path);
            mediaPlayer.prepare();
            mediaPlayer.start();

            //update title of tooblar
            toolBar.setTitle(arrSongs.get(currentSongIndex).title);

            //change Button Image to pause image
            playButton.setImageResource(R.drawable.pause);

            //set Progress bar values
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);

            //update progress bar
            updateProgressBar();

            //notification
            buildNotification();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        } catch (IllegalStateException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(currentSongIndex < (arrSongs.size()-1)){
            playSong(currentSongIndex+1);
            currentSongIndex = currentSongIndex + 1;
        } else {
            //play first song
            playSong(0);
            currentSongIndex=0;
        }
        buildNotification();
    }

    private void buildNotification() {
        Intent intent = new Intent(getApplicationContext(),PlayerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),1,intent,0);
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.song)
                .setContentTitle("Media Artist")
                .setContentText(arrSongs.get(currentSongIndex).title)
                .setDeleteIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }
    // implement update ProgressBar() method
    private void updateProgressBar() {
        mHandler.postDelayed(mUpDateTimeTask,100);
    }
    /*
     Background Runnable thread
     */
    private Runnable mUpDateTimeTask = new Runnable() {
        @Override
        public void run() {
            try {

                long totalDuration = mediaPlayer.getDuration();
                long currentDuration = mediaPlayer.getCurrentPosition();
                //display total duration time
                totalDurationTextView.setText("" + Util.milliSecondstoTimer(totalDuration));
                //display time finish play
                currentDurationTextView.setText("" + Util.milliSecondstoTimer(currentDuration));
                //update progress bar
                int progress = (int) (Util.getProgressPercentage(currentDuration,totalDuration));
                //Log,d ("Progress","" +progress)
                songProgressBar.setProgress(progress);
                //run this thread after 100 milisecond
                mHandler.postDelayed(this,100);
            }catch (Exception e){

            }
        }
    };


    @Override
    public void onClick(View v) {
      int id = v.getId();
        /* play button click event*/
        if(id == R.id.btn_play){
            if(mediaPlayer.isPlaying()){
                if(mediaPlayer != null){
                    mediaPlayer.pause();
                    //change button imgae to play button
                    playButton.setImageResource(R.drawable.play);
                }
            } else{
                //resume song
                if(mediaPlayer!=null){
                    mediaPlayer.start();
                    //change button imgae to pause button
                    playButton.setImageResource(R.drawable.pause);
                }
            }
        }
        /* forward button click event*/
        if(id == R.id.btn_forward){
            //get current song position
            int currentPosition = mediaPlayer.getCurrentPosition();
            //check if seekBackward time is less than song duration
            if(currentPosition + seekForwardTime <= mediaPlayer.getDuration()){
                //forward song
                mediaPlayer.seekTo(currentPosition + seekForwardTime);
            } else{
                //forward to end position
                mediaPlayer.seekTo(mediaPlayer.getDuration());
            }
        }
        /* backward button click event*/
        if(id == R.id.btn_rewind){
            //get current song position
            int currentPosition = mediaPlayer.getCurrentPosition();
            //check if seekBackward time is greater than 0
            if(currentPosition - seekBackwardTime >=0 ){
                // backward song
                mediaPlayer.seekTo(currentPosition - seekBackwardTime);
            } else{
                //backward to start position
                mediaPlayer.seekTo(0);
            }
        }

        /* next button click event*/
        if( id == R.id.btn_next_song){
            //check if next song is there or not
            if(currentSongIndex < (arrSongs.size()-1)){
                playSong(currentSongIndex + 1);
                currentSongIndex = currentSongIndex+1;
            }else {
                //play first song
                playSong(0);
                currentSongIndex=0;
            }
            buildNotification();
        }

        /* next button click event*/
        if(id == R.id.btn_back_song){
            if(currentSongIndex>0){
                playSong(currentSongIndex - 1);
                currentSongIndex = currentSongIndex-1;
            } else {
                //play last song
                playSong(arrSongs.size()-1);
                currentSongIndex = arrSongs.size()-1;
            }
            buildNotification();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_songs:
                Intent intent = new Intent(this, ListSongsActivity.class);
                startActivityForResult(intent, SELECT_SONG_REQUEST);
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
