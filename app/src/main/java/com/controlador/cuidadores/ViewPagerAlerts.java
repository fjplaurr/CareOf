package com.controlador.cuidadores;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.controlador.pfc.R;
import com.controlador.cuidadores.adaptadores.AlertsFragmentAdapter;

public class ViewPagerAlerts extends AppCompatActivity {
    private AlertsFragmentAdapter mSectionsPagerAdapter;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valertas_cuidadores);

        //Toolbar
        mToolbar=(Toolbar) findViewById(R.id.TBAlertas_cuidadores);
        setSupportActionBar(mToolbar);

        //ViewPager y adaptador
        mViewPager=(ViewPager)findViewById(R.id.VPalertas_cuid);
        mSectionsPagerAdapter=new AlertsFragmentAdapter(getSupportFragmentManager());   //La clase adaptadora FragmentPagerAdapter sólo tiene un constructor que admite
                //un FragmentManager como argumento. Lo obtengo mediante getSupportFragmentManager() de la clase Fragment Activity que devuelve un
                //Fragment Manager para interactura con fragments asociados con esta actividad.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout=(TabLayout)findViewById(R.id.TLalertas_cuidadores);
        mTabLayout.setupWithViewPager(mViewPager);  //Establece un link entre el TabLayout y el ViewPager para que el propio TabLayout permita cambiar de un Fragment a otro.
    }

}
