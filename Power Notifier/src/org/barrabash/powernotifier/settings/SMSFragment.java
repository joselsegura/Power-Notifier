package org.barrabash.powernotifier.settings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.barrabash.powernotifier.R;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.EditTextPreference;
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

public class SMSFragment extends SwitchedFragment 
	implements AddContactDialog.AddContactDialogListener {
	
	private static final int MENU_ID_ADD_PHONE = Menu.FIRST;
	private List<EditTextPreference> _phones = 
			new ArrayList <EditTextPreference>();
	
	private Preference.OnPreferenceChangeListener _phoneEdited = 
			new Preference.OnPreferenceChangeListener() {
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			String t = ((EditTextPreference) preference).getText();
			preference.setTitle((String) newValue);
			
			SharedPreferences shPrefs = PreferenceManager.
					getDefaultSharedPreferences(getActivity());
			Set<String> sms_list = shPrefs.getStringSet("sms_list",
					new HashSet<String>());
			
			sms_list.remove(t);
			
			sms_list.add((String) newValue);
			
			SharedPreferences.Editor editor = shPrefs.edit();
			editor.putStringSet("sms_list", sms_list);
			editor.apply();
						
			return true;
		}
	};
	
	public SMSFragment() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		
		addPreferencesFromResource(R.xml.preferences_sms);
		setHasOptionsMenu(true);
        addSwitchToActionBar();
        
        SharedPreferences sharedPrefs = PreferenceManager.
				getDefaultSharedPreferences(getActivity());
        
        _headerSwitch.setChecked(sharedPrefs.getBoolean("send_sms", false));
		_headerSwitch.invalidate();
		
		/* Entering the data on preferences */
		Set<String> addresses = sharedPrefs.getStringSet("sms_list",
				new HashSet<String>());
		
		for (String address : addresses) {
			EditTextPreference phone = new EditTextPreference(getActivity());
			phone.setOnPreferenceChangeListener(_phoneEdited);
			phone.setText(address);
			phone.setTitle(address);
			phone.setDialogTitle(R.string.edit);
			phone.getEditText().setInputType(
					InputType.TYPE_CLASS_PHONE);
			_phones.add(phone);
			
			getPreferenceScreen().addPreference(phone);
		}
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		registerForContextMenu(getListView());
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add(Menu.NONE, MENU_ID_ADD_PHONE, 0, R.string.add_new)
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
	    
	    Log.d(getString(R.string.app_name), "Creating the context");
	    
	    MenuInflater inflater = getActivity().getMenuInflater();
	    inflater.inflate(R.menu.list_context_menu, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case MENU_ID_ADD_PHONE:
			SharedPreferences sharedPrefs = PreferenceManager.
				getDefaultSharedPreferences(getActivity());
			
			if(sharedPrefs.getBoolean("send_sms", false)) {
				AddContactDialog d = new AddContactDialog(this,
						AddContactDialog.TYPE_PHONE);
				
				d.show(getActivity().getFragmentManager(), "AddSMSDialog");
			}
			
			break;

		default:
			Log.w(getString(R.string.app_name),
					"Something extrange happened:");
			Log.w(getString(R.string.app_name),
					"\tSelected unexpected menu item on" +
					" SMSFragment menu");
		}

		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Log.d(getString(R.string.app_name),
				"Context item selected, aka, throw the bomb");
		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		
		switch(item.getItemId()) {
		case R.id.delete:
			Log.d(getString(R.string.app_name), "Delete this item, please");
			EditTextPreference toDel = _phones.get(info.position);
			getPreferenceScreen().removePreference(toDel);			
			_phones.remove(info.position);
			
			SharedPreferences shPrefs = PreferenceManager.
					getDefaultSharedPreferences(getActivity());
					
			Set<String> set = shPrefs.getStringSet("sms_list",
					new HashSet<String>());
			set.remove(toDel.getText());
			
			
			SharedPreferences.Editor editor = PreferenceManager.
					getDefaultSharedPreferences(getActivity()).edit();
			editor.putStringSet("sms_list", set);
			editor.apply();
			
			break;
			
		default:
			Log.w(getString(R.string.app_name),
					"Something extrange happened:");
			Log.w(getString(R.string.app_name),
					"\tSelected unexpected menu item on" +
					" SMSFragment context menu");
		}
		
		return false;
	}

	public void onCheckedChanged(CompoundButton buttonView,
			boolean isChecked) {
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(getActivity()).edit();
		
		editor.putBoolean("send_sms", isChecked);
		editor.apply();
		
		buttonView.invalidate();
	}

	public void onDialogPositiveClick(DialogFragment dialog,
			String input_value) {
		Log.wtf(getString(R.string.app_name),
				"I'm not kidding, I will launch the missiles now! " +
						input_value);

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity()); 
				
		Set<String> phones = sharedPrefs.getStringSet("sms_list", new HashSet<String>());
		
		if(phones.size() > 1) {
			// TODO: SHOW ALERT DIALOG TO CONFIRM ADDING
			
		}
		
		// TODO: From here to the method end will be on the affirmative answer to the previous TODO
		phones.add(input_value);
		
		/* Add to shared preferences */
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putStringSet("sms_list", phones);
		editor.apply();
		
		/* Adding to the UI */
		EditTextPreference phone = new EditTextPreference(getActivity());
		phone.setText(input_value);
		phone.setTitle(input_value);
		phone.setDialogTitle(R.string.edit);
		phone.getEditText().setInputType(
				InputType.TYPE_CLASS_PHONE);
		
		_phones.add(phone);
		
		getPreferenceScreen().addPreference(phone);
		
	}

	public void onDialogNegativeClick(DialogFragment dialog) {
		Log.wtf(getString(R.string.app_name),
				"Don't be afroid, my friends. The war is over");
	}
}
