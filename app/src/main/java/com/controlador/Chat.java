package com.controlador;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;
import android.widget.*;
import com.controlador.pfc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.modelo.Message;
import java.util.HashMap;

/**
 * En esta clase se muestra el diario de la persona dependiente seleccionada en la clase ListDependentPeople
 */
public class Chat extends AppCompatActivity {
    private RecyclerView RVgenerico;
    private DatabaseReference mDatabase, mRootRef;
    private String mCurrentUser;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ImageButton btn_enviar;
    private EditText et_texto;
    private String idFamiliar;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vlistado_generico_con_enviar);
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Bundle extras=getIntent().getExtras();
        idFamiliar =extras.getString("id_familiar");

        //Usuario actual
        mAuth=FirebaseAuth.getInstance();
        mCurrentUser= mAuth.getCurrentUser().getUid();

        //Inicialización DatabaseRefence
        mRootRef=FirebaseDatabase.getInstance().getReference();
        mDatabase = mRootRef.child("Chat").child(idFamiliar);
        query=mDatabase.orderByChild("hora");

        //Toolbar
        mToolbar=(Toolbar)findViewById(R.id.TBgenerico);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Chat");

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
            DatabaseReference alerta_push=mRootRef.child("Chat").child(idFamiliar).push();
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
        FirebaseRecyclerAdapter<Message,Chat.GruposViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Message, Chat.GruposViewHolder>(
                        Message.class,
                        R.layout.chat,
                        Chat.GruposViewHolder.class,
                        query) {
                    @Override
                    protected void populateViewHolder(final Chat.GruposViewHolder viewHolder, final Message mensaje, int position) {
                        viewHolder.setFecha(mensaje.getHora());

                        if(mensaje.getEmisor().equals(mCurrentUser)){
                            viewHolder.setMensaje(mensaje.getMensaje(),Color.WHITE,R.drawable.message_text_background);
                            viewHolder.setNombre("Yo");
                        }else{
                            viewHolder.setMensaje(mensaje.getMensaje(),Color.WHITE,R.drawable.message_text_background_ii);
                            String id_emisor=mensaje.getEmisor();
                            DatabaseReference mDatabase2=mRootRef.child("users").child(id_emisor).child("username");
                            mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    viewHolder.setNombre(dataSnapshot.getValue().toString());
                                }
                                @Override public void onCancelled(DatabaseError databaseError) {}
                            });
                        }
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

        public void setNombre(String nombre){
            TextView TVnombre=(TextView)mView.findViewById(R.id.TVnombre_chat);
            TVnombre.setText(nombre);
        }

        public void setMensaje(String mensaje, int color, int contornoTexto){
            TextView TVmensaje=(TextView)mView.findViewById(R.id.TVmensaje_chat);
            TVmensaje.setTextColor(color);
            TVmensaje.setBackgroundResource(contornoTexto);
            TVmensaje.setText(mensaje);
        }

        public void setFecha(String hora){
            TextView TVhora=(TextView)mView.findViewById(R.id.TVfecha_chat);
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