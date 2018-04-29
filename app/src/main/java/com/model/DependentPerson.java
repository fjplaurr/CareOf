package com.model;

public class DependentPerson {
    public String familiar;
    public String grupo;
    public String nombre;
    public String cuidador;

    public DependentPerson() {

    }

    public DependentPerson(String familiar, String grupo, String nombre, String cuidador) {
        this.familiar = familiar;
        this.grupo = grupo;
        this.nombre = nombre;
        this.cuidador = cuidador;
    }

    public String getFamiliar() {
        return familiar;
    }

    public void setFamiliar(String familiar) {
        this.familiar = familiar;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCuidador() {
        return cuidador;
    }

    public void setCuidador(String cuidador) {
        this.cuidador = cuidador;
    }
}
