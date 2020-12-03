package com.example.weatherappproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String CITY_TABLE = "CITY_TABLE";
    public static final String COLUMN_CITY_NAME = "CITY_NAME";
    public static final String COLUMN_ID = "ID";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "city.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + CITY_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CITY_NAME + " TEXT)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(CityModel cityModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CITY_NAME, cityModel.getCity());

        long insert = db.insert(CITY_TABLE, null, cv);
        if (insert == -1){
            return false;
        }else   {
            return true;
        }
    }

    public boolean deleteOne(CityModel cityModel){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + CITY_TABLE + " WHERE " + COLUMN_ID + " = " + cityModel.getId();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            return true;
        }
        else{
            return false;
        }
    }


    public List<CityModel> getEveryone() {

        List<CityModel> returnList= new ArrayList<>();

        String queryString = "SELECT * FROM " + CITY_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            do{
                int cityID = cursor.getInt(0);
                String cityName = cursor.getString(1);

                CityModel newCity = new CityModel(cityID,cityName);
                returnList.add(newCity);
            } while (cursor.moveToNext());

        }else{
            //failure
        }
        cursor.close();
        db.close();
        return returnList;

    }
}
