package com.arduino.michael.arduinotempcontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Michael on 2016-08-17.
 */
public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "cigars";
    // table name
    private static final String TABLE_TEMPANDHUMID = "tempandhumid";
    //  Table Columns names
    private static final String KEY_ID ="id";
    private static final String KEY_TEMP = "temperature";
    private static final String KEY_HUMID= "humidity";
    private static final String KEY_DATE = "date";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TEMP_HUMID_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TEMPANDHUMID + " (" + KEY_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + KEY_TEMP + " TEXT, "
                + KEY_HUMID + " TEXT, " + KEY_DATE + " DATE" + ")";

        db.execSQL(CREATE_TEMP_HUMID_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS "  + TABLE_TEMPANDHUMID);
    // Creating tables again
        onCreate(db);
    }

    public boolean insertHumidor(Humidor humidor){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        try{
            //contentValues.put(KEY_ID, humidor.getId());
            contentValues.put(KEY_TEMP, humidor.getTemperature());
            contentValues.put(KEY_HUMID, humidor.getHumidity());
            contentValues.put(KEY_DATE, humidor.getCurrentDate());

            db.insert(TABLE_TEMPANDHUMID,null,contentValues);
            return true;

        }catch (Exception e){
            return false;
        }

    }

    public ArrayList<Humidor>getAllHumidors(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Humidor> humidors = new ArrayList<Humidor>();

        Cursor cur = db.rawQuery("select * from "+TABLE_TEMPANDHUMID,null);
        cur.moveToFirst();

        try{

            while(cur.isAfterLast()==false){
                Humidor h = new Humidor();
                h.setId(cur.getInt(cur.getColumnIndex("id")));
                h.setHumidity(cur.getDouble(cur.getColumnIndex("humidity")));
                h.setTemperature(cur.getDouble(cur.getColumnIndex("temperature")));
                h.setCurrentDate(cur.getString(cur.getColumnIndex("date")));
                humidors.add(h);
                cur.moveToNext();
            }
            return humidors;
        }catch (Exception e){
            return null;
        }
    }
}

