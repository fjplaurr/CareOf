package com.model;

import com.google.firebase.database.*;

public class Diary {
    public String Emisor;
    public String Hora;
    public String Mensaje;
    public String Receptor;

    public Diary() {
    } // Default constructor required for calls to DataSnapshot.getValue(User.class)

    public Diary(String Emisor, String Hora, String Mensaje, String Receptor) {
        this.Emisor = Emisor;
        this.Hora = Hora;
        this.Mensaje=Mensaje;
        this.Receptor=Receptor;
    }

    public void AgregarDiario (){
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Diarios").child(Receptor);
        mDatabase.child(this.Hora).setValue(this);
    }

}
