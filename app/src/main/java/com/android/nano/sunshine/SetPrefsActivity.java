package com.android.nano.sunshine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SetPrefsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(android.R.id.content, new SettingsActivity()).commit();
        }
    }

}
