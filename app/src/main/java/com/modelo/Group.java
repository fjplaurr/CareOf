package com.modelo;

import com.google.firebase.database.*;

public class Group {
    public String cuidador;
    public String nombre;

    public Group() {
    } // Default constructor required for calls to DataSnapshot.getValue(Group.class)

    public Group(String cuidador, String nombre) {
        this.cuidador = cuidador;
        this.nombre = nombre;
    }

    public String getCuidador() {
        return cuidador;
    }

    public void setCuidador(String cuidador) {
        this.cuidador = cuidador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
