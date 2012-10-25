package org.barrabash.powernotifier;

import java.util.HashSet;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class ChargingReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context ctx, Intent intent) {
		String action = intent.getAction();
		
		Log.wtf(PowerNotifierActivity.TAG, action);
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
		String[] array;
		boolean sent = false;
		
		if(sharedPref.getBoolean("send_email", false)) {
			Set<String> email_addresses = sharedPref.getStringSet("email_list", new HashSet<String>());
			if(email_addresses.size() > 0) {
				array = email_addresses.toArray(new String[0]);
			
				new MailAsyncTask(ctx).execute(array);
				sent = true;
			}
		}
		
		if(sharedPref.getBoolean("send_sms", false)) {
			Set<String> sms_phones = sharedPref.getStringSet("sms_list", new HashSet<String>());
			if(sms_phones.size() > 0) {
				SmsManager smsMg = SmsManager.getDefault();
				
				for(String phone : sms_phones)
					smsMg.sendTextMessage(phone, null, "TEST", null, null);
					
				sent = true;
			}
			
		}
		
		if (sent)
			Toast.makeText(ctx, ctx.getString(R.string.sent),  Toast.LENGTH_SHORT).show();
		
	}
}
 