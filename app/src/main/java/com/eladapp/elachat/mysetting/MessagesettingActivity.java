package com.eladapp.elachat.mysetting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.eladapp.elachat.R;

public class MessagesettingActivity extends AppCompatActivity{
    private Switch messagesettingval;
    @Override
    protected void onCreate(Bundle saveInstanceStatus){
        super.onCreate(saveInstanceStatus);
        setContentView(R.layout.activity_message_setting);
        initview();
        messagesettingval.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    System.out.println("选中");
                    //SettingPre.setSwitchFiled(isChecked);
                }else{
                    System.out.println("取消");
                    //  SettingPre.setSwitchFiled(isChecked);
                }
            }
        });
    }
    public void initview(){
        messagesettingval = (Switch)findViewById(R.id.messagesettingval);
    }
    public void back(View view){
        finish();
    }
}