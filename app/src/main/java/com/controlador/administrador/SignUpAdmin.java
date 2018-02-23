package com.controlador.administrador;

import com.controlador.pfc.R;
import com.google.android.gms.tasks.*;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import com.google.firebase.iid.FirebaseInstanceId;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import java.util.HashMap;

public class SignUpAdmin extends AppCompatActivity {
	private Button btnRegistrarAdmin;
	private TextInputLayout usuarioAdmin, passAdmin,emailAdmin;
	private FirebaseAuth mAuth;
	private DatabaseReference mDatabase;
	private ProgressDialog mRegProgress;
	private Toolbar mToolbar;

	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.vregistrarcentro);

			mAuth = FirebaseAuth.getInstance();

			//Toolbar
			mToolbar=(Toolbar)findViewById(R.id.TBreg_admin);
			setSupportActionBar(mToolbar);
			getSupportActionBar().setTitle("Registrar administrador");

			//Inicio instancia del ProgressDialog
			mRegProgress=new ProgressDialog(this  );
	        
	        //configuramos el boton de registrar administrador
	        btnRegistrarAdmin = (Button) findViewById(R.id.btnRegistrarAdmin);
	        btnRegistrarAdmin.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	usuarioAdmin = (TextInputLayout) findViewById(R.id.usuarioAdmin);
	            	passAdmin = (TextInputLayout) findViewById(R.id.passAdmin);
	            	emailAdmin=(TextInputLayout)findViewById(R.id.email_administrador);

					String usuario=usuarioAdmin.getEditText().getText().toString();
					String password=passAdmin.getEditText().getText().toString();
					String email=emailAdmin.getEditText().getText().toString();

	            	//darlo de alta en la bd
					mRegProgress.setTitle("Registrando administrador");
					mRegProgress.setMessage("Espera mientras se crea la cuenta!");
					mRegProgress.setCanceledOnTouchOutside(false);   //Establece que el dialogo no pueda cerrarse si se toca fuera de la ventana.
					mRegProgress.show();
	            	register_user(usuario,email,password);
	            }
	        });
	    }


	private void register_user(final String display_name, final String email, final String password) {
		mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if (task.isSuccessful()) {
					FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();
					String uid=current_user.getUid();
					mDatabase=FirebaseDatabase.getInstance().getReference().child("users").child(uid);
						//Quiero almacenar un token en la BD cada vez que haglo log in. Para ello, utilizo la clase FirebaseInstanceId.
					String device_token= FirebaseInstanceId.getInstance().getToken();
						//Para almacenar datos en la Base de datos, se puede utilizar un HashMap en lugar de crear una clase.
					HashMap<String,String> userMap=new HashMap<>();
					userMap.put("device_token",device_token);
					userMap.put("username",display_name);
					userMap.put("tipousuario","administrador");
					//La siguiente línea hacer dos cosas: primero hace setValue y después añade un OnCompleteListener para
					//ver si la tarea de añadir un setValue se realiza correctamente. OJO: Se hace el setValue. Parece evidente
					//pero lo remarco.
					mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
						@Override
						public void onComplete(@NonNull Task<Void> task) {
							if(task.isSuccessful()){
								mRegProgress.dismiss();
								Toast.makeText(SignUpAdmin.this, "Cuenta creada correctamente.",Toast.LENGTH_SHORT).show();
								Intent mainIntent=new Intent(SignUpAdmin.this,MainActivityCarer.class);
								mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //No lo tengo muy claro pero es algo así. New_task crea un nuevo Task para la Activity que se va a abrir
								//(MainActivity) y Clear_Task elimina todas las otras tasks que estaban abiertas hasta ahora dejando la nueva task creada con New_task como la única. Con ésto consigo que activity_main pase
								//a ser la actividad arriba de la pila de la nueva task y que, por tanto, al pulsar atrás, regrese al escritorio inicial del Smartphone en lugar de al RegisterActivity.
								startActivity(mainIntent);
								finish();
							}
						}
					});
				} else {
					mRegProgress.dismiss();
					Toast.makeText(SignUpAdmin.this, "No se puede registrar. Por favor, revisa los datos.",Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

}
