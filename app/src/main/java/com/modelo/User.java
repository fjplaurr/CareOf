package com.modelo;

import com.google.firebase.database.*;

public class User  {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTipousuario() {
        return tipousuario;
    }

    public void setTipousuario(String tipousuario) {
        this.tipousuario = tipousuario;
    }

    public String username;
    public String tipousuario;

    public User() {
    } // Default constructor required for calls to DataSnapshot.getValue(User.class)

    public User(String username, String tipousuario) {
        this.username = username;
        this.tipousuario=tipousuario;
    }

}
