package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


import static com.example.myapplication.MainActivity.musicFiles;

public class PlayerActivity extends AppCompatActivity {

    TextView durationPlayed,title,Artist,durationtotal;
    ImageView coverArt,nextbtn,prevbtn,shufflebtn,repeatbtn,backBtn;
    SeekBar seekBar;
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler=new Handler();
    int position=-1;
    FloatingActionButton playbtn;
    static ArrayList<MusicFiles> musicList =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initViews();
        getIntentmethod();

        title.setText(musicList.get(position).getTitle());
        Artist.setText(musicList.get(position).getArtist());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer!=null&&  b){
                    mediaPlayer.seekTo(i*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    int currentPosition = mediaPlayer.getCurrentPosition()/1000;
                    seekBar.setProgress(currentPosition);
                    durationPlayed.setText(formattedTime(currentPosition));
                    int duration =mediaPlayer.getDuration()/1000;
                    durationtotal.setText(formattedTime(duration));
                }
                handler.postDelayed(this,1000);
            }
        });
    }


    private void initViews() {

        durationPlayed =findViewById(R.id.durationPlayed);
        durationtotal =findViewById(R.id.durationTotal);
        title =findViewById(R.id.title);
        Artist =findViewById(R.id.artist);
        coverArt=(ImageView) findViewById(R.id.songImage);
        nextbtn =findViewById(R.id.next);
        prevbtn =findViewById(R.id.prev);
        shufflebtn=findViewById(R.id.shuffle);
        repeatbtn =findViewById(R.id.repeat);
        seekBar=findViewById(R.id.seekBar);
        playbtn =findViewById(R.id.play);
        backBtn =findViewById(R.id.back_btn);
    }




    private void getIntentmethod() {
        position = getIntent().getIntExtra("position",-1);
        musicList = musicFiles;
        if(musicList!= null){
            playbtn.setImageResource(R.drawable.pause);
            uri =Uri.parse(musicList.get(position).getPath());
        }
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        seekBar.setMax(mediaPlayer.getDuration()/1000);
        metaData(uri);
    }

    private void metaData(Uri uri){
        MediaMetadataRetriever retriever= new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        byte[] art = retriever.getEmbeddedPicture();

        if (art!=null){
            Glide.with(PlayerActivity.this)
                    .asBitmap()
                    .load(art)
                    .into(coverArt);
        }else {
            Glide.with(this).asBitmap().load(R.drawable.music).into(coverArt);
        }


    }


    private String formattedTime(int currentPosition) {
        String totalout;
        String totalnew;
        String seconds =String.valueOf(currentPosition%60);
        String minutes =String.valueOf(currentPosition/60);
        totalout = minutes + ":" + seconds;
        totalnew = minutes + ":"+"0"+seconds;
        if(seconds.length() ==1){
            return totalnew;
        }
        else{
            return  totalout;
        }
    }
}