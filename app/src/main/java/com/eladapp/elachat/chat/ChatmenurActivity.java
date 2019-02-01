package com.eladapp.elachat.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

import com.eladapp.elachat.R;
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
                System.out.println("点击了");
                Intent intent=new Intent();
                intent.setClass(this,AddFriendsActivity.class);
                startActivity(intent);
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
}
