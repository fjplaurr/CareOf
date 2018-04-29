package com.controller.administrator;

import com.controller.pfc.R;
import com.controller.Login;
import com.google.firebase.auth.FirebaseAuth;
import android.content.*;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;

public class MainActivityAdmin extends AppCompatActivity {
	private Toolbar mToolbar;
	private FirebaseAuth mAuth1;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_administrator);

		//Toolbar
		mToolbar=(Toolbar)findViewById(R.id.TBadministrador);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);

		//Authentication
		mAuth1 = FirebaseAuth.getInstance();
    }
    
    public void sePulsaRegistrar(View view){
		Intent intent = new Intent(this, SignUpCarer.class);
		startActivity(intent);
	}

	public void sePulsarEliminar(View view){
		Intent intent = new Intent(this, DeleteCarer.class);
		startActivity(intent);
	}

	public void crearGrupo(View view){// creamos un nuevo grupo en la base de datos
		Intent intent = new Intent(this, CreateGroup.class);
		startActivity(intent);
	}

	public void borrarGrupo(View view){// eliminamos un grupo de la base de datos
		Intent intent = new Intent(this, DeleteGroup.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.menu_principal_administrador, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
			case R.id.btnMenuCerrarSesion:
				cerrar_sesion();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
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
