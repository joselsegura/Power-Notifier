package org.barrabash.powernotifier;

import android.content.Context;
import android.preference.Preference;

public class EmailAddress extends Preference {
	private String _address;
	
	public EmailAddress(Context context, String address) {
		super(context);
		
		_address = address;
		setTitle(_address);		
	}
	
	public String getAddress() {
		return _address;
	}
	
}
