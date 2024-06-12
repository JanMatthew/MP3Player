package com.example.reproducer.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reproducer.ImageDownloader;
import com.example.reproducer.MainActivity;
import com.example.reproducer.Model.Cancion;
import com.example.reproducer.R;

import java.util.List;
import java.util.Locale;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.PlayListViewHolder> {
    private List<Cancion> canciones;
    private LayoutInflater layoutInflater;
    private View.OnClickListener onClickListener;
    private Context context;

    public PlayListAdapter(Context context){

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;

    }

    public void setCanciones(List<Cancion> canciones){

        this.canciones = canciones;

    }

    @NonNull
    @Override
    public PlayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_cancion, parent, false);
        view.setOnClickListener(onClickListener);
        return new PlayListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListViewHolder holder, int position) {

        Cancion currentCancion = canciones.get(position);
        MediaPlayer mediaPlayer = MediaPlayer.create(context,currentCancion.getSource());
        ImageDownloader.downloadImage(currentCancion.getCover(),holder.cover);
        holder.titulo.setText(currentCancion.getTitulo());

        int minDur = mediaPlayer.getDuration()/1000/60;
        int secDur = mediaPlayer.getDuration()/1000%60;

        holder.duracion.setText(minDur + ":" + secDur);

        mediaPlayer.release();

    }

    @Override
    public int getItemCount() {
        return canciones.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    static class PlayListViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView titulo,duracion;
        PlayListViewHolder(@NonNull View itemView){
            super(itemView);
            cover = itemView.findViewById(R.id.coverItem);
            titulo = itemView.findViewById(R.id.tituloItem);
            duracion = itemView.findViewById(R.id.duracionItem);

        }
    }
}
