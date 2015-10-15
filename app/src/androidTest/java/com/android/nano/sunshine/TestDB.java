package com.android.nano.sunshine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import junit.framework.TestCase;

/**
 * Created by Gauresh on 10/01/2015.
 */
public class TestDB extends TestCase {

    Context con;

    public long testTableLocation() {
        long rowId;
        WeatherDBHepler weatherDBHepler = new WeatherDBHepler(con);
        SQLiteDatabase db = weatherDBHepler.getWritableDatabase();

        ContentValues con = TestUtilities.createValues();

        rowId = db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, con);

        assertTrue(rowId > 0);

        Cursor cur = db.query(WeatherContract.LocationEntry.TABLE_NAME, null, null, null, null, null, null);

        assertTrue("No records", cur.moveToFirst());

        assertFalse("More Than one record", cur.moveToNext());

        cur.close();
        db.close();
        return rowId;
    }

}
