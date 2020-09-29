package com.demo.sisyphus.chatRobot.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.demo.sisyphus.chatRobot.R;
import com.demo.sisyphus.chatRobot.adapter.ChatAdapter;
import com.demo.sisyphus.chatRobot.bean.Msg;
import com.demo.sisyphus.chatRobot.database.DBManager;
import com.demo.sisyphus.chatRobot.utils.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etMsg;
    private Button btnSend;
    private RecyclerView rvChat;
    private ImageView ivSet;
    private ChatAdapter chatAdapter;
//    private ArrayList<Msg> list;

    private String inputText;
    String strJson;
    private String text;
    private String tulingUrl="http://openapi.tuling123.com/openapi/api/v2";

    private DBManager dbmanager = new DBManager(this);
    private  ArrayList<Msg> msgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        statusBarHide(this);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        initView();
        btnSend.setOnClickListener(this);
        ivSet.setOnClickListener(this);
    }

    private void initView() {
        etMsg = (EditText) findViewById(R.id.et_msg);
        btnSend = (Button) findViewById(R.id.btn_send);
        ivSet = (ImageView) findViewById(R.id.iv_set);

        rvChat = (RecyclerView) findViewById(R.id.rv_chat);
        rvChat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        msgList =  dbmanager.getMsgList();
        chatAdapter = new ChatAdapter(this, msgList);
        rvChat.setAdapter(chatAdapter);
        rvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send:
                Msg msg_my = new Msg(etMsg.getText().toString(), 0);
                msgList.add(msg_my);//插入list
                dbmanager.insert(msg_my);//插入数据库
                chatAdapter.notifyItemInserted(chatAdapter.getItemCount() - 1);//通知适配器更新
                rvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);//滑动界面
                inputText = etMsg.getText().toString();
                Log.i("inputText", inputText);
                strJson = writeJSON(inputText);//将数据转化为json数据格式
                etMsg.setText("");
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            text = OkHttpUtil.post(tulingUrl,strJson);//得到post的返回值
                            Log.i("post strJson", strJson);
                            readJSON(text);//解析json数据
                            Msg msg_robot = new Msg(text, 1);
                            msgList.add(msg_robot);//插入list
                            dbmanager.insert(msg_robot);//插入数据库
                        } catch (IOException e) {
                            Log.e("IOException", e.toString());
                        }
                        handler.sendEmptyMessage(0);//通知适配器更新
                    }
                }.start();
                break;
            case R.id.iv_set:
                Intent intent= new Intent(MainActivity.this,setActivity.class);
                startActivity(intent);
                break;
        }

    }

    public void readJSON(String strJson) {
        Log.i("strJson", strJson);
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(strJson);
            JSONArray jsonArray = jsonObject.getJSONArray("results");//得到键值为results的jsonArray
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                String values = jsonObject2.getString("values");
                JSONObject jsonObject3 =new JSONObject(values);
                text = jsonObject3.getString("text");//得到返回的text
                Log.i("getText", text);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String writeJSON(String inputText){
        String Json = "{" +
                "\"reqType\":0," +
                "    \"perception\": {" +
                "        \"inputText\": {" +
                "            \"text\":" +"\""+inputText+"\""+
                "        }," +
                "        \"selfInfo\": {" +
                "            \"location\": {" +
                "                \"city\": \"江苏\"," +
                "                \"province\": \"南京\"," +
                "                \"street\": \"文苑路\"" +
                "            }" +
                "        }" +
                "    }," +
                "    \"userInfo\": {" +
                "        \"apiKey\": \"e9d5e9b6e89146e0a7b499537f12ad06\"," +
                "        \"userId\": \"357046\"" +
                "    }" +
                "}";
        return Json;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 0:
                    chatAdapter.notifyItemInserted(chatAdapter.getItemCount() - 1);
                    rvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                    break;
            }
        }
    };

    protected void onStart() {
        super.onStart();
        initView();
    }

    protected void onRestart() {
        super.onRestart();
//        chatAdapter.getId();
        initView();
    }

    public void onResume() {
        super.onResume();
        initView();
    }
}
