package com.example.admin.musicplayer.view;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.admin.musicplayer.R;
import com.example.admin.musicplayer.controller.SongManager;
import com.example.admin.musicplayer.model.SongModel;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener,
        MediaPlayer.OnCompletionListener{
    public static final int SELECT_SONG_REQUEST = 0;
    private Toolbar toolBar;
    private SongModel playingSong;
    public static ArrayList<SongModel> arrSongs = new ArrayList<>();
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime=5000; // 5000 milliseconds
    private int songIndex = 0;
    // Media Player
    private  MediaPlayer mp;
    //Handler to update UI timer, progress bar etc....
    private Handler mHandler = new Handler();
    private ImageView btnPlay;
    private ImageView btnForward;
    private ImageView btnBackward;
    private ImageView btnNext;
    private ImageView btnPrevious;
    private SeekBar songProgressBar;
    private TextView lblCurrentDuration;
    private TextView lblTotalDuration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        //get action bar
        toolBar = (Toolbar) findViewById(R.id.title_tb);
        toolBar.setTitle("Mp3 Player");
        toolBar.setTitleTextColor(Color.BLACK);
        toolBar.setNavigationIcon(R.drawable.icon_app);
        setSupportActionBar(toolBar);
        //get all song from sd card

        SongManager songManager = new SongManager();
        arrSongs = songManager.getPlayList();

        //Media player is used to play music
        mp = new MediaPlayer();

        // map views to player_activity_xml
        btnPlay=(ImageView) findViewById(R.id.btn_play);
        btnNext=(ImageView) findViewById(R.id.btn_next_song);
        btnPrevious=(ImageView) findViewById(R.id.btn_back_song);
        btnForward=(ImageView) findViewById(R.id.btn_forward);
        btnBackward=(ImageView) findViewById(R.id.btn_rewind);
        songProgressBar=(SeekBar) findViewById(R.id.progress_seekbar);
        lblCurrentDuration=(TextView) findViewById(R.id.tv_current_time);
        lblTotalDuration=(TextView) findViewById(R.id.tv_total_time);
        // call event listener of each view
        btnPlay.setOnClickListener(this);
        btnForward.setOnClickListener(this);
        btnBackward.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        
        songProgressBar.setOnSeekBarChangeListener(this);// bat su kien de user biet bai hat dang tua den dau
        mp.setOnCompletionListener(this);// su kien de biet khi   nao mediaplayer choi xong 1 bai nhac
        
        playSong(songIndex);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
         MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_menu, menu);
        return true;
    }
    private void playSong(int songIndex) {
        //Play song
        try {
            mp.reset();
            mp.setDataSource(arrSongs.get(songIndex).path);
            mp.prepare();
            mp.start();

            //Update title of toolbar
            toolBar.setTitle(arrSongs.get(songIndex).title);

            //Changing Button Image to pause image
            btnPlay.setImageResource(R.drawable.pause);

            //set Progress bar values
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);

            //Updating progress bar
            updateProgressBar();

            //notification
            buildNotification();

        } catch (IllegalArgumentException e){
            e.pintStackTrace();
        }catch (IllegalStateException e){
            e.pintStackTrace();
        }catch (IOException e){
            e.pintStackTrace();
        }
    }

    /**
     *Update timer on seekbar
     */
    public void updateProgressBar(){
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     */
    private  Runnable mUpdateTimeTask = () -> {
        public void run(){
            try {
                long totalDuration = mp.getDuration();
                long curentDuảtion = mp.getCurentPosition();

                //Displaying Total Duration time
                lblTotalDuration.setText(""+Util.milliSecondsToTimer(totalDuration));
                //Displaying time complete playing
                lblCurrentDuration.setText(""+Util.milliSecondsToTimer(currentDuration));

                //Updating progress bar
                int progress = (int)(Util.getProgressPercentage(currentDuration, totalDuration));
                songProgressBar.setProgress(progress);
                //Running this thread after 100 millisecond
                mHandler.postDelayed(this, 100);
            }catch (Exception ex){

            }
        }
    };

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(songIndex < (arrSongs.size() - 1)){
            playSong(songIndex + 1);
            songIndex = songIndex + 1;
        }else{
            //play first song
            playSong(0);
            songIndex = 0;
        }
        buildNotification();
    }
    private  void buildNotification(){
        Intent intent = new Intent(getApplicationContext(),PlayerActivity.class);
        PendingIntent pendingIntent = PendingItent.getService(getApplicationContext() , 1 ,ontent 0)
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.song)
                .setContentTitle("Media Artist")
                .setContentText(arrSongs.get(songIndex).title)
                .setDeleteIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) ()
    }
    @Override
    public void onClick(View v) {

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
