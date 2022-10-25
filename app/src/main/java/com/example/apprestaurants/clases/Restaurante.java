package com.example.apprestaurants.clases;

import java.io.Serializable;

public class Restaurante implements Serializable {

    int id_restaurante;
    String nombre;
    String descripcion;
    String foto;
    float promedio;


    public Restaurante(){

    }
    public Restaurante(String nombre) {
        this.nombre = nombre;
    }

    public Restaurante(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Restaurante(String nombre, String descripcion, String foto) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.foto = foto;
    }

    public Restaurante(String nombre, String descripcion, String foto, float promedio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.foto = foto;
        this.promedio = promedio;
    }

    public Restaurante(int id_restaurante, String nombre, String descripcion, String foto, float promedio) {
        this.id_restaurante=id_restaurante;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.foto = foto;
        this.promedio = promedio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public float getPromedio() {
        return promedio;
    }

    public void setPromedio(float promedio) {
        this.promedio = promedio;
    }

    public int getId_restaurante() {
        return id_restaurante;
    }

    public void setId_restaurante(int id_restaurante) {
        this.id_restaurante = id_restaurante;
    }
}
