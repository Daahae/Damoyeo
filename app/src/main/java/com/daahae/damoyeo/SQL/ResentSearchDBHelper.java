package com.daahae.damoyeo.SQL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ResentSearchDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String SEARCH = "search";

    public ResentSearchDBHelper(Context context){
        super(context, "User.db",null,DATABASE_VERSION);
        Log.v("DB 생성"," 생성됨");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String ClosetSQL = "create table tb_resent_search "+
                "(_id integer primary key autoincrement, "+
                SEARCH + ")";
        Log.v("table 생성","Table 생성됨");

        db.execSQL(ClosetSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == DATABASE_VERSION){
            db.execSQL("drop table tb_resent_search");
            onCreate(db);
        }
    }

    public void insertResentSearch(String search){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into tb_resent_search (" + SEARCH + ") values ('"+search+"')");
        db.close();
    }


    public String findSearch(String search){
        SQLiteDatabase db = this.getWritableDatabase();
        String str;
        Cursor cursor = db.rawQuery("SELECT * from tb_resent_search WHERE search ='" + search + "';",null);
        if(!cursor.moveToFirst())
            str = null;
        else
            str = cursor.getString(0);
        db.close();
        return str;
    }

    public void updateSearch(String search, String chage){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update tb_resent_search set " + SEARCH + " ='"+chage+"' Where " + SEARCH + " = '"+search+"'");
        db.close();
    }

    public void deleteSearch(String search){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("Delete from tb_resent_search WHERE " + SEARCH + " ='" + search + "';");
    }


    public Cursor selectAllSearch(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from tb_resent_search",null);
        if(!cursor.moveToFirst()){
            db.close();
            return null;
        }
        else{
            db.close();
            return cursor;
        }
    }

}
