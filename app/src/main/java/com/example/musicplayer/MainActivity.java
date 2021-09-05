package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer mediaPlayer;
    private ImageView artistImage;
    private TextView leftTime;
    private TextView rightTime;
    private SeekBar seekBar;
    private Button prevButton;
    private Button playButton;
    private Button nextButton;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpUI();

        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser)
                {
                    mediaPlayer.seekTo(progress);
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
                int currentPos = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();



                leftTime.setText(dateFormat.format(new Date(currentPos-1800000)));

                rightTime.setText(dateFormat.format(new Date(duration-1800000)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setUpUI()
    {
        mediaPlayer= new MediaPlayer();
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.amplifier);

        artistImage = (ImageView)findViewById(R.id.imageView);
        leftTime = (TextView)findViewById(R.id.leftTimeID);
        rightTime = (TextView)findViewById(R.id.rightTimeID);
        seekBar = (SeekBar)findViewById(R.id.seekBarID);
        prevButton = (Button)findViewById(R.id.prevButtonID);
        playButton = (Button)findViewById(R.id.playButtonID);
        nextButton = (Button)findViewById(R.id.nextButtonID);

        playButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.prevButtonID:
                backMusic();
                break;
            case R.id.playButtonID:
                if(mediaPlayer.isPlaying())
                {
                    pauseMusic();
                }
                else
                {
                    startMusic();
                }
                break;
            case R.id.nextButtonID:
                nextMusic();
                break;
        }
    }

    public void pauseMusic()
    {
        if(mediaPlayer!=null)
        {
            mediaPlayer.pause();
            playButton.setBackgroundResource(android.R.drawable.ic_media_play);
        }
    }

    public void startMusic()
    {
        if(mediaPlayer!=null)
        {
            mediaPlayer.start();
            updateThread();
            playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
        }
    }

    public void backMusic()
    {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.seekTo(0);
        }
    }

    public void nextMusic()
    {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.seekTo(mediaPlayer.getDuration()-1000);
        }
    }

    public void updateThread()
    {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                        while(mediaPlayer!=null && mediaPlayer.isPlaying()) {
                            Thread.sleep(50);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    int newPositon = mediaPlayer.getCurrentPosition();
                                    int newMax = mediaPlayer.getDuration();
                                    seekBar.setMax(newMax);
                                    seekBar.setProgress(newPositon);

                                    leftTime.setText(String.valueOf(new java.text.SimpleDateFormat("mm:ss").format(new Date(mediaPlayer.getCurrentPosition()-1800000))));
                                    rightTime.setText(String.valueOf(new java.text.SimpleDateFormat("mm:ss").format(new Date(mediaPlayer.getDuration()-1800000))));
                                }
                            });
                        }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}