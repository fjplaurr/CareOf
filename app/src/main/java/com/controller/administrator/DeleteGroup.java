package com.controller.administrator;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.controller.pfc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.model.Group;

public class DeleteGroup extends AppCompatActivity {
    private RecyclerView RVgenerico;
    private DatabaseReference mDatabase;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generic_list);

        mToolbar=(Toolbar)findViewById(R.id.TBgenerico);
        setSupportActionBar(mToolbar);
        //Inicialización DatabaseRefence
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Group");

        //RecyclerView
        RVgenerico=(RecyclerView)findViewById(R.id.RVgenerico);
        RVgenerico.setHasFixedSize(true);
        RVgenerico.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Group,GruposViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Group, GruposViewHolder>(
                Group.class,
                R.layout.one_element,
                GruposViewHolder.class,
                mDatabase) {

            @Override
            protected void populateViewHolder(GruposViewHolder viewHolder, final Group grupos, int position) {
                viewHolder.setGrupo(grupos.getNombre());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertTwoButtons(grupos.getNombre());
                    }
                });
            }
        };
        RVgenerico.setAdapter(firebaseRecyclerAdapter);
    }

    public void alertTwoButtons(final String GrupoBorrar) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(DeleteGroup.this);
        builder.setTitle("Borrar grupo");
        builder.setMessage("¿Está seguro de eliminar el grupo "+GrupoBorrar+" ?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // ELIMINAR GRUPO DE LA BASE DE DATOS
                final DatabaseReference referenciaGrupoaBorrar=FirebaseDatabase.getInstance().getReference("Group").child(GrupoBorrar);
                referenciaGrupoaBorrar.removeValue();   //Elimina el campo del User de la BD
                dialog.cancel();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public static class GruposViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public GruposViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setGrupo(String grupo){
            TextView TVgrupo=(TextView)mView.findViewById(R.id.TVelemento);
            TVgrupo.setText("Grupo "+grupo);
        }
    }

}
