package org.barrabash.powernotifier.settings;

import android.app.ActionBar;
import android.preference.PreferenceFragment;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.CompoundButton;

public abstract class SwitchedFragment extends PreferenceFragment
	implements CompoundButton.OnCheckedChangeListener {
	
	protected Switch _headerSwitch;
	private ListView _listView;
	

	public void addSwitchToActionBar() {
		/* Configure the action bar */            
		_headerSwitch = new Switch(getActivity());
		_headerSwitch.setOnCheckedChangeListener(this);		
		
		ActionBar action_bar = getActivity().getActionBar();

		action_bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
				ActionBar.DISPLAY_SHOW_CUSTOM);
		action_bar.setCustomView(_headerSwitch, new ActionBar.LayoutParams(
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.WRAP_CONTENT,
				Gravity.CENTER_VERTICAL | Gravity.RIGHT));
	}
	
	public ListView getListView() {
		if (_listView == null) {
			View v = getView();
			
			if (v == null)
				throw new IllegalStateException("Content view not yet created");
			
			View listView = v.findViewById(android.R.id.list);
			
			if (! (listView instanceof ListView))
				throw new RuntimeException("Content has view with id attribute 'android.R.id.list' that is not a ListView object");
			
			_listView = (ListView) listView;
			}
		
		return _listView;
	}
}
