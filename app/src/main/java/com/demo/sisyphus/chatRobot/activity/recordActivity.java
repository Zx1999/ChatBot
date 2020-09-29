package com.demo.sisyphus.chatRobot.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.demo.sisyphus.chatRobot.R;
import com.demo.sisyphus.chatRobot.adapter.ChatAdapter;
import com.demo.sisyphus.chatRobot.bean.Msg;
import com.demo.sisyphus.chatRobot.database.DBManager;

import java.io.IOException;
import java.util.ArrayList;

public class recordActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnDelete;
    private RecyclerView rvChat;
    private ChatAdapter chatAdapter;
    private ArrayList<Msg> msgList;
    private DBManager dbmanager = new DBManager(this);
    private int deleteline;
    private int id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_record);
        initView();
        Toast.makeText(recordActivity.this,"点击头像可选中消息",Toast.LENGTH_SHORT).show();
        btnDelete.setOnClickListener(this);
        //chatAdapter绑定setOnItemClickListener监听器
        chatAdapter.setOnItemClickListener(new ChatAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        deleteline = position;
                        //获得选中位置的msg
                        Msg msg = msgList.get(position);
                        Log.i("msg","----->" + msg.getMsg());
                        Log.i("id","----->" + msg.getId());
                        id = msg.getId();
                        Toast.makeText(recordActivity.this,"您选中了第"+position+"行：" + msg.getMsg(),Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onLongClick(int position) {
//                        Toast.makeText(recordActivity.this,"您长按点击了"+position+"行",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public void onClick(View v) {
        new Thread() {
            @Override
            public void run() {
                msgList.remove(deleteline);//将对应msg从msgList中删除
                dbmanager.delete(id);//将对应msg从数据库中删除
                handler.sendEmptyMessage(1);

            }
        }.start();
    }
    private void initView(){
        btnDelete = (Button) findViewById(R.id.btn_delete);
        rvChat = (RecyclerView) findViewById(R.id.rv_chat);
        rvChat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        msgList =  dbmanager.getMsgList();
        chatAdapter = new ChatAdapter(this, msgList);
        rvChat.setAdapter(chatAdapter);
    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 1:
                    chatAdapter.notifyItemRemoved(deleteline);
                    chatAdapter.notifyItemRangeChanged(0,msgList.size());
                    Log.i("last","----->" + msgList.size());
                    break;
            }
        }
    };
}
