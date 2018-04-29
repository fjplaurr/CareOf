package com.controller.carers;

import com.controller.pfc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
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
import android.widget.*;
import java.util.*;

public class SignUpDependentPerson extends AppCompatActivity {
	private TextInputLayout nombrePerDepend, nombreF, passF, emailFamiliar;
	private String nombrePerDependiente, grupoPerDepend,nombreFamiliar,passwordFamiliar, email;
	private FirebaseAuth mAuth1, mAuth2;
	private DatabaseReference mRoot, mDatabase, mDatabase2,fDatabaseRoot;
	private ProgressDialog mRegProgress;
	private Button boton;
	private String usuarioConectado;
	private Toolbar mToolbar;
	private Spinner miSpinner;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_dependent_person);
		//Toolbar
		mToolbar=(Toolbar)findViewById(R.id.TBreg_per_dep);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle("Registrar persona dependiente");

		//Layout
		nombrePerDepend=(TextInputLayout) findViewById(R.id.nombrePerDepend);
		nombreF=(TextInputLayout) findViewById(R.id.nombreFamiliar);
		passF=(TextInputLayout) findViewById(R.id.passwordUsuarioFamiliar);
		emailFamiliar=(TextInputLayout)findViewById(R.id.emailFamiliar);
		boton=(Button)findViewById(R.id.btnRegistrarCuidador);

		//Inicio instancia del ProgressDialog
		mRegProgress=new ProgressDialog(this  );

		//Authentication
		mAuth1 = FirebaseAuth.getInstance();
		usuarioConectado=mAuth1.getCurrentUser().getUid();

		//DatabaseReference
		fDatabaseRoot=FirebaseDatabase.getInstance().getReference();

		//Spinner
		fDatabaseRoot.child("Group").orderByChild("nombre").addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				final List<String> listGrupos=new ArrayList<String>();
				for(DataSnapshot gruposSnapShot: dataSnapshot.getChildren()){
					String pruebaNombre=gruposSnapShot.child("nombre").getValue().toString();
					listGrupos.add(pruebaNombre);
					miSpinner=(Spinner)findViewById(R.id.SPseleccionar_grupo);
					ArrayAdapter<String> miAdapter=new ArrayAdapter<String>(SignUpDependentPerson.this,
							android.R.layout.simple_spinner_item,listGrupos);
					miAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //Vista del Spinner
					miSpinner.setAdapter(miAdapter);
				}
			}
			@Override public void onCancelled(DatabaseError databaseError) {}
		});


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

		boton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				sepulsaagregar();
			}
		});
	}

	public void sepulsaagregar(){
		nombrePerDependiente=nombrePerDepend.getEditText().getText().toString();
		//grupoPerDepend=grupo.getEditText().getText().toString();
		nombreFamiliar=nombreF.getEditText().getText().toString();
		passwordFamiliar=passF.getEditText().getText().toString();
		email=emailFamiliar.getEditText().getText().toString();
		grupoPerDepend=miSpinner.getSelectedItem().toString();

		//ProgressDialog
		mRegProgress.setTitle("Registrando persona dependiente");
		mRegProgress.setMessage("Espera mientras se crea la cuenta!");
		mRegProgress.setCanceledOnTouchOutside(false);   //Establece que el dialogo no pueda cerrarse si se toca fuera de la ventana.
		mRegProgress.show();

		register_user(nombreFamiliar,email,passwordFamiliar,nombrePerDependiente,grupoPerDepend);
	}

	private void register_user(final String nombFam, final String email, final String password, final String nombreDep,final String grupo) {
		mAuth2.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if (task.isSuccessful()) {
					final FirebaseUser current_user= mAuth2.getCurrentUser();
					final String uid=current_user.getUid();
					mRoot=FirebaseDatabase.getInstance().getReference();
					mDatabase=mRoot.child("users").child(uid);
					mDatabase2=mRoot.child("DependentPerson").child(uid);
					//Quiero almacenar un token en la BD cada vez que haglo log in. Para ello, utilizo la clase FirebaseInstanceId.
					String device_token= FirebaseInstanceId.getInstance().getToken();
					//Para almacenar datos en la Base de datos, se puede utilizar un HashMap en lugar de crear una clase.
					HashMap<String,String> userMap=new HashMap<>();
					userMap.put("device_token",device_token);
					userMap.put("username",nombFam);
					userMap.put("tipousuario","familiar");
					//La siguiente línea hacer dos cosas: primero hace setValue y después añade un OnCompleteListener para
					//ver si la tarea de añadir un setValue se realiza correctamente. OJO: Se hace el setValue. Parece evidente
					//pero lo remarco.
					mAuth2.signOut();
					mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
						@Override
						public void onComplete(@NonNull Task<Void> task) {
							if(task.isSuccessful()){
								HashMap<String,String>userMap2=new HashMap<>();
								userMap2.put("grupo",grupo);
								userMap2.put("nombre",nombreDep);
								userMap2.put("cuidador",usuarioConectado);
								userMap2.put("familiar",uid);
								mDatabase2.setValue(userMap2).addOnCompleteListener(new OnCompleteListener<Void>() {
									@Override
									public void onComplete(@NonNull Task<Void> task) {
										if(task.isSuccessful()){
											mRegProgress.dismiss();
											Toast.makeText(SignUpDependentPerson.this, "Cuenta creada correctamente.",Toast.LENGTH_SHORT).show();
											Intent mainIntent=new Intent(SignUpDependentPerson.this,MainActivityCarers.class);
											mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //No lo tengo muy claro pero es algo así. New_task crea un nuevo Task para la Activity que se va a abrir
											//(MainActivity) y Clear_Task elimina todas las otras tasks que estaban abiertas hasta ahora dejando la nueva task creada con New_task como la única. Con ésto consigo que activity_main pase
											//a ser la actividad arriba de la pila de la nueva task y que, por tanto, al pulsar atrás, regrese al escritorio inicial del Smartphone en lugar de al RegisterActivity.
											startActivity(mainIntent);
											finish();
										}
										else {
											Toast.makeText(SignUpDependentPerson.this, "Ha habido un problema y la cuenta no ha podido ser creada.",Toast.LENGTH_SHORT).show();
										}
									}
								});
							}else{
								Toast.makeText(SignUpDependentPerson.this, "Ha habido un problema y la cuenta no ha podido ser creada.",Toast.LENGTH_SHORT).show();
							}
						}
					});
				} else {
					mRegProgress.dismiss();
					Toast.makeText(SignUpDependentPerson.this, "No se puede registrar. Por favor, revisa los datos.",Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

}
