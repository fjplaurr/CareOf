package com.controller.mentors;

import com.controller.pfc.R;
import com.controller.Chat;
import com.controller.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;

public class MainActivityMentor extends AppCompatActivity {
	private Toolbar mToolbar;
	private DatabaseReference mRootRef;
	private FirebaseAuth mAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_mentor);

		//Llamada a la BD
		mRootRef= FirebaseDatabase.getInstance().getReference();
		mAuth= FirebaseAuth.getInstance();

		//Toolbar
		mToolbar=(Toolbar)findViewById(R.id.TBfamiliar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);

	}
	
	//MENU
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.menu_principal_familiar, menu);
		return true;
	}
	 
	 @Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {

			case R.id.CerrarSesi:
				cerrar_sesion();
				return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	 
	//lanzadores de actividades
	 public void lanzar_diario_persona_dependiente(View view){
		 Intent i= new Intent(this, DiaryMentor.class);
		 startActivity(i);
	 }

	 public void lanzar_mensajes(View view){
		 Intent i= new Intent(this, Chat.class);
		 //SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		 //String nombrePDependiente = datos.getString("pDependiente", "");
		 //i.putExtra("nombrePDependiente",nombrePDependiente);
		 i.putExtra("id_familiar",mAuth.getCurrentUser().getUid());
		 startActivity(i);
	 }

	 public void lanzar_alertas(View view){
	 	Intent i= new Intent(MainActivityMentor.this, AlertsMentor.class);
	 	startActivity(i);
	 }

	public void cerrar_sesion(){
		FirebaseAuth.getInstance().signOut();
		SharedPreferences datos=null;
		SharedPreferences.Editor myeditor=null;
		datos= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		myeditor=datos.edit();
		myeditor.putString("USERNAME","");
		myeditor.putString("tipoUsuario","");
		myeditor.apply();
		Intent intent = new Intent(this, Login.class);
		startActivity(intent);
		finish();
	}

}
