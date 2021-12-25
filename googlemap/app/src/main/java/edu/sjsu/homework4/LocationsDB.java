package edu.sjsu.homework4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocationsDB extends SQLiteOpenHelper{
    private static String DBNAME = "location";

    private static int VERSION = 1;

    public static final String FIELD_ROW_ID = "_id";
    public static final String FIELD_LAT = "latitude";
    public static final String FIELD_LNG = "long";
    public static final String FIELD_ZOOM = "zoom";
    private static final String DATABASE_TABLE = "locations";
    private SQLiteDatabase mDB;

    public LocationsDB(Context context) {
        super(context, DBNAME, null, VERSION);
        this.mDB = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql =     "create table " + DATABASE_TABLE + " ( " +
                FIELD_ROW_ID + " integer primary key autoincrement , " +
                FIELD_LNG + " double , " +
                FIELD_LAT + " double , " +
                FIELD_ZOOM + " text " +
                " ) ";

        db.execSQL(sql);
    }

    public long insert(ContentValues contentValues){
        long rowID = mDB.insert(DATABASE_TABLE, null, contentValues);
        return rowID;
    }

    public int del(){
        int cnt = mDB.delete(DATABASE_TABLE, null , null);
        return cnt;
    }

    public Cursor getAllLocations(){
        return mDB.query(DATABASE_TABLE, new String[] { FIELD_ROW_ID,  FIELD_LAT , FIELD_LNG, FIELD_ZOOM } , null, null, null, null, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}