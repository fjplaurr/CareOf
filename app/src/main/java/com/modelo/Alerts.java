package com.modelo;

public class Alerts {

    public String emisor,hora,mensaje;

    public Alerts(){}

    public Alerts(String hora, String alerta) {
        this.hora = hora;
        this.mensaje = mensaje;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getAlerta() {
        return mensaje;
    }

    public void setAlerta(String alerta) {
        this.mensaje = alerta;
    }
    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

}
