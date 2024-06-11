package com.example.reproducer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

public class ImageDownloader {

    private static RequestQueue colaPeticiones ;
    private final static String TAG = ImageDownloader.class.getName();

    /**
     * Descarga una imagen de una URL y la carga en un ImageView utilizando Picasso.
     *
     * @param url       La URL de la imagen a descargar.
     * @param imageView El ImageView donde se cargará la imagen descargada.
     */
    public static void downloadImage(String url, ImageView imageView) {
        Picasso.get().load(url).into(imageView);
    }

    /**
     * Descarga una imagen de una URL y la carga en un ImageView utilizando Volley.
     *
     * @param context         El contexto de la aplicación.
     * @param url             La URL de la imagen a descargar.
     * @param imageView       El ImageView donde se cargará la imagen descargada.
     * @param defaultDrawable El recurso drawable que se mostrará si la descarga de la imagen falla.
     */
    public static void downloadImage(Context context, String url, ImageView imageView, int defaultDrawable) {
        ImageRequest request = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null, // maxWidth, maxHeight, decodeConfig
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        imageView.setImageResource(defaultDrawable);
                        Log.e(TAG, error.getMessage());
                    }
                }
        );
        getRequestQueue(context).add(request);
    }

    /**
     * Obtiene la cola de peticiones de Volley para la aplicación. Si no existe, se crea una nueva.
     *
     * @param context El contexto de la aplicación.
     * @return La cola de peticiones de Volley.
     */
    private static RequestQueue getRequestQueue(Context context) {
        if (colaPeticiones == null)
            colaPeticiones = Volley.newRequestQueue(context);
        return colaPeticiones;
    }

}
