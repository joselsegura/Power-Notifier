package org.barrabash.powernotifier.settings;

import org.barrabash.powernotifier.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;

public class SMSFragment extends SwitchedFragment {
	public SMSFragment() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		addPreferencesFromResource(R.xml.preferences_sms);
        
        addSwitchToActionBar();
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(getActivity()).edit();
		
		editor.putBoolean("send_email", isChecked);
		editor.apply();
	}
}
