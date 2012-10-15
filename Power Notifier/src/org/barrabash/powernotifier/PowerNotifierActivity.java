package org.barrabash.powernotifier;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceActivity;


public class PowerNotifierActivity extends PreferenceActivity {
	final static String TAG = "POW_NOTIFY";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onBuildHeaders(List<Header> target) {
    	loadHeadersFromResource(R.xml.preference_headers, target);
    }
    
    
}