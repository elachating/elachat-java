package com.eladapp.elachat.chat;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.eladapp.elachat.R;

import org.ela.Carrier.Chatcarrier;
import org.elastos.carrier.UserInfo;

/**
 * @author liu
 * @date 2018-10-3
 */
public class ChatmenurActivity extends Activity implements OnClickListener{

    private LinearLayout uploadRecord;
    private LinearLayout registerRecord;
    private LinearLayout newMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatrightmenu);
        System.out.println("弹出框运行了");
        initView();
    }


    private void initView(){
        uploadRecord = findViewById(R.id.add_friend_layout);
      //  registerRecord = findViewById(R.id.register_record_layout);
       // newMessage = findViewById(R.id.new_massage_layout);

        uploadRecord.setOnClickListener(this);
       // registerRecord.setOnClickListener(this);
       // newMessage.setOnClickListener(this);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        finish();
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_friend_layout:
                Chatcarrier chatcarrier = new Chatcarrier();
                UserInfo myinfo = chatcarrier.getmyinfo();
                String myusername = myinfo.getName();
                if(myusername.equals("")){
                    if(getlangconfig().equals("cn")){
                        Toast.makeText(getApplicationContext(), "昵称不能为空，请设置昵称", Toast.LENGTH_SHORT).show();
                    }else if(getlangconfig().equals("en")){
                        Toast.makeText(getApplicationContext(), "Nickname cannot be empty. Please set a nickname.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "昵称不能为空，请设置昵称", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent();
                    intent.setClass(this, MyInfoActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent();
                    intent.setClass(this,AddFriendsActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.add_group_layout:
                //SharedData.resultID=2;
                break;
            default:
                //SharedData.resultID=0;
                break;
        }
        this.finish();
    }
    public String getlangconfig(){
        String lang = "";
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        String langs = String.valueOf(config.locale);
        if(langs.equals("cn")){
            lang = "cn";
        }else if(langs.equals("en")){
            lang = "en";
        }else{
            lang = "cn";
        }
        return lang;
    }
}
