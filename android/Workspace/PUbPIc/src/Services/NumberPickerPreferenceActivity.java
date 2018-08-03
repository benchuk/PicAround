package Services;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class NumberPickerPreferenceActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager preferenceManager = getPreferenceManager();
        preferenceManager.setSharedPreferencesMode(MODE_PRIVATE);
        preferenceManager.setSharedPreferencesName("numberPicker.preferences");

       // addPreferencesFromResource(R.xml.preferences);
    }
}
