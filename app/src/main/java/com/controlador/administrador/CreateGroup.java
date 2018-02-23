package com.controlador.administrador;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;
import com.controlador.pfc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;
import com.modelo.Group;

import java.util.*;

public class CreateGroup extends AppCompatActivity {
    private DatabaseReference fDatabaseRoot;
    private EditText ETgrupo;
    private String STgrupo;
    private Button BTcrear_grupo;
    private Spinner miSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vcrear_grupo);

        //Inicialización Layout
        ETgrupo=(EditText)findViewById(R.id.ETgrupo);
        BTcrear_grupo=(Button) findViewById(R.id.BTcrear_grupo);

        //Inicialización DatabaseReference
        fDatabaseRoot=FirebaseDatabase.getInstance().getReference();

        //toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.TBcrear_grupo);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Crear Grupo");

        //Spinner
        fDatabaseRoot.child("users").orderByChild("tipousuario").equalTo("cuidador").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> listCuidadores=new ArrayList<String>();
                for(DataSnapshot cuidadoresSnapShot: dataSnapshot.getChildren()){
                    String pruebaNombre=cuidadoresSnapShot.child("username").getValue().toString();
                    listCuidadores.add(pruebaNombre);
                    miSpinner=(Spinner)findViewById(R.id.SPcrear_grupo);
                    ArrayAdapter<String> miAdapter=new ArrayAdapter<String>(CreateGroup.this,android.R.layout.simple_spinner_item,listCuidadores);
                    miAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //Vista del Spinner
                    miSpinner.setAdapter(miAdapter);
                }
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });

        //Botón
        BTcrear_grupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                STgrupo=ETgrupo.getText().toString();
                if(!STgrupo.isEmpty()){
                    final ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange( DataSnapshot dataSnapshot) {
                            String nombreFamiliar= miSpinner.getSelectedItem().toString();
                            Query familiar=fDatabaseRoot.child("users").orderByChild("username").equalTo(nombreFamiliar);
                            familiar.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String id=dataSnapshot.getValue().toString().substring(1,29);
                                    Group grupo=new Group(id,STgrupo);
                                    fDatabaseRoot.child("Group").child(STgrupo).setValue(grupo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(CreateGroup.this,"Se ha creado el nuevo grupo.",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                @Override public void onCancelled(DatabaseError databaseError) {}
                            });

                            }
                            @Override public void onCancelled(DatabaseError databaseError) {}
                    };
                    fDatabaseRoot.child("Group").addValueEventListener(valueEventListener);

                }else{
                    Toast.makeText(CreateGroup.this,"El campo del grupo debe rellenarse.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
