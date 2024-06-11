package com.example.reproducer;

import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button play,stop,prev,next;
    private SeekBar barra;
    private ImageView portada;
    private Handler handler;
    private MediaPlayer mediaPlayer;
    private List<Integer> canciones = new ArrayList<>();
    private HashMap<Integer ,String> covers = new HashMap<>();
    private int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        barra = findViewById(R.id.barra);
        portada = findViewById(R.id.portada);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);

        canciones.add(R.raw.one);
        canciones.add(R.raw.torii);

        covers.put(R.raw.one,"https://i1.sndcdn.com/artworks-000058395969-vegj94-t500x500.jpg");
        covers.put(R.raw.torii,"https://t2.genius.com/unsafe/504x504/https%3A%2F%2Fimages.genius.com%2Fb9a026c00e5a81c64a51005fed0b1836.1000x1000x1.png");


        mediaPlayer = MediaPlayer.create(this,canciones.get(pos));
        barra.setMax(mediaPlayer.getDuration());
        ImageDownloader.downloadImage(covers.get(canciones.get(pos)),portada);

        play.setOnClickListener(view->{
            mediaPlayer.start();
            Toast.makeText(this,"Si lo hace", Toast.LENGTH_SHORT).show();
            handler.postDelayed(updateSeekBar, 100);
        });
        stop.setOnClickListener(view->{
            mediaPlayer.pause();
            handler.removeCallbacks(updateSeekBar);
        });

        next.setOnClickListener(view->{

            if (pos == canciones.size()-1){
                pos = 0;
            }
            else{
                ++pos;
            }
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            mediaPlayer = MediaPlayer.create(this,canciones.get(pos));
            ImageDownloader.downloadImage(covers.get(canciones.get(pos)),portada);
            barra.setMax(mediaPlayer.getDuration());
        });

        prev.setOnClickListener(view->{

            if (pos == 0){
                pos = canciones.size()-1;
            }
            else{
                --pos;
            }
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            mediaPlayer = MediaPlayer.create(this,canciones.get(pos));
            ImageDownloader.downloadImage(covers.get(canciones.get(pos)),portada);
            barra.setMax(mediaPlayer.getDuration());

        });
        barra.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        handler = new Handler();

    }

    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if(mediaPlayer != null){
                barra.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this,100);
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }
}