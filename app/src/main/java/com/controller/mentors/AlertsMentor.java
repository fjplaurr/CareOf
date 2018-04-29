package com.controller.mentors;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.view.View;
import android.widget.TextView;
import com.controller.pfc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.model.Alerts;

public class AlertsMentor extends AppCompatActivity{
    private RecyclerView RVAlertas;
    private DatabaseReference mAlertasDatabase;
    public String nombrePDependiente;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generic_list);

        //sacamos de las preferencias el nombre del usuario
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //sacamos de los extras enviados de la clase anterior el nombre de la persona dependiente
        nombrePDependiente = datos.getString("pDependiente", "");

        //Usuario actual
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String mCurrentUser= mAuth.getCurrentUser().getUid();
        //Inicialización DatabaseRefence
        //mAlertasDatabase = FirebaseDatabase.getInstance().getReference().child("Alerts").child(nombrePDependiente);
        mAlertasDatabase = FirebaseDatabase.getInstance().getReference().child("Alerts").child("Individuales").child(mCurrentUser);

        //RecyclerView
        RVAlertas=(RecyclerView)findViewById(R.id.RVgenerico);
        RVAlertas.setHasFixedSize(true);
        RVAlertas.setLayoutManager(new LinearLayoutManager(this));
        //Toolbar
        mToolbar=(Toolbar)findViewById(R.id.TBgenerico);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Alerts");

    }

    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Alerts,UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Alerts,
                UsersViewHolder>(
                        Alerts.class,
                        R.layout.two_elements,
                        UsersViewHolder.class,
                        mAlertasDatabase) {

                    @Override
                    protected void populateViewHolder(UsersViewHolder usersViewHolder, Alerts alertas, int position) {
                        usersViewHolder.setAlerta(alertas.getAlerta());
                        usersViewHolder.setHora(alertas.getHora());
                    }
                };
        RVAlertas.setAdapter(firebaseRecyclerAdapter);
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public UsersViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setAlerta(String alerta){
            TextView TVAlerta=(TextView)mView.findViewById(R.id.TVAlerta);
            TVAlerta.setText(alerta);
        }

        public void setHora(String hora) {
            TextView TVHora=(TextView)mView.findViewById(R.id.TVHora);
            String mes=null;
            switch(hora.substring(4,6)){
                case "01": mes="ENERO"; break;
                case "02": mes="FEBRERO"; break;
                case "03": mes="MARZO"; break;
                case "04": mes="ABRIL"; break;
                case "05": mes="MAYO"; break;
                case "06": mes="JUNIO"; break;
                case "07": mes="JULIO";break;
                case "08": mes="AGOSTO"; break;
                case "09": mes="SEPTIEMBRE"; break;
                case "10": mes="OCTUBRE"; break;
                case "11": mes="NOVIEMBRE"; break;
                case "12": mes="DICIEMBRE"; break;
            }
            String horaAMostrar=
                    hora.substring(6,8)+" DE "+mes+" "+hora.substring(9,11)+":"+hora.substring(11,13);
            TVHora.setText(horaAMostrar);
        }
    }


}
