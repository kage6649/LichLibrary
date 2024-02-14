package com.AI.lichlibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {
    public static final String DBNAME = "log.db";
    public DB(Context context) {
        super(context, DBNAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table log(username TEXT , status TEXT)");
        MyDB.execSQL("create Table reg(username TEXT , status TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists log");
        MyDB.execSQL("drop Table if exists reg");
    }
    public Boolean inLog(String username, String status){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username", username);
        contentValues.put("status", status);
//        contentValues.put("usia",usia);
        long result = MyDB.insert("log", null, contentValues);
        if(result==-1) return false;
        else
            return true;
    }
    public Boolean inReg(String username, String status){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username", username);
        contentValues.put("status", status);
        long result = MyDB.insert("reg", null, contentValues);
        if(result==-1) return false;
        else
            return true;
    }

    public String cek() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from log", null);
        String usr;
        if (cursor.getCount() >=1 ){
            cursor.moveToFirst();
            int col = cursor.getColumnIndex("username");
            usr = cursor.getString(col);
        }else {
            usr = "";
        }
        MyDB.close();
        return usr;
    }
    public String cekReg() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from reg", null);
        String usr;
        if (cursor.getCount() >=1 ){
            cursor.moveToFirst();
            int col = cursor.getColumnIndex("username");
            usr = cursor.getString(col);
        }else {
            usr = "";
        }
        MyDB.close();
        return usr;
    }
    public void delLog(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("delete from log where username = ?", new String[]{username});
        MyDB.delete("log","username = ?",new String[]{username});
        MyDB.delete("log","username = ?",new String[]{""});
        MyDB.close();
    }
    public void delLog_All() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
//        Cursor cursor = MyDB.rawQuery("delete from log",null);
        MyDB.delete("log",null,null);
//        MyDB.delete("log","username = ?",new String[]{""});
        MyDB.close();
    }
    public void delReg(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("delete from reg where username = ?", new String[]{username});
        MyDB.delete("reg","username = ?",new String[]{username});
        MyDB.delete("reg","username = ?",new String[]{""});
        MyDB.close();
    }
    public void delReg_All() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
//        Cursor cursor = MyDB.rawQuery("delete from reg where username = ?", new String[]{username});
//        MyDB.delete("reg","username = ?",new String[]{username});
        MyDB.delete("reg",null,null);
        MyDB.close();
    }
}
