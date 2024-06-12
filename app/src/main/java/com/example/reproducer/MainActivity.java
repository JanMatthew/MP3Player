package com.example.reproducer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reproducer.Adapter.PlayListAdapter;
import com.example.reproducer.Model.Cancion;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button play,stop,prev,next;
    private ImageButton playBtn;
    private int minCur, secCur,minDur, secDur;
    private SeekBar barra;
    private ImageView portada;
    private Handler handler;
    private MediaPlayer mediaPlayer;
    private RecyclerView recyclerView;
    private PlayListAdapter adapter;
    private List<Cancion> canciones = new ArrayList<>();
    private TextView time;
    private int pos = 0;
    private Boolean playing = false;

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
        recyclerView = findViewById(R.id.recyclerView);
        time = findViewById(R.id.time);
        playBtn = findViewById(R.id.playBtn);

        canciones.add(new Cancion(R.raw.one,"https://i1.sndcdn.com/artworks-000058395969-vegj94-t500x500.jpg","ONE"));
        canciones.add(new Cancion(R.raw.torii,"https://t2.genius.com/unsafe/504x504/https%3A%2F%2Fimages.genius.com%2Fb9a026c00e5a81c64a51005fed0b1836.1000x1000x1.png","Torii"));
        adapter = new PlayListAdapter(this);
        adapter.setCanciones(canciones);
        adapter.setOnClickListener(view->{
            int position = recyclerView.getChildAdapterPosition(view);
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            mediaPlayer = MediaPlayer.create(this,canciones.get(position).getSource());
            ImageDownloader.downloadImage(canciones.get(position).getCover(),portada);
            barra.setMax(mediaPlayer.getDuration());

            minCur = mediaPlayer.getCurrentPosition()/1000/60;
            secCur = mediaPlayer.getCurrentPosition()/1000%60;

            minDur = mediaPlayer.getDuration()/1000/60;
            secDur = mediaPlayer.getDuration()/1000%60;

            time.setText(minCur + ":" + secCur + "/" +minDur + ":" + secDur);

            pos = position;

        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mediaPlayer = MediaPlayer.create(this,canciones.get(pos).getSource());
        barra.setMax(mediaPlayer.getDuration());

        minCur = mediaPlayer.getCurrentPosition()/1000/60;
        secCur = mediaPlayer.getCurrentPosition()/1000%60;

        minDur = mediaPlayer.getDuration()/1000/60;
        secDur = mediaPlayer.getDuration()/1000%60;

        time.setText(minCur + ":" + secCur + "/" +minDur + ":" + secDur);

        ImageDownloader.downloadImage(canciones.get(pos).getCover(),portada);

        playBtn.setOnClickListener(view->{
            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                playBtn.setImageResource(android.R.drawable.ic_media_play);
            }
            else {

                mediaPlayer.start();
                Toast.makeText(this,"Si lo hace", Toast.LENGTH_SHORT).show();
                handler.postDelayed(updateSeekBar, 100);
                playBtn.setImageResource(android.R.drawable.ic_media_pause);

            }
        });

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
            mediaPlayer = MediaPlayer.create(this,canciones.get(pos).getSource());
            ImageDownloader.downloadImage(canciones.get(pos).getCover(),portada);
            barra.setMax(mediaPlayer.getDuration());

            minCur = mediaPlayer.getCurrentPosition()/1000/60;
            secCur = mediaPlayer.getCurrentPosition()/1000%60;

            minDur = mediaPlayer.getDuration()/1000/60;
            secDur = mediaPlayer.getDuration()/1000%60;

            time.setText(minCur + ":" + secCur + "/" +minDur + ":" + secDur);
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
            mediaPlayer = MediaPlayer.create(this,canciones.get(pos).getSource());
            ImageDownloader.downloadImage(canciones.get(pos).getCover(),portada);
            barra.setMax(mediaPlayer.getDuration());

            minCur = mediaPlayer.getCurrentPosition()/1000/60;
            secCur = mediaPlayer.getCurrentPosition()/1000%60;

            minDur = mediaPlayer.getDuration()/1000/60;
            secDur = mediaPlayer.getDuration()/1000%60;

            time.setText(minCur + ":" + secCur + "/" +minDur + ":" + secDur);

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

                minCur = mediaPlayer.getCurrentPosition()/1000/60;
                secCur = mediaPlayer.getCurrentPosition()/1000%60;

                time.setText(minCur + ":" + secCur + "/" +minDur + ":" + secDur);
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }
}