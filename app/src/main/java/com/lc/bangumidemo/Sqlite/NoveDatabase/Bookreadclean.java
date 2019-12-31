package com.lc.bangumidemo.Sqlite.NoveDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lc.bangumidemo.Sqlite.CollectDatabase.Collectdbhelper;

public class Bookreadclean {
    public static void clean(Context context){
        MyDatabaseHelper dbhelper=new MyDatabaseHelper(context,"bookstore",null,1);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("BookRead",null,null);
        db.delete("BookData",null,null);
        db.close();

    }
    public static void cleanall(Context context){
        MyDatabaseHelper dbhelper=new MyDatabaseHelper(context,"bookstore",null,1);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("BookRead",null,null);
        db.delete("BookIndex",null,null);
        db.delete("BookData",null,null);
        db.close();
    }
    public static void cleancollect(Context context){
        Collectdbhelper dbhelper=new Collectdbhelper(context,"collect.db",null,1);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("CollectData",null,null);
        db.close();

    }
}
