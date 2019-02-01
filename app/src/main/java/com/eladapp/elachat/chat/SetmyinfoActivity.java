package com.eladapp.elachat.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eladapp.elachat.R;

import org.ela.Carrier.Chatcarrier;
import org.elastos.carrier.UserInfo;

public class SetmyinfoActivity extends AppCompatActivity {
    private Button btn_save_myinfo;
    private EditText edtnickname;
    private UserInfo myinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setmyinfo);
        initView();
        final Chatcarrier chatcarrier = new Chatcarrier();
        myinfo = chatcarrier.getmyinfo();
        String myusername = myinfo.getName();
        if(myusername.equals("")){
            edtnickname.setText("我的昵称");
        }else{
            edtnickname.setText(myusername);
        }
        btn_save_myinfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String nickname = edtnickname.getText().toString();
                if(nickname.equals("")){
                    Toast.makeText(getApplicationContext(), "昵称不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    chatcarrier.updatemyinfo(nickname,"");
                    Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void initView() {
        edtnickname = (EditText) findViewById(R.id.edt_nickname);
        btn_save_myinfo = (Button) findViewById(R.id.btn_save_myinfo);
    }
    public void back(View view){
        finish();
    }
}