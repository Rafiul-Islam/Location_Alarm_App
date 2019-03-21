package com.example.rafiulislamrafi.locationalarm;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase_Helper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "UserData.db";
    public static final String TABLE_NAME = "USER_INPUT";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "PLACE";
    public static final String COL_3 = "TASK";
    public static final String COL_4 = "TIME";
    public static final String COL_5 = "DATE";

    public DataBase_Helper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table " + TABLE_NAME + " (ID TEXT PRIMARY KEY, PLACE TEXT, TASK TEXT, TIME TEXT, DATE TEXT) " );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String id, String place, String task, String time, String date){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_1, id);
        contentValues.put(COL_2, place);
        contentValues.put(COL_3, task);
        contentValues.put(COL_4, time);
        contentValues.put(COL_5, date);

        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        if (result == -1){
            return false;
        }

        else {
            return true;
        }
    }

    public Cursor getAllData(){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME, null);
        return data;
    }

    public boolean updateData(String id, String place, String task, String time, String date) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_1, id);
        contentValues.put(COL_2, place);
        contentValues.put(COL_3, task);
        contentValues.put(COL_4, time);
        contentValues.put(COL_5, date);

        sqLiteDatabase.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});

        return true;

    }

    public Integer deleteData(String id) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        return sqLiteDatabase.delete(TABLE_NAME, "ID = ?", new String[]{id});

    }

}

