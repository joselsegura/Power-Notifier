package org.barrabash.powernotifier.settings;

import org.barrabash.powernotifier.R;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class GeneralFragment extends PreferenceFragment {
	private Preference.OnPreferenceChangeListener _summaryChanger = 
			new Preference.OnPreferenceChangeListener() {
	
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			preference.setSummary((String) newValue);
			return true;
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		addPreferencesFromResource(R.xml.preferences);
		
		EditTextPreference etp = (EditTextPreference)
				findPreference("smtp_user");
		etp.setSummary(etp.getText());
		etp.setOnPreferenceChangeListener(_summaryChanger);
		
		etp = (EditTextPreference) findPreference("smtp_pass");
		if(etp.getText() != null && ! etp.getText().isEmpty())
			etp.setSummary(R.string.password_set);
		else
			etp.setSummary(R.string.password_not_set);
		
		etp.setOnPreferenceChangeListener(
				new Preference.OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				String value = (String) newValue;
				
				if(value.isEmpty())
					preference.setSummary(R.string.password_not_set);
				
				else
					preference.setSummary(R.string.password_set);
				return true;
			}
		});
		
		etp = (EditTextPreference) findPreference("smtp_host");
		etp.setSummary(etp.getText());
		etp.setOnPreferenceChangeListener(_summaryChanger);
		
		etp = (EditTextPreference) findPreference("smtp_port");
		etp.setSummary(etp.getText());
		etp.setOnPreferenceChangeListener(_summaryChanger);
	}
}
