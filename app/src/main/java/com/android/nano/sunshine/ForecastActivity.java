package com.android.nano.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForecastActivity extends Fragment {
    String TAG = ForecastActivity.class.getSimpleName();
    ListView lv_forecast;
    String[] finalForecast;
    ArrayAdapter<String> adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        lv_forecast = (ListView) rootView.findViewById(R.id.list_view);

        String[] foreCastArray = {"Today - Sunny - 88/63", "Tomorrow - Windy - 68/65", "Wed - Rainy - 38/67", "Thurs - Sunny - 88/63",
                "Friday - Sunny - 88/63", "Sat - Cold - 78/83"};


//        new AsyncForecastData().execute("400009");
//        updateForecast();
//        List<String> foreCastList = new ArrayList<String>(Arrays.asList(foreCastArray));
//
//        adapter = new ArrayAdapter<String>(getContext(), R.layout.list_item, R.id.tv_list_item_forecast, new ArrayList<String>());
//        lv_forecast.setAdapter(adapter);

        lv_forecast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String forecast = adapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(intent);
//                Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    public void updateForecast() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String loc = prefs.getString(getString(R.string.pref_location_key),
                String.valueOf(R.string.pref_location_default_value));

        new AsyncForecastData().execute(loc);
    }

    public void onStart() {
        super.onStart();
        PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_general, false);
        updateForecast();
    }

    class AsyncForecastData extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            String TAG = AsyncForecastData.class.getSimpleName();
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            int numOfDays;
            try {

                //String URl = "http://api.openweathermap.org/data/2.5/forecast/daily?q=400009&mode=json&units=metric&cnt=7";
                String format = "json";
                String unit = "metric";
                numOfDays = 7;

                final String baseUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QueryParam = "q";
                final String FormatParam = "mode";
                final String UnitsParam = "unit";
                final String DaysParam = "cnt";

                Uri buildUri = Uri.parse(baseUrl).buildUpon()
                        .appendQueryParameter(QueryParam, params[0])
                        .appendQueryParameter(FormatParam, format)
                        .appendQueryParameter(UnitsParam, unit)
                        .appendQueryParameter(DaysParam, Integer.toString(numOfDays))
                        .build();

                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL(buildUri.toString());
                Log.i("Build Uri", buildUri.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                Log.i("Response", forecastJsonStr);
            } catch (IOException e) {
                Log.e(TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getDataFromJson(forecastJsonStr, numOfDays);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(String[] forecastResponse) {
            if (forecastResponse != null) {
                try {
                    finalForecast = forecastResponse;
                    List<String> foreCastList = new ArrayList<String>(Arrays.asList(finalForecast));

                    adapter = new ArrayAdapter<String>(getContext(), R.layout.list_item, R.id.tv_list_item_forecast, foreCastList);
                    lv_forecast.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String[] getDataFromJson(String json, int days) throws JSONException {


        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "list";
        final String OWM_WEATHER = "weather";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_DESCRIPTION = "main";

        JSONObject forecastJson = new JSONObject(json);
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

        // OWM returns daily forecasts based upon the local time of the city that is being
        // asked for, which means that we need to know the GMT offset to translate this data
        // properly.

        // Since this data is also sent in-order and the first day is always the
        // current day, we're going to take advantage of that to get a nice
        // normalized UTC date for all of our weather.

        Time dayTime = new Time();
        dayTime.setToNow();

        // we start at the day returned by local time. Otherwise this is a mess.
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

        // now we work exclusively in UTC
        dayTime = new Time();

        String[] resultStrs = new String[days];
        for (int i = 0; i < weatherArray.length(); i++) {
            String day;
            String description;
            String highAndLow;

            // Get the JSON object representing the day
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            // The date/time is returned as a long.  We need to convert that
            // into something human-readable, since most people won't read "1400356800" as
            // "this saturday".
            long dateTime;
            // Cheating to convert this to UTC time, which is what we want anyhow
            dateTime = dayTime.setJulianDay(julianStartDay + i);
            day = getReadableDateString(dateTime);

            // description is in a child array called "weather", which is 1 element long.
            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);

            // Temperatures are in a child object called "temp".  Try not to name variables
            // "temp" when working with temperature.  It confuses everybody.
            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            double high = temperatureObject.getDouble(OWM_MAX);
            double low = temperatureObject.getDouble(OWM_MIN);

            highAndLow = formatHighLows(high, low);
            resultStrs[i] = day + " - " + description + " - " + highAndLow;
        }

        for (String s : resultStrs) {
            Log.i("Forecast Result", s);
        }
        return resultStrs;

    }

    private String getReadableDateString(long time) {
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(time);
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        // For presentation, assume the user doesn't care about tenths of a degree.

        SharedPreferences share = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String strType = share.getString(getString(R.string.pref_units_key),
                String.valueOf(R.string.pref_units_default));

        if(strType.equals(getString(R.string.pref_units_imperial))){
            high = (high * 1.8) + 32;
            low = (low * 1.8) + 32;
        } else if(!strType.equals(getString(R.string.pref_units_default))){
            Log.i("ForeCastActivity","No Type found");
        }

        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_activity_forecast, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            updateForecast();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


