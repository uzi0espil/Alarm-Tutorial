package com.alarm.coderji.org.alarm_tutorial.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alarm.coderji.org.alarm_tutorial.Utilities.Util;

/**
 * Created by Osama on 12/25/2015.
 */
abstract class AbstractModel {

    public static final String COL_ID = "id";

    protected long id;

    public static String getIDSQLStatement(){
        return Util.concat(COL_ID, " INTEGER PRIMARY KEY AUTOINCREMENT, ");
    }

    public void update(ContentValues cv){
        cv.put(COL_ID, id);
    }

    public void load(Cursor cursor){
        id = cursor.getLong(cursor.getColumnIndex(COL_ID));
    }

    protected void reset(){
        id = 0;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    abstract long save(SQLiteDatabase db);
    abstract boolean update(SQLiteDatabase db);
    abstract boolean load(SQLiteDatabase db);
    abstract boolean delete(SQLiteDatabase db);

    public long persist(SQLiteDatabase db){
        if(id > 0)
            return update(db) ? id : 0;
        return save(db);
    }

}
