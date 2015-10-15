package com.android.nano.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, new ForecastActivity()).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SetPrefsActivity.class);
            startActivity(intent);

//            FragmentManager mFragmentManager = getFragmentManager();
//            FragmentTransaction mFragmentTransaction = mFragmentManager
//                    .beginTransaction();
//            SettingsActivity mPrefsFragment = new SettingsActivity();
//            mFragmentTransaction.replace(R.id.fragment, mPrefsFragment);
//            mFragmentTransaction.commit();
////            getFragmentManager().beginTransaction().replace(R.id.fragment,
////                    new SettingsActivity()).commit();

            return true;
        }
        if (id == R.id.action_map) {
            openPreferedLocation();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openPreferedLocation() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String loc = prefs.getString(getString
                (R.string.pref_location_key), getString(R.string.pref_location_default_value));

        Uri geoLoc = Uri.parse("geo:0,07").buildUpon().appendQueryParameter("q", loc).build();
        Intent in = new Intent(Intent.ACTION_VIEW);
        in.setData(geoLoc);

        if (in.resolveActivity(getPackageManager()) != null) {
            startActivity(in);
        } else {
            Log.i("MainActivity", "Couldnt find" + loc);
        }
    }
}
