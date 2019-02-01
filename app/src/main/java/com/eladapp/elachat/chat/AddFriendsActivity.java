package com.eladapp.elachat.chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.eladapp.elachat.R;

import org.ela.Carrier.Chatcarrier;
import org.elastos.carrier.Carrier;
import org.elastos.carrier.exceptions.CarrierException;

public class AddFriendsActivity extends AppCompatActivity{
    private String TAG="AddFriendsActivity";
    private Carrier mycarrier;
    private EditText edt_addfriend;
    private Button btn_addfriend;
    private EditText edt_checkMessage;
    private ImageView btnsan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        edt_addfriend = (EditText) findViewById(R.id.edt_addfriend);
        btn_addfriend = (Button) findViewById(R.id.btn_addfriend);
        edt_checkMessage = (EditText) findViewById(R.id.edt_checkMessage);
        btnsan = (ImageView) findViewById(R.id.btn_sancode);
        mycarrier = Carrier.getInstance();
        btn_addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_addfriend.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "请填写好友地址", Toast.LENGTH_SHORT).show();
                }else{
                    addfriend();
                }
            }
        });
        btnsan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(AddFriendsActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt("请扫码加好友"); //底部的提示文字，设为""可以置空
                integrator.setCameraId(0); //前置或者后置摄像头
                integrator.setBeepEnabled(false); //扫描成功的「哔哔」声，默认开启
                integrator.setBarcodeImageEnabled(true);//是否保留扫码成功时候的截图
                integrator.initiateScan();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String result = scanResult.getContents();
            edt_addfriend = (EditText) findViewById(R.id.edt_addfriend);
            edt_addfriend.setText(result);
        }
    }
    /**
     *
     * 加好友
     *
     */
    private void addfriend(){
        String address = edt_addfriend.getText().toString();
        String checkMessage = edt_checkMessage.getText().toString();
        Chatcarrier chatcarrier = new Chatcarrier();
        try{
            String rsa = chatcarrier.applyfriend(address,checkMessage);
            if( rsa=="valid"){
                Toast.makeText(getApplicationContext(), "无效的地址", Toast.LENGTH_SHORT).show();
            }else if(rsa=="succfully"){
                Toast.makeText(getApplicationContext(), "好友请求发送成功", Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(1000);
                    finish();
                } catch (InterruptedException e) {
                }
            }else{
                Toast.makeText(getApplicationContext(), "好友已添加,不能重复添加", Toast.LENGTH_SHORT).show();
                finish();
            }
        }catch( CarrierException e ){
            e.printStackTrace();
        }
    }
    public void back(View view){
        finish();
    }
}
