package org.barrabash.powernotifier.settings;

import org.barrabash.powernotifier.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class NewContactDialog extends DialogFragment {
	public interface NewContactDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog, String input);
		public void onDialogNegativeClick(DialogFragment dialog);
	}
	
	private NewContactDialogListener _listener;
	
	public NewContactDialog(Activity parent) {
		try {
			_listener = (NewContactDialogListener) parent;
		}
		catch(ClassCastException e) {
			throw new ClassCastException(parent.toString() + 
					"must implement NewContactDialogListener");
		}
	}
	
	public NewContactDialog(Fragment parent) {
		try {
			_listener = (NewContactDialogListener) parent;
		}
		catch(ClassCastException e) {
			throw new ClassCastException(parent.toString() + 
					"must implement NewContactDialogListener");
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View ui = inflater.inflate(R.layout.email_dialog, null);
		
		final EditText input = (EditText) ui.findViewById(R.id.email_address);
				
		AlertDialog.Builder dlgBuilder = new AlertDialog.Builder
				(getActivity());
		dlgBuilder.setTitle(R.string.add_new)
		.setView(ui)
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				Log.wtf("TEST", "Launch missiles");
				_listener.onDialogPositiveClick(NewContactDialog.this,
						input.getText().toString());
			}
		})
		.setNegativeButton(android.R.string.cancel, new DialogInterface
				.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				Log.wtf("TEST", "Don't worry, it's just a joke");
				_listener.onDialogNegativeClick(NewContactDialog.this);
			}
		});
		
		return dlgBuilder.create();
			
	}
}
