package com.controlador;

import com.controlador.pfc.R;
import com.controlador.administrador.*;
import com.controlador.cuidadores.MainCarers;
import com.controlador.familiares.MainMentor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import com.google.firebase.iid.FirebaseInstanceId;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

public class Login extends AppCompatActivity {
	private TextInputLayout emailtext,passtext;
	/*private SharedPreferences datos=null;
	private SharedPreferences.Editor myeditor=null;*/
	private String email,password;
	private Button botonLogin, botonRegistCentro;
	private android.support.v7.widget.Toolbar mToolbar;
	private FirebaseAuth mAuth;
	private ProgressDialog mLoginProgress;
	private DatabaseReference mUserDatabase, mUserRef;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		datos= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		myeditor=datos.edit();
		String usuariopreferencias=datos.getString("USERNAME","");
		String tipo=datos.getString("tipoUsuario","");
		*/

		mAuth = FirebaseAuth.getInstance();
		if(mAuth.getCurrentUser()!=null){
			mUserRef= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
			mUserRef.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					String tipoUsuario=dataSnapshot.child("tipousuario").getValue().toString();
					empiezaActividadSegunUsuario(tipoUsuario);
				}
				@Override public void onCancelled(DatabaseError databaseError) {}
			});

		}else{
			setContentView(R.layout.vlogin);

			//ProgressDialog
			mLoginProgress=new ProgressDialog(this);

			//Firebase
			mAuth=FirebaseAuth.getInstance();
			mUserDatabase= FirebaseDatabase.getInstance().getReference().child("users");

			//Toolbar
			mToolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.TBVlogin);
			setSupportActionBar(mToolbar);
			getSupportActionBar().setIcon(R.drawable.ic_launcher);

			//Layout
			passtext= (TextInputLayout) findViewById(R.id.contra_vlogin);
			emailtext= (TextInputLayout) findViewById(R.id.email_vlogin);
			botonLogin=(Button)findViewById(R.id.btnRegistrarCuidador);
			botonRegistCentro=(Button)findViewById(R.id.btnRegistrarCentro);

			botonLogin.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					email = emailtext.getEditText().getText().toString();
					password=passtext.getEditText().getText().toString();
					if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
						//ProgressDialog
						mLoginProgress.setTitle("Accediendo");
						mLoginProgress.setMessage("Espera mientras revisamos tus credenciales.");
						mLoginProgress.setCanceledOnTouchOutside(false);

						loginUser(email,password);
					}
				}
			});

			botonRegistCentro.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(Login.this, SignUpAdmin.class);
					startActivity(intent);
				}
			});
		}
    }

	public void empiezaActividadSegunUsuario(String tipoUsuario){
		Intent intent=null;
		switch (tipoUsuario){
			case "administrador":
				intent = new Intent(Login.this, MainActivityCarer.class);
				break;
			case "cuidador":
				intent = new Intent(Login.this, MainCarers.class);
				break;
			case "familiar":
				intent = new Intent(Login.this, MainMentor.class);
				break;
		}
		startActivity(intent);
		finish();
	}

	private void loginUser(final String email, final String password) {
		mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if(task.isSuccessful()){
					//ProgressDialog
					mLoginProgress.dismiss();

					final String current_user_id=mAuth.getCurrentUser().getUid();
					String deviceToken= FirebaseInstanceId.getInstance().getToken(); //Quiero almacenar un token en la BD cada vez que haglo log in.
					mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnCompleteListener(new OnCompleteListener<Void>() {
						@Override
						public void onComplete(@NonNull Task<Void> task) {
							if(task.isSuccessful()){
								mUserDatabase.child(current_user_id).child("tipousuario").addValueEventListener(new ValueEventListener() {
									@Override
									public void onDataChange(DataSnapshot dataSnapshot) {
										empiezaActividadSegunUsuario(dataSnapshot.getValue().toString());
									}
									@Override public void onCancelled(DatabaseError databaseError) {}
								});
							}
						}
					});

				}else{
					String error = "";
					try {
						throw task.getException();
					} catch (FirebaseAuthInvalidUserException e) {
						error = "Email inválido.";
					} catch (FirebaseAuthInvalidCredentialsException e) {
						error = "Contraseña inválida.";
					} catch (Exception e) {
						error = "Ha ocurrido un error.";
						e.printStackTrace();
					}
					mLoginProgress.hide();
					Toast.makeText(Login.this, error, Toast.LENGTH_LONG).show();
				}
			}
		});
	}

}
