package com.controlador.familiares;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.view.View;
import android.widget.TextView;
import com.controlador.pfc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.modelo.Message;

/**
 * En esta clase se muestra el diario de la persona dependiente
 */
public class DiaryMentor extends AppCompatActivity {
    private String nombrePDependiente;
    private Toolbar mToolbar;
    private RecyclerView RVgenerico;
    private DatabaseReference mDatabase;
    private String mCurrentUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vlistado_generico);

        //sacamos de las preferencias el nombre del usuario
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //sacamos de los extras enviados de la clase anterior el nombre de la persona dependiente
        nombrePDependiente = datos.getString("pDependiente", "");

        //Usuario actual
        mAuth=FirebaseAuth.getInstance();
        mCurrentUser= mAuth.getCurrentUser().getUid();

        //Inicialización DatabaseRefence
        //mDatabase = FirebaseDatabase.getInstance().getReference().child("Diarios").child(nombrePDependiente);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Diarios").child(mCurrentUser);
        //Toolbar
        mToolbar=(Toolbar)findViewById(R.id.TBgenerico);
        setSupportActionBar(mToolbar);

        //RecyclerView
        RVgenerico=(RecyclerView)findViewById(R.id.RVgenerico);
        RVgenerico.setHasFixedSize(true);
        RVgenerico.setLayoutManager(new LinearLayoutManager(this));
    }

    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Message,DiaryMentor.GruposViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Message, DiaryMentor.GruposViewHolder>(
                        Message.class,
                        R.layout.dos_elementos,
                        DiaryMentor.GruposViewHolder.class,
                        mDatabase) {
                    @Override
                    protected void populateViewHolder(DiaryMentor.GruposViewHolder viewHolder, final Message mensaje, int position) {
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
            String horaAMostrar= hora.substring(6,8)+" DE "+mes+" "+hora.substring(9,11)+":"+hora.substring(11,13);
            TVhora.setText(horaAMostrar);
        }
    }

}


