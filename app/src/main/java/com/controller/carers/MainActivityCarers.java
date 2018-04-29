package com.controller.carers;

import com.controller.pfc.R;
import android.content.*;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import com.controller.Login;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import android.view.View;

public class MainActivityCarers extends AppCompatActivity {
	private Toolbar mToolbar;
	private String usuario;
	private DatabaseReference mRootRef;
	private FirebaseAuth mAuth;
	private FirebaseUser current_user;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_carers);

		//Llamada a la BD
		mRootRef= FirebaseDatabase.getInstance().getReference();
		mAuth= FirebaseAuth.getInstance();
		current_user=mAuth.getCurrentUser();

		SharedPreferences datos= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		usuario=datos.getString("USERNAME",""); //El segundo valor es para ver cuánto tendrá si no encuentra la clave.

		//Toolbar
		mToolbar=(Toolbar)findViewById(R.id.TBcuidador);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
	}

	
	 @Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_principal_cuidador, menu);
		return true;
	}
	 
	 @Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case R.id.agregar_persona_dependiente:
			lanzar_agregar_persona_dependiente();
			return true;
		case R.id.borrar_persona_dependiente:
			lanzar_borrar_persona_dependiente();
			return true;
		case R.id.cerrar_sesion:
			cerrar_sesion();
			return true;
		/*case R.id.diario_persona_dependiente:
			lanzar_diario_persona_dependiente();
			return true;
		case R.id.alertas:
			lanzar_alertas();
			return true;
		case R.id.mensajes:
			lanzar_mensajes();
			return true;*/
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void lanzar_agregar_persona_dependiente(){
		Intent i= new Intent(this, SignUpDependentPerson.class);
		startActivity(i);
	}

	public void lanzar_borrar_persona_dependiente(){
		Intent i= new Intent(this, DeleteDependentPerson.class);


		startActivity(i);
	}

	 public void lanzar_diario_persona_dependiente(View view){
		 Intent i= new Intent(this, ListDependentPeople.class);
		 i.putExtra("nombre",usuario);
		 i.putExtra("aplicacion","diario");
		 startActivity(i);
	 }

	 public void lanzar_mensajes(View view){
	 	Intent i= new Intent(this, ListDependentPeople.class);
		 i.putExtra("nombre",usuario);
		 i.putExtra("aplicacion","chat");
		 startActivity(i);
	 }

	 public void lanzar_alertas(View view){
		 Intent i= new Intent(this, ViewPagerAlerts.class);
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
