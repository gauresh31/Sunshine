package com.android.nano.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;

;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String TAG = DetailActivityFragment.class.getSimpleName();
    private static final String FORECAST_TAG = "Sunshine App";
    private String mforecastStr;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail,
                container, false);

        TextView detailtext = (TextView) rootView.findViewById(R.id.tv_detail);
        Intent in = getActivity().getIntent();

        if (in != null && in.hasExtra(Intent.EXTRA_TEXT)) {
            mforecastStr = in.getStringExtra(Intent.EXTRA_TEXT);
            detailtext.setText(mforecastStr);
        }

        return rootView;
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);//Return to your activity instead of the activity handling the intent
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mforecastStr + FORECAST_TAG);
        return shareIntent;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);

        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareIntent());
        } else {
            Log.i(TAG, "ShareActionProvider null");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
