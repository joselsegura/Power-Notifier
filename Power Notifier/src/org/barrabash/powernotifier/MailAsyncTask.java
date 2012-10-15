package org.barrabash.powernotifier;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class MailAsyncTask extends AsyncTask<String, Integer, Boolean> {
	private Context context_;
	
	public MailAsyncTask(Context context) {
		context_ = context;
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		assert params.length > 1;
		
		MailSender m = new MailSender("fatima.nieto.sanchez@gmail.com ", "pastelillo");
		m.setFrom("fatima.nieto.sanchez@gmail.com");
		m.setTo(params);
		m.setSubject("Testing: going off");
		m.setBody("The AC is going to OFF");

		try {
			m.send();
			return Boolean.valueOf(true);
		} catch (AddressException e) {
			Log.wtf(PowerNotifierActivity.TAG, "Error: Addressing error");
			return Boolean.valueOf(false);
		} catch (MessagingException e) {
			Log.wtf(PowerNotifierActivity.TAG, "Error: Messaging error: " + e.getMessage());
			return Boolean.valueOf(false);
		}
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		String text;
		if (result.booleanValue())
			text = "Notifications sent";
		
		else
			text = "Notifications failure";
		
		if (context_ != null)
			Toast.makeText(context_, text, Toast.LENGTH_LONG).show();
	
		Log.i(PowerNotifierActivity.TAG, text);
	}

}
