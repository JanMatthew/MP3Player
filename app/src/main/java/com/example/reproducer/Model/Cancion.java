package com.example.reproducer.Model;

public class Cancion {
    private int source;
    private String cover;
    private String titulo;

    public Cancion(int source, String cover, String titulo) {
        this.source = source;
        this.cover = cover;
        this.titulo = titulo;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}

