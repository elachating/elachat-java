package com.eladapp.elachat.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import com.eladapp.elachat.R;
import org.ela.Carrier.Chatcarrier;
import org.elastos.carrier.Carrier;
import org.elastos.carrier.UserInfo;
import org.elastos.carrier.exceptions.CarrierException;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.os.Messenger;
import com.eladapp.elachat.utils.ZXingUtils;

public class MyInfoActivity extends AppCompatActivity {
    private TextView myuserid;
    private TextView myaddress;
    private TextView username;
    private Button btn_msg;
    private ImageView ivTwoCode;
    private Messenger messenger;
    private UserInfo myinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        initView();
        Chatcarrier chatcarrier = new Chatcarrier();
        myinfo = chatcarrier.getmyinfo();
        Carrier carrierinstance = chatcarrier.carrierinstance();
        String myuserida = myinfo.getUserId();
        String myusername = myinfo.getName();
        try{
            String myaddressa = carrierinstance.getAddress();
            if(myusername.equals("")){
                username.setText("我的昵称");
            }else{
                username.setText(myusername);
            }
            myuserid.setText(myuserida);
            myaddress.setText(myaddressa);
            Bitmap bitmap = ZXingUtils.createQRImage(myaddressa, 200, 200);
            ivTwoCode.setImageBitmap(bitmap);
        }catch (CarrierException e){
            e.printStackTrace();;
        }
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyInfoActivity.this, SetmyinfoActivity.class));
            }
        });
    }
    public  void onResume() {
        super.onResume();
        Chatcarrier chatcarrier = new Chatcarrier();
        myinfo = chatcarrier.getmyinfo();
        String myusername = myinfo.getName();
        if(myusername.equals("")){
            username.setText("我的昵称");
        }else{
            username.setText(myusername);
        }
    }
    private void initView() {
        username = (TextView) findViewById(R.id.nick_name);
        myuserid = (TextView) findViewById(R.id.myuserid);
        myaddress = (TextView) findViewById(R.id.myaddress);
        ivTwoCode = (ImageView) findViewById(R.id.iv_ercode);
    }
    public void back(View view){
        finish();
    }
}
