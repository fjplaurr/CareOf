package com.controlador.cuidadores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.view.View;
import android.widget.TextView;
import com.controlador.pfc.R;
import com.controlador.Chat;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.*;
import com.modelo.DependentPerson;

/**
 * DIARIO
 * Esta clase se usa para listar las personas dependientes de la pestaña diarios
 */

public class ListDependentPeople extends AppCompatActivity {
    public String usuario, aplicacion;
    private RecyclerView RVgenerico;
    private DatabaseReference mPersDep;
    private android.support.v7.widget.Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vlistado_generico);
        //Toolbar
        mToolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.TBgenerico);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Seleccione la persona dependiente");

        //DatabaseReference
        mPersDep=FirebaseDatabase.getInstance().getReference().child("DependentPerson");

        Bundle bundle=getIntent().getExtras();
        usuario=bundle.getString("nombre");
            //La variable aplicacion determina qué botón se ha pulsado: diario o chat.
        aplicacion=bundle.getString("aplicacion");

        //RecyclerView
        RVgenerico=(RecyclerView)findViewById(R.id.RVgenerico);
        RVgenerico.setHasFixedSize(true);
        RVgenerico.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<DependentPerson,ListDependentPeople.UsersViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<DependentPerson, ListDependentPeople.UsersViewHolder>(
                DependentPerson.class,
                R.layout.unelemento_list,
                ListDependentPeople.UsersViewHolder.class,
                mPersDep) {
            @Override
            public void populateViewHolder(ListDependentPeople.UsersViewHolder usersViewHolder, final DependentPerson pDep, int position) {
                usersViewHolder.setNombre(pDep.getNombre());
                final String id_familiar=getRef(position).getKey();
                usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(aplicacion.equals("diario")){
                            Intent vdiarioIntent=new Intent(ListDependentPeople.this,DiaryCarers.class);
                            vdiarioIntent.putExtra("id_familiar",id_familiar);
                            vdiarioIntent.putExtra("nombrePdependiente",pDep.getNombre());
                            startActivity(vdiarioIntent);
                        }else if(aplicacion.equals("chat")){
                            Intent vChatIntent=new Intent(ListDependentPeople.this,Chat.class);
                            vChatIntent.putExtra("id_familiar",id_familiar);
                            startActivity(vChatIntent);
                        }

                    }
                });
            }
        };

        RVgenerico.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setNombre(String nombre) {
            TextView TVnombre = (TextView) mView.findViewById(R.id.TVelemento);
            TVnombre.setText(nombre);
        }
    }

}



