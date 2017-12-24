package ankit.applespace.carbuddies.JavaPackage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by ankit on 11/12/17.
 */

public class SpClass {
    private static final String TAG = "Ankit";
    private Context context;
    private SharedPreferences sp;

    public SpClass(Activity context) {
        this.context = context;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setValue(String key,String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
        Log.i(TAG, "savedValue: "+key+": "+getValue(key));

    }

    public String getValue(String key) {
        return sp.getString(key, null);

    }
}
