package com.example.dan2.ships;

import android.app.Activity;
import android.os.Bundle;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "scoreboard.db";
    public static final String RECORDS_TABLE_NAME = "records";
    public static final String RECORDS_COLUMN_ID = "id";
    public static final String RECORDS_COLUMN_NAME = "name";
    public static final String RECORDS_COLUMN_TIME = "time";
    public static final String RECORDS_COLUMN_SHOTS = "shots";
    public static final String RECORDS_COLUMN_ALIVED = "alive";

    public static ArrayList<String> arrayList = new ArrayList<String>();

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE records " + "(id INTEGER PRIMARY KEY, name TEXT, time INTEGER, shots INTEGER, alive INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS records");
        onCreate(db);
    }

    public boolean insertContact(String name, long time, int shots, int alive)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if(name.length() <= 0){
            return false;
        }
        else {
            contentValues.put("name", name);
            contentValues.put("time", time);
            contentValues.put("shots", shots);
            contentValues.put("alive", alive);
            db.insert("records", null, contentValues);
            return true;
        }
    }

    //Cursor representuje vracena data
    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from records where id=" + id + "", null);
        return res;
    }

    //TODO - update zatim nebudu potrebovat
    public boolean updateContact (Integer id, String name, String time, String shots, String alive)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("time", time);
        contentValues.put("shots", shots);
        contentValues.put("alive", alive);
        db.update("records", contentValues, "id="+id, null);
        return true;
    }

    public boolean deleteContact (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("records", "id="+id, null);
        return true;
    }

    public void setAllContacs()
    {
        arrayList.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from records", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            arrayList.add(res.getString(res.getColumnIndex(RECORDS_COLUMN_NAME)));
            res.moveToNext();
        }
    }

    public ArrayList<String> getAllContacsName()
    {
        return arrayList;
    }

    public void removeAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(RECORDS_TABLE_NAME, "1", null);
    }
}
