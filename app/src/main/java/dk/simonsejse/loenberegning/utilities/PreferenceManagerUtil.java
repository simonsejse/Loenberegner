package dk.simonsejse.loenberegning.utilities;

import android.content.Context;

import androidx.preference.PreferenceManager;

public class PreferenceManagerUtil {
    public static int getHourlyRate(final Context context) {
        final String hourlyRateAsString = PreferenceManager.getDefaultSharedPreferences(context).getString(PreferenceUtil.hourlyRate.getText(), "120");
        try{
            return Integer.parseInt(hourlyRateAsString);
        }catch(NumberFormatException nfe){
            return 120;
        }
    }
}
