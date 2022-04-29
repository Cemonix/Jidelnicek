package com.example.jidelnicek;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DatabaseController extends SQLiteOpenHelper implements Serializable {

    private static DatabaseController db;

    public static final String LUNCH_TABLE = "LUNCH_TABLE";
    public static final String ID = "ID";
    public static final String LUNCH = "LUNCH";
    public static final String SPAN = "SPAN";
    public static final String TYPE = "TYPE";

    public static DatabaseController getInstance(Context context){

        if(db == null){
            db = new DatabaseController(context);
        }
        return db;
    }

    private DatabaseController(@Nullable Context context) {
        super(context, "lunch.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + LUNCH_TABLE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LUNCH + " TEXT NOT NULL UNIQUE, " +
                SPAN + " INTEGER NOT NULL, " +
                TYPE + " TEXT NOT NULL)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    public boolean insert(Lunch lunch){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(LUNCH, lunch.getName());
        cv.put(SPAN, lunch.getSpan());
        cv.put(TYPE, lunch.getType());

        long l = db.insert(LUNCH_TABLE, null, cv);

        db.close();
        return l >= 0;
    }

    public List<Lunch> select(String chosenType, int chosenDay){
        List<Lunch> lunchList = new ArrayList<>();
        String queryString;

        if(chosenType.equals("Všechny") && chosenDay == -1){
            queryString = "SELECT * FROM " + LUNCH_TABLE;
        }
        else if(chosenType.equals("Všechny")){
            queryString = "SELECT * FROM " + LUNCH_TABLE + " WHERE " + SPAN + "=" + chosenDay;
        }
        else if(chosenDay == -1){
            queryString = "SELECT * FROM " + LUNCH_TABLE + " WHERE " + TYPE + "=" + "'" + chosenType + "'";
        }
        else {
            queryString = "SELECT * FROM " + LUNCH_TABLE + " WHERE " + TYPE + "=" + "'" + chosenType + "'" +
                    " AND " + SPAN + "=" + chosenDay;
        }

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            do {
                int lunch_id = cursor.getInt(0);
                String lunch_name = cursor.getString(1);
                int lunch_span = cursor.getInt(2);
                String lunch_type = cursor.getString(3);

                Lunch lunch = new Lunch(lunch_id, lunch_name, lunch_span, lunch_type);
                lunchList.add(lunch);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return lunchList;
    }

    public boolean remove(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + LUNCH_TABLE + " WHERE " + LUNCH + "=" + "'" + name + "'";

        Cursor cursor = db.rawQuery(queryString, null);
        boolean isRemoved = !cursor.moveToFirst();
        cursor.close();
        db.close();

        return isRemoved;
    }

}
