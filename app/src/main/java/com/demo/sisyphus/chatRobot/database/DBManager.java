package com.demo.sisyphus.chatRobot.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.demo.sisyphus.chatRobot.activity.MainActivity;
import com.demo.sisyphus.chatRobot.bean.Msg;

import java.util.ArrayList;

public class DBManager {
    private DBHelper dbHelper;

    public DBManager(Context context){
        dbHelper=new DBHelper(context);
    }

    public void insert(Msg msg){
        //打开连接，写入数据
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Msg.KEY_msg,msg.msg);
        values.put(Msg.KEY_type,msg.type);
        db.insert(Msg.TABLE,null,values);
        db.close();
        Log.i("insert","have inserted");
    }

    public ArrayList<Msg> getMsgList(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                Msg.KEY_msg+","+
                Msg.KEY_id+","+
                Msg.KEY_type+" FROM "+Msg.TABLE;
        ArrayList<Msg> msgList=new ArrayList<>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        Msg msg_begin = new Msg("你好鸭~~~", 1,0);
        msgList.add(msg_begin);

        if(cursor.moveToFirst()){
            do{
                String msg = cursor.getString(cursor.getColumnIndex(Msg.KEY_msg));
//                Log.i("msg",msg);
                String str_type = cursor.getString(cursor.getColumnIndex(Msg.KEY_type));
//                Log.i("str_type",str_type);
                int type = Integer.parseInt(str_type);

                String str_id = cursor.getString(cursor.getColumnIndex(Msg.KEY_id));
//                Log.i("str_type",str_type);
                int id = Integer.parseInt(str_id);

                Msg massage = new Msg(msg,type,id);
                msgList.add(massage);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return msgList;
    }

    public void delete(int msg_Id){

        SQLiteDatabase db=dbHelper.getWritableDatabase();
//        db.delete(Msg.TABLE,Msg.KEY_id+"=?", new String[]{String.valueOf(msg_Id)});
        String delete_str = "delete from " + Msg.TABLE + " where " + Msg.KEY_id + " = " + msg_Id;
        db.execSQL(delete_str);

        db.close();
        Log.i("delete","msg_id------>" + msg_Id);
    }

}

