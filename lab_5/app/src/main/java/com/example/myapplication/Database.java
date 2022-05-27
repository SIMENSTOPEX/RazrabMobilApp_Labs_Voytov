package com.example.myapplication;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LOGIN = "login";
    public static final String COLUMN_PASSWORD = "password";
    private static final String DATABASE_NAME = "test.db";
    private static final int DATABASE_VERSION = 1;


    private String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "(" + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_LOGIN + " text not null, " + COLUMN_PASSWORD + " text not null);";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion > 1) {
            db.execSQL(" DROP TABLE IF EXISTS " + TABLE_USERS);
        }
    }
    public boolean addUser(String login, String password){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues c=new ContentValues();
        c.put(COLUMN_LOGIN,login);
        c.put(COLUMN_PASSWORD,password);
        long result=db.insert(TABLE_USERS,null,c);
        if (result==-1){
            return false;
        } else {
            return true;
        }
    }

    public String getLoginData(String login,String password)
    {
        SQLiteDatabase sql=this.getReadableDatabase();
        String query=" select count(*) from "+TABLE_USERS+" where login ='"+login+"' and password='"+password+"'";
        Cursor cursor =sql.rawQuery(query,null);
        cursor.moveToFirst();
        String count = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
        return count;
    }
}
