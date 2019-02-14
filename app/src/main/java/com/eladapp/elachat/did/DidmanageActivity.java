package com.eladapp.elachat.did;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eladapp.elachat.R;
import com.eladapp.elachat.application.CloudchatApp;
import com.eladapp.elachat.db.Db;
import com.elastos.spvcore.IDid;
import com.elastos.spvcore.IDidManager;
import com.elastos.spvcore.IMasterWallet;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import net.sf.json.JSONObject;

import org.ela.Carrier.Chatcarrier;
import org.elastos.carrier.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class DidmanageActivity extends AppCompatActivity {
    private CloudchatApp cloudchatapp;
    private IDidManager didmanager;
    private String didid;
    private Db db;
    private TextView didname;
    private TextView nickname;
    private TextView useradr;
    private TextView walletadr;
    private ImageView authordid;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_didmanager);
        didname = (TextView)findViewById(R.id.did_name);
        nickname = (TextView)findViewById(R.id.usernickname);
        useradr = (TextView)findViewById(R.id.useraddr);
        walletadr = (TextView)findViewById(R.id.walletaddr);
        authordid = (ImageView)findViewById(R.id.authordid_sancode);
        handler=new Handler();
        db = new Db();
        authordid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(DidmanageActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt("Please Scan To grant authorization"); //底部的提示文字，设为""可以置空
                integrator.setCameraId(0); //前置或者后置摄像头
                integrator.setBeepEnabled(false); //扫描成功的「哔哔」声，默认开启
                integrator.setBarcodeImageEnabled(true);//是否保留扫码成功时候的截图
                integrator.initiateScan();
            }
        });
        JSONArray didinfo = db.getdidinfo();
        if(didinfo.toString().equals("[]")){
            createdid();
        }else{
            try {
                JSONObject jsonobjects = JSONObject.fromObject(didinfo.get(0).toString());
                didname.setText(jsonobjects.get("did").toString());
                Chatcarrier chatcarrier = new Chatcarrier();
                UserInfo myinfo = chatcarrier.getmyinfo();
                if(myinfo.getName().equals(jsonobjects.get("nickname").toString())){
                    nickname.setText(" "+jsonobjects.get("nickname").toString());
                }else{
                    nickname.setText(" "+myinfo.getName().toString());
                }
                useradr.setText(" "+jsonobjects.get("useradr").toString());
                walletadr.setText(" "+jsonobjects.get("walletadr").toString());
            } catch (JSONException e) {
                e.getMessage();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String result = scanResult.getContents();
            //判断授权是否合法

        }
    }
    //创建DID
    public void  createdid(){
        long l = System.currentTimeMillis();
        String url = "http://203.189.235.252:8080/trucks/createdid.jsp?id="+l;
        URL urls = null;
        try {
            urls = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //getwebinfo(urls);
        final URL finalUrls = urls;
        new Thread(){
            public void run(){
                didid = getwebinfo(finalUrls);
                handler.post(runnableUi);
            }
        }.start();
    }

    private String getwebinfo(URL url) {
        String contentes = "";
        try {
            //1,找水源--创建URL
            //URL url = new URL("https://www.baidu.com/");//放网站
            //2,开水闸--openConnection
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //httpURLConnection.getResponseMessage();
            //3，建管道--InputStream
            InputStream inputStream = null;
            int code = httpURLConnection.getResponseCode();
            if (code == 200) {
                inputStream = httpURLConnection.getInputStream();
            } else {
                inputStream = httpURLConnection.getErrorStream(); // 得到网络返回的输入流
            }

            //4，建蓄水池蓄水-InputStreamReader
            InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
            //5，水桶盛水--BufferedReader
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuffer buffer = new StringBuffer();
            String temp = null;

            while ((temp = bufferedReader.readLine()) != null) {
                //取水--如果不为空就一直取
                buffer.append(temp);
            }
            bufferedReader.close();//记得关闭
            reader.close();
            inputStream.close();
            try {
               org.json .JSONObject jsonobja = new  org.json .JSONObject(buffer.toString());
                 /*org.json .JSONObject jsonobjb = new  org.json .JSONObject(jsonobja.get("tick").toString());
                */
                contentes = jsonobja.toString();
                System.out.println("内容："+jsonobja.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("MAIN",buffer.toString());//打印结果

        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("链接错误："+e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("异常错误："+e.getMessage());
        }
        return contentes;
    }
    public void back(View view){
        finish();
    }
    //获取地址
    public String getassetadr(){
        cloudchatapp = new CloudchatApp();
        ArrayList<IMasterWallet> Fmastwallet = cloudchatapp.getwalletlist();
        String adrinfo = Fmastwallet.get(0).GetSubWallet("ELA").GetAllAddress(0,1).toString();
        JSONObject jsonobj = JSONObject.fromObject(adrinfo);
        net.sf.json.JSONArray jsonobja = net.sf.json.JSONArray.fromObject(jsonobj.get("Addresses"));
        return String.valueOf(jsonobja.get(0));
    }
    Runnable  runnableUi=new  Runnable(){
        @Override
        public void run() {
            JSONObject jsonobject = JSONObject.fromObject(didid);
            Chatcarrier chatcarrier = new Chatcarrier();
            UserInfo myinfo = chatcarrier.getmyinfo();
            db.adddid(jsonobject.get("DID").toString(),jsonobject.get("DidPrivateKey").toString(),jsonobject.get("DidPublicKey").toString(),myinfo.getUserId(),myinfo.getName(),getassetadr());
            didname.setText(jsonobject.get("DID").toString());
            nickname.setText(myinfo.getName());
            useradr.setText(myinfo.getUserId());
            walletadr.setText(getassetadr());
        }
    };
}