package com.android.nano.sunshine;

import android.content.ContentValues;

import junit.framework.TestCase;

/**
 * Created by Gauresh on 09/30/2015.
 */
public class TestUtilities extends TestCase {

    public static ContentValues createValues() {
        String testLocationSetting = "99705";
        String testCityName = "North Pole";
        double testLatitude = 64.7488;
        double testLongitude = -147.353;

        ContentValues cv = new ContentValues();

        cv.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
        cv.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, testCityName);
        cv.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT, testLatitude);
        cv.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG, testLongitude);

        return cv;
    }
}
