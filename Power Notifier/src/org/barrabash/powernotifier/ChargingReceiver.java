package org.barrabash.powernotifier;

import java.util.HashSet;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class ChargingReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context ctx, Intent intent) {
		String action = intent.getAction();
		
		Log.wtf(PowerNotifierActivity.TAG, action);
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
		Set<String> email_addresses = sharedPref.getStringSet("email_list", new HashSet<String>());
		String[] array = email_addresses.toArray(new String[0]);
		
		new MailAsyncTask(ctx).execute(array);
	}
}
 