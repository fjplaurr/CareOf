package com.model;

import com.google.firebase.database.*;

public class Message {
    public String emisor;
    public String hora;
    public String mensaje;

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String receptor;

    public Message() {
    } // Default constructor required for calls to DataSnapshot.getValue(User.class)

    public Message(String emisor, String hora, String mensaje, String receptor) {
        this.emisor = emisor;
        this.hora = hora;
        this.mensaje=mensaje;
        this.receptor=receptor;
    }

    public void AgregarMensaje (String pDependiente){
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Mensajes").child(pDependiente);
        mDatabase.child(this.hora).setValue(this);
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
