package com.android.nano.sunshine;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gauresh on 09/30/2015.
 */
public class WeatherDBHepler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    static String DATABASE_NAME = "weather.db";

    public WeatherDBHepler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_LOCATION_TABLE = "CREATE TABLE " + WeatherContract.LocationEntry.TABLE_NAME + "(" +
                WeatherContract.LocationEntry._ID + "INTEGER PRIMARY KEY, " +
                WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + "TEXT UNIQUE NOT NULL, " +
                WeatherContract.LocationEntry.COLUMN_CITY_NAME + "TEXT NOT NULL, " +
                WeatherContract.LocationEntry.COLUMN_COORD_LAT + "REAL NOT NULL, " +
                WeatherContract.LocationEntry.COLUMN_COORD_LONG + "REAL NOT NULL, " +
                " );";

        final String CREATE_WEATHER_TABLE = "CREATE TABLE " + WeatherContract.WeatherEntry.TABLE_NAME + "(" +
                WeatherContract.WeatherEntry._ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WeatherContract.WeatherEntry.COLUMN_LOC_KEY + "INTEGER NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_DATE + "INTEGER NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_SHORT_DESC + "TEXT NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_WEATHER_ID + "INTEGER NOT NULL, " +

                WeatherContract.WeatherEntry.COLUMN_MAX_TEMP + "REAL NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_MIN_TEMP + "REAL NOT NULL, " +

                WeatherContract.WeatherEntry.COLUMN_HUMIDITY + "REAL NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_PRESSURE + "REAL NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_WIND_SPEED + "REAL NOT NULL, " +
                WeatherContract.WeatherEntry.COLUMN_DEGREES + "REAL NOT NULL, " +

                "FOREIGN KEY (" + WeatherContract.WeatherEntry.COLUMN_LOC_KEY + ") REFERENCES " +
                WeatherContract.LocationEntry.TABLE_NAME + " (" + WeatherContract.LocationEntry._ID + "), " +

                "UNIQUE(" + WeatherContract.WeatherEntry.COLUMN_DATE + " ," +
                WeatherContract.WeatherEntry.COLUMN_LOC_KEY + ")" + "ON CONFLICT REPLACE" +
                ");";

        db.execSQL(CREATE_LOCATION_TABLE);
        db.execSQL(CREATE_WEATHER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + WeatherContract.LocationEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WeatherContract.WeatherEntry.TABLE_NAME);
        onCreate(db);
    }
}
