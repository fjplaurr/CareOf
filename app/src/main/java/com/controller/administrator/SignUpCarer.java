package com.controller.administrator;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import com.controller.pfc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.*;

public class SignUpCarer extends AppCompatActivity {
	private Toolbar mToolbar;
	private TextInputLayout usuarioCuida, emailCuida, passCuida;
	private FirebaseAuth mAuth1, mAuth2;
	private ProgressDialog mRegProgress;
	private DatabaseReference mDatabase;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_carer);

		//Inicio instancia del ProgressDialog
		mRegProgress=new ProgressDialog(this  );

		//Authentication
		mAuth1 = FirebaseAuth.getInstance();
		FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
				.setDatabaseUrl("https://pfcandroidstudio.firebaseio.com")
				.setApiKey("AIzaSyA33xzqVsHUgSfSq_wsjBWu2e75Fpt2_kI")
				.setApplicationId("1:216144674058:android:a0ed12900bd95b2f").build();
		List miListadeApps= FirebaseApp.getApps(this);
		if(miListadeApps.size()==1){
			FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(),firebaseOptions,"pfcandroidstudio");
			mAuth2 = FirebaseAuth.getInstance(myApp);
		}else {
			FirebaseApp myApp=FirebaseApp.getInstance("pfcandroidstudio");
			mAuth2 = FirebaseAuth.getInstance(myApp);
		}

		//Toolbar
		mToolbar=(Toolbar)findViewById(R.id.TBaltaCuidador);
        setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle("Registrar personal");
    }
    
    public void sePulsaRegistrar(View view){
	 	usuarioCuida=(TextInputLayout) findViewById(R.id.usuarioCuida);
	 	String usuario=usuarioCuida.getEditText().getText().toString();
		emailCuida=(TextInputLayout)findViewById(R.id.emailCuida);
		String email=emailCuida.getEditText().getText().toString();
	 	passCuida=(TextInputLayout)findViewById(R.id.passCuida);
	 	String password=passCuida.getEditText().getText().toString();

		//ProgressDialog
		mRegProgress.setTitle("Registrando cuidador");
		mRegProgress.setMessage("Espera mientras se crea la cuenta!");
		mRegProgress.setCanceledOnTouchOutside(false);   //Establece que el dialogo no pueda cerrarse si se toca fuera de la ventana.
		mRegProgress.show();

		//Registrar usuario
		register_user(usuario,email,password);
	}


	private void register_user(final String display_name, final String email, final String password) {
		mAuth2.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if (task.isSuccessful()) {
					FirebaseUser current_user= mAuth2.getCurrentUser();
					String uid=current_user.getUid();
					mDatabase=FirebaseDatabase.getInstance().getReference().child("users").child(uid);
					//Quiero almacenar un token en la BD cada vez que haglo log in. Para ello, utilizo la clase FirebaseInstanceId.
					String device_token= FirebaseInstanceId.getInstance().getToken();
					//Para almacenar datos en la Base de datos, se puede utilizar un HashMap en lugar de crear una clase.
					HashMap<String,String> userMap=new HashMap<>();
					userMap.put("device_token",device_token);
					userMap.put("username",display_name);
					userMap.put("tipousuario","cuidador");
					//La siguiente línea hacer dos cosas: primero hace setValue y después añade un OnCompleteListener para
					//ver si la tarea de añadir un setValue se realiza correctamente. OJO: Se hace el setValue. Parece evidente
					//pero lo remarco.
					mAuth2.signOut();
					mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
						@Override
						public void onComplete(@NonNull Task<Void> task) {
							if(task.isSuccessful()){
								mRegProgress.dismiss();
								Toast.makeText(SignUpCarer.this, "Cuenta creada correctamente.",Toast.LENGTH_SHORT).show();
							}
						}
					});
				} else {
					mRegProgress.dismiss();
					Toast.makeText(SignUpCarer.this, "No se puede registrar. Por favor, revisa los datos.",Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

}
