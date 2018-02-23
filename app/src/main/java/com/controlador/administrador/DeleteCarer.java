package com.controlador.administrador;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.controlador.pfc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.modelo.User;

/**
 * DIARIO
 * Esta clase se usa para listar las personas dependientes de la pestaña diarios
 */

public class DeleteCarer extends AppCompatActivity {
	public String usuario;
	private RecyclerView RVgenerico;
	private DatabaseReference mCuidador;
	private android.support.v7.widget.Toolbar mToolbar;
	private Query mQuery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vlistado_generico);
		//Toolbar
		mToolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.TBgenerico);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle("Seleccione el cuidador a borrar");

		//DatabaseReference
		mCuidador=FirebaseDatabase.getInstance().getReference().child("users");
		mQuery=mCuidador.orderByChild("tipousuario").equalTo("cuidador");

		//RecyclerView
		RVgenerico=(RecyclerView)findViewById(R.id.RVgenerico);
		RVgenerico.setHasFixedSize(true);
		RVgenerico.setLayoutManager(new LinearLayoutManager(this));
	}

	public void onStart() {
		super.onStart();
		FirebaseRecyclerAdapter<User,DeleteCarer.UsersViewHolder> firebaseRecyclerAdapter
				= new FirebaseRecyclerAdapter<User, DeleteCarer.UsersViewHolder>(
				User.class,
				R.layout.unelemento_list,
				DeleteCarer.UsersViewHolder.class,
				mQuery) {
			@Override
			public void populateViewHolder(DeleteCarer.UsersViewHolder usersViewHolder,
										   final User miUser, int position) {
				usersViewHolder.setNombre(miUser.getUsername());
				final String id_cuidador=getRef(position).getKey();
				usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						final AlertDialog.Builder builder=new AlertDialog.Builder(DeleteCarer.this);
						builder.setTitle("Borrar cuidador");
						builder.setMessage("¿Está seguro de eliminar a "+miUser.getUsername()+" ?");
						builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								//se saca el id del cuidador
								final DatabaseReference refCuidadorBorrar=FirebaseDatabase.getInstance().
										getReference("users").child(id_cuidador);
								refCuidadorBorrar.removeValue();
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