package com.demo.sisyphus.chatRobot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.demo.sisyphus.chatRobot.R;

public class setActivity extends AppCompatActivity implements View.OnClickListener {
    private Button record_btn;
    private Button about_btn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_set);

        initView();

        record_btn.setOnClickListener(this);
        about_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.record://跳转至聊天记录界面
                Intent intent= new Intent(setActivity.this,recordActivity.class);
                startActivity(intent);
                break;
            case R.id.about://弹出对话框
                AlertDialog alertDialog = new AlertDialog.Builder(setActivity.this)
                        .setTitle("关于chatRobot")//标题
                        .setMessage("制作:杨静 赵轩\n"+
                                "\n感谢您对本软件的使用与支持,如有意见或建议,欢迎联系作者~\n")//内容
                        .setIcon(R.mipmap.ameng)//图标
                        .create();
                alertDialog.show();
                break;
        }
    }

    private void initView(){
        record_btn = (Button) findViewById(R.id.record);
        about_btn = (Button) findViewById(R.id.about);
    }
}
