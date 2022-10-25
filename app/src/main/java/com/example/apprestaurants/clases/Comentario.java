package com.example.apprestaurants.clases;

public class Comentario {
    private String nombre;
    private String comentario;
    private float puntaje;

    public Comentario(){

    }

    public Comentario(String nombre, String comentario, float puntaje) {
        this.nombre = nombre;
        this.comentario = comentario;
        this.puntaje = puntaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public float getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(float puntaje) {
        this.puntaje = puntaje;
    }
}
