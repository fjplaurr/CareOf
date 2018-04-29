package com.controller.carers.adaptadores;

import android.support.v4.app.*;
import com.controller.carers.FragmGroupalAlerts;
import com.controller.carers.FragmIndividualAlerts;

public class AlertsFragmentAdapter extends FragmentPagerAdapter{

	public AlertsFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0://alertas a una persona concreta
			return new FragmIndividualAlerts();
		case 1://alertas grupales
			return new FragmGroupalAlerts();
		default:
			return null;
		}
	}

	@Override
	public int getCount() {
		//numero de submenus
		return 2;
	}

	public CharSequence getPageTitle(int position){
		switch (position){
			case 0:
				return "ALERTA INDIVIDUAL";
			case 1:
				return "ALERTA GRUPAL";
			default:
				return null;
		}
	}
	

}
