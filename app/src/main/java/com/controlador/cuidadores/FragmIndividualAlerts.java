package com.controlador.cuidadores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.*;
import android.view.LayoutInflater;
import android.view.*;
import android.widget.TextView;
import com.controlador.pfc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.modelo.DependentPerson;

public class FragmIndividualAlerts extends Fragment {
    private View mMainView;
    public String mCurrentUser;
    private RecyclerView RVgenerico;
    private DatabaseReference mPersDep;
    private LinearLayoutManager mLayoutManager;
    private Query query;

    public FragmIndividualAlerts() {}// Required empty public constructor

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_falerta_individual, container, false);

        //RecyclerView
        RVgenerico=(RecyclerView)mMainView.findViewById(R.id.RVfragment_falerta);

        //Layout Manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        RVgenerico.setHasFixedSize(true);
        RVgenerico.setLayoutManager(mLayoutManager);

        //Usuario
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser().getUid();

        //DatabaseReference
        mPersDep=FirebaseDatabase.getInstance().getReference().child("DependentPerson");
        query=mPersDep.orderByChild("cuidador").equalTo(mCurrentUser);

        FirebaseRecyclerAdapter<DependentPerson,FragmIndividualAlerts.UsersViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<DependentPerson, FragmIndividualAlerts.UsersViewHolder>(
                DependentPerson.class,
                R.layout.unelemento_list,
                FragmIndividualAlerts.UsersViewHolder.class,
                query) {
            @Override
            protected void populateViewHolder(FragmIndividualAlerts.UsersViewHolder viewHolder,
                                              final DependentPerson pDep, int position) {
                viewHolder.setNombre(pDep.getNombre());
                final String id_familiar=getRef(position).getKey();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent vdiarioIntent=new Intent(getContext(),CarerAlerts.class);
                        vdiarioIntent.putExtra("id_familiar",id_familiar);
                        vdiarioIntent.putExtra("nombrePdependiente",pDep.getNombre());
                        startActivity(vdiarioIntent);
                    }
                });
            }

        };

        //4.Adapter
        RVgenerico.setAdapter(firebaseRecyclerAdapter);

        return mMainView;
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
