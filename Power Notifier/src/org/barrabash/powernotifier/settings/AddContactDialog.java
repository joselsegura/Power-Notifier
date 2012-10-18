package org.barrabash.powernotifier.settings;

import org.barrabash.powernotifier.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class AddContactDialog extends DialogFragment {
	public static final int TYPE_UNKNOWN = 0;
	public static final int TYPE_EMAIL = 1;
	public static final int TYPE_PHONE = 2;
	
	public interface AddContactDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog, String input);
		public void onDialogNegativeClick(DialogFragment dialog);
	}
	
	private AddContactDialogListener _listener;
	private int _type;
		
	public AddContactDialog(Activity parent, int type) {
		try {
			_listener = (AddContactDialogListener) parent;
		}
		catch(ClassCastException e) {
			throw new ClassCastException(parent.toString() + 
					"must implement NewContactDialogListener");
		}
		
		switch(type) {
		case TYPE_EMAIL:
		case TYPE_PHONE:
			_type = type;
			break;
			
		default:
			Log.w(getString(R.string.app_name),
					"Unknown type. Using default EditText");
			_type = TYPE_UNKNOWN;
			
		} 
	}
	
	public AddContactDialog(Fragment parent, int type) {
		try {
			_listener = (AddContactDialogListener) parent;
		}
		catch(ClassCastException e) {
			throw new ClassCastException(parent.toString() + 
					"must implement NewContactDialogListener");
		}
		
		switch(type) {
		case TYPE_EMAIL:
		case TYPE_PHONE:
			_type = type;
			break;
			
		default:
			Log.w(getString(R.string.app_name),
					"Unknown type. Using default EditText");
			_type = TYPE_UNKNOWN;
			
		} 
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View ui = inflater.inflate(R.layout.add_contact_dialog, null);
		
		final EditText input = (EditText) ui.findViewById(R.id.contact_info);
		
		/* Set EditText params by type */
		switch (_type) {
		case TYPE_EMAIL:
			input.setHint(R.string.email_address);
			input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			break;
			
		case TYPE_PHONE:
			input.setHint(R.string.sms_phone);
			input.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		}
				
		AlertDialog.Builder dlgBuilder = new AlertDialog.Builder
				(getActivity());
		dlgBuilder.setTitle(R.string.add_new)
		.setView(ui)
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				Log.wtf("TEST", "Launch missiles");
				_listener.onDialogPositiveClick(AddContactDialog.this,
						input.getText().toString());
			}
		})
		.setNegativeButton(android.R.string.cancel, new DialogInterface
				.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				Log.wtf("TEST", "Don't worry, it's just a joke");
				_listener.onDialogNegativeClick(AddContactDialog.this);
			}
		});
		
		return dlgBuilder.create();
			
	}
}
