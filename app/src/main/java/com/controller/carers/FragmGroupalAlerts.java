package com.controller.carers;

import android.content.*;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.TextView;
import com.controller.pfc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.model.Group;

public class FragmGroupalAlerts extends Fragment {
    private View mMainView;
    private RecyclerView RVAlert;
    private SharedPreferences datos=null;
    private SharedPreferences.Editor myeditor=null;
    private DatabaseReference mDatabase;
    private Query query;
    public String mCurrentUser;

    public FragmGroupalAlerts() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment, reciclamos el falertaindividual
        mMainView = inflater.inflate(R.layout.fragment_falerta_individual, container, false);

        //RecyclerView
        RVAlert=(RecyclerView)mMainView.findViewById(R.id.RVfragment_falerta);
        RVAlert.setHasFixedSize(true);
        RVAlert.setLayoutManager(new LinearLayoutManager(getContext()));
        return mMainView;
    }


    public void onStart() {
        super.onStart();

        //Preferences
        datos= PreferenceManager.getDefaultSharedPreferences(getContext());
        myeditor=datos.edit();
        String usuariopreferencias=datos.getString("USERNAME","");

        //Usuario
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Firebase, sacamos los grupos asignados al cuidador
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Group");
        query=mDatabase.orderByChild("cuidador").equalTo(mCurrentUser);

        //FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter<Group,FragmGroupalAlerts.UsersViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Group, FragmGroupalAlerts.UsersViewHolder>(
                Group.class,
                R.layout.user_list,
                FragmGroupalAlerts.UsersViewHolder.class,
                query) {
            @Override protected void populateViewHolder(FragmGroupalAlerts.UsersViewHolder viewHolder, final Group grupos, int position) {
                viewHolder.setNombre(grupos.getNombre());
                final String grupo=getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent listadoAlertas=new Intent(getContext(),CarerAlerts.class);
                        listadoAlertas.putExtra("grupo",grupo);
                        startActivity(listadoAlertas);
                    }
                });
            }
        };

        //Pongo el FirebaseRecyclerAdapter al RecyclerView
        RVAlert.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public UsersViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setNombre(String nombre){
            TextView TVnombre=(TextView)mView.findViewById(R.id.TVelemento);
            TVnombre.setText("Grupo "+nombre);
        }
    }

}
