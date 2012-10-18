package org.barrabash.powernotifier;

import java.util.HashSet;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class ChargingReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context ctx, Intent intent) {
		String action = intent.getAction();
		
		Log.wtf(PowerNotifierActivity.TAG, action);
		
		;SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
		String[] array;
		
		if(sharedPref.getBoolean("send_email", false)) {
			Set<String> email_addresses = sharedPref.getStringSet("email_list", new HashSet<String>());
			array = email_addresses.toArray(new String[0]);
			new MailAsyncTask(ctx).execute(array);
			Toast.makeText(ctx,  "E-mail",  Toast.LENGTH_SHORT).show();
		}
		
		if(sharedPref.getBoolean("send_sms", false)) {
			Set<String> sms_phones = sharedPref.getStringSet("sms_list", new HashSet<String>());
			array = sms_phones.toArray(new String[0]);
			// send SMS
			Toast.makeText(ctx,  "SMS",  Toast.LENGTH_SHORT).show();
		}
		
		
	}
}
 