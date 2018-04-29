package com.controller.carers;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;
import android.widget.*;
import com.controller.pfc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.model.Message;
import java.util.HashMap;

public class CarerAlerts extends AppCompatActivity{
    private RecyclerView RVgenerico;
    private DatabaseReference mDatabase, mRootRef;
    private String mCurrentUser;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ImageButton btn_enviar;
    private EditText et_texto;
    private String idFamiliar,idgrupo, nombrePdependiente;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generic_list_with_send_btn);
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //Datos del familiar
        Bundle extras=getIntent().getExtras();
        idFamiliar =extras.getString("id_familiar");
        idgrupo=extras.getString("grupo");
        nombrePdependiente =extras.getString("nombrePdependiente");


        //Usuario actual
        mAuth=FirebaseAuth.getInstance();
        mCurrentUser= mAuth.getCurrentUser().getUid();

        //Inicialización DatabaseRefence
        mRootRef=FirebaseDatabase.getInstance().getReference();

        if(idFamiliar!=null){
            mDatabase = mRootRef.child("Alerts").child("Individuales").child(idFamiliar);
            query=mDatabase.orderByChild("hora");
        }else{
            mDatabase = mRootRef.child("Alerts").child("Grupales").child(idgrupo);
            query=mDatabase.orderByChild("hora");
        }

        //Toolbar
        mToolbar=(Toolbar)findViewById(R.id.TBgenerico);
        setSupportActionBar(mToolbar);
        if(nombrePdependiente!=null){
            getSupportActionBar().setTitle("Alerts de "+ nombrePdependiente);
        }
        else{
            getSupportActionBar().setTitle("Alerts del grupo "+ idgrupo);
        }


        //RecyclerView
        RVgenerico=(RecyclerView)findViewById(R.id.RVgener_enviar);
        RVgenerico.setHasFixedSize(true);
        RVgenerico.setLayoutManager(new LinearLayoutManager(this));

        //Layout
        et_texto=(EditText)findViewById(R.id.ETlist_env);
        btn_enviar=(ImageButton)findViewById(R.id.BTlist_env);
        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarMensaje();
                //Para limpiar el EditText tras enviar.
                et_texto.getText().clear();
            }
        });
    }

    private void enviarMensaje() {
        String mensaje= et_texto.getText().toString();
        if(!TextUtils.isEmpty(mensaje)){
            DatabaseReference alerta_push;
            if(idFamiliar!=null) {
                alerta_push = mRootRef.child("Alerts").child("Individuales").child(idFamiliar).push();
            }else{
                alerta_push = mRootRef.child("Alerts").child("Grupales").child(idgrupo).push();
            }
            HashMap mi_hashMap=new HashMap<>();
            Time mitime = new Time();
            mitime.setToNow();
            final String fecha = mitime.toString().substring(0, 15);
            mi_hashMap.put("emisor",mCurrentUser);
            mi_hashMap.put("hora", fecha);
            mi_hashMap.put("mensaje", mensaje);

            alerta_push.setValue(mi_hashMap);
        }
    }

    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Message,CarerAlerts.GruposViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Message, CarerAlerts.GruposViewHolder>(
                        Message.class,
                        R.layout.two_elements,
                        CarerAlerts.GruposViewHolder.class,
                        query) {
                    @Override
                    protected void populateViewHolder(CarerAlerts.GruposViewHolder viewHolder, final Message mensaje, int position) {
                        viewHolder.setMensaje(mensaje.getMensaje());
                        viewHolder.setHora(mensaje.getHora());
                    }
                };
        RVgenerico.setAdapter(firebaseRecyclerAdapter);
    }

    public static class GruposViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public GruposViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setMensaje(String mensaje){
            TextView TVmensaje=(TextView)mView.findViewById(R.id.TVAlerta);
            TVmensaje.setText(mensaje);
        }

        public void setHora(String hora){
            TextView TVhora=(TextView)mView.findViewById(R.id.TVHora);
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
            TVhora.setText(horaAMostrar);
        }
    }

}
