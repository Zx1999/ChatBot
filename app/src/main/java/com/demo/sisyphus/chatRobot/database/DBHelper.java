package com.demo.sisyphus.chatRobot.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.demo.sisyphus.chatRobot.bean.Msg;

public class DBHelper extends SQLiteOpenHelper {

    //数据库版本
     private static final int DATABASE_VERSION = 1;

     //数据库名称
     private static final String DATABASE_NAME = "my_sqlite_Msg";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //创建数据表
                 String CREATE_TABLE_STUDENT="CREATE TABLE "+ Msg.TABLE+"("
                         +Msg.KEY_id+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
                         +Msg.KEY_msg+" TEXT, "
                         +Msg.KEY_type+" INTEGER)";
                 db.execSQL(CREATE_TABLE_STUDENT);
        Log.i("CREATE",Msg.TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //如果旧表存在，删除，所以数据将会消失
                 db.execSQL("DROP TABLE IF EXISTS "+ Msg.TABLE);

                 //再次创建表
                 onCreate(db);

    }
}
