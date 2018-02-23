package com.controlador.cuidadores;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.View;
import android.widget.TextView;
import com.controlador.pfc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.*;
import com.modelo.DependentPerson;

/**
 * DIARIO
 * Esta clase se usa para listar las personas dependientes de la pestaña diarios
 */

public class DeleteDependentPerson extends AppCompatActivity {
	public String usuario;
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
		getSupportActionBar().setTitle("Seleccione la persona a borrar");

		//DatabaseReference
		mPersDep=FirebaseDatabase.getInstance().getReference().child("DependentPerson");


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
				usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						final AlertDialog.Builder builder=new AlertDialog.Builder(DeleteDependentPerson.this);
						builder.setTitle("Borrar persona dependiente");
						builder.setMessage("¿Está seguro de eliminar a "+pDep.getNombre()+" ?");
						builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								//se saca el identificador del familiar
								final DatabaseReference refPersBorrar=FirebaseDatabase.getInstance().getReference("DependentPerson").child(pDep.getFamiliar());
								refPersBorrar.removeValue();
								final DatabaseReference refUserBorrar=FirebaseDatabase.getInstance().getReference("users").child(pDep.getFamiliar());
								refUserBorrar.removeValue();
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