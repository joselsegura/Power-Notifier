package org.barrabash.powernotifier.settings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.barrabash.powernotifier.R;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CompoundButton;

public class EmailFragment extends SwitchedFragment
	implements NewContactDialog.NewContactDialogListener{
	
	private static final String TAG = "EmailFragment";
	private static final int MENU_ID_ADD_ADDRESS = Menu.FIRST;
	private List<EditTextPreference> _addresses = 
			new ArrayList <EditTextPreference>();
	
	private Preference.OnPreferenceChangeListener _emailEdited = 
			new Preference.OnPreferenceChangeListener() {
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			String t = ((EditTextPreference) preference).getText();
			preference.setTitle((String) newValue);
			
			SharedPreferences shPrefs = PreferenceManager.
					getDefaultSharedPreferences(getActivity());
			Set<String> email_list = shPrefs.getStringSet("email_list",
					new HashSet<String>());
			
			email_list.remove(t);
			
			email_list.add((String) newValue);
			
			SharedPreferences.Editor editor = shPrefs.edit();
			editor.putStringSet("email_list", email_list);
			editor.apply();
						
			return true;
		}
	};
	
		
	public EmailFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences_email);
		setHasOptionsMenu(true);
		addSwitchToActionBar();
		
		SharedPreferences sharedPrefs = PreferenceManager.
				getDefaultSharedPreferences(getActivity());
		
		_headerSwitch.setChecked(sharedPrefs.getBoolean("send_email", false));
		_headerSwitch.invalidate();
		
		/* Entering the data on preferences */
		Set<String> addresses = sharedPrefs.getStringSet("email_list",
				new HashSet<String>());
		
		for (String address : addresses) {
			EditTextPreference email = new EditTextPreference(getActivity());
			email.setOnPreferenceChangeListener(_emailEdited);
			email.setText(address);
			email.setTitle(address);
			email.setDialogTitle(R.string.edit);
			email.getEditText().setInputType(
					InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			_addresses.add(email);
			
			getPreferenceScreen().addPreference(email);
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		registerForContextMenu(getListView());
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add(Menu.NONE, MENU_ID_ADD_ADDRESS, 0, R.string.add_new)
		.setIcon(android.R.drawable.ic_menu_add)
		// FIXME: try to connect it to the header switch callback
		.setEnabled(true)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    
	    Log.d(TAG, "Creating the context");
	    
	    MenuInflater inflater = getActivity().getMenuInflater();
	    inflater.inflate(R.menu.email_context_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case MENU_ID_ADD_ADDRESS:
			SharedPreferences sharedPrefs = PreferenceManager.
				getDefaultSharedPreferences(getActivity());
			
			if(sharedPrefs.getBoolean("send_email", false)) {
				NewContactDialog d = new NewContactDialog(this);
				d.show(getActivity().getFragmentManager(), "AddEmailDialog");
			}
			
			break;

		default:
			Log.w(TAG, "Something extrange happened:");
			Log.w(TAG, "\tSelected unexpected menu item on" +
					" EmailFragment menu");
		}

		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Log.d(TAG, "Context item selected, aka, throw the bomb");
		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		
		switch(item.getItemId()) {
		case R.id.delete:
			Log.d(TAG, "Delete this item, please");
			EditTextPreference toDel = _addresses.get(info.position);
			getPreferenceScreen().removePreference(toDel);			
			_addresses.remove(info.position);
			
			SharedPreferences shPrefs = PreferenceManager.
					getDefaultSharedPreferences(getActivity());
					
			Set<String> set = shPrefs.getStringSet("email_list",
					new HashSet<String>());
			set.remove(toDel.getText());
			
			
			SharedPreferences.Editor editor = PreferenceManager.
					getDefaultSharedPreferences(getActivity()).edit();
			editor.putStringSet("email_list", set);
			editor.apply();
			
			break;
			
		default:
			Log.w(TAG, "Something extrange happened:");
			Log.w(TAG, "\tSelected unexpected menu item on" +
					" EmailFragment context menu");
		}
		
		return false;
	}
	
	public void onCheckedChanged(CompoundButton buttonView,
			boolean isChecked) {
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(getActivity()).edit();
		
		editor.putBoolean("send_email", isChecked);
		editor.apply();
		
		buttonView.invalidate();
	}

	public void onDialogPositiveClick(DialogFragment dialog, String input_value) {
		Log.wtf(TAG, "I'm not kidding, I will launch the missiles now! " + input_value);

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity()); 
				
		Set<String> addresses = sharedPrefs.getStringSet("email_list", new HashSet<String>());
		
		addresses.add(input_value);
		
		/* Add to shared preferences */
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putStringSet("email_list", addresses);
		editor.apply();
		
		/* Adding to the UI */
		EditTextPreference email = new EditTextPreference(getActivity());
		email.setText(input_value);
		email.setTitle(input_value);
		email.setDialogTitle(R.string.edit);
		email.getEditText().setInputType(
				InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		
		_addresses.add(email);
		
		getPreferenceScreen().addPreference(email);
		
	}

	public void onDialogNegativeClick(DialogFragment dialog) {
		Log.wtf(TAG, "Don't be afroid, my friends. The war is over");
		
	}

}

