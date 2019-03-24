package com.eladapp.elachat.mysetting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eladapp.elachat.R;
import com.eladapp.elachat.db.Db;
import com.eladapp.elachat.utils.StreamTools;
import com.eladapp.elachat.walletspv.AssetpayActivity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import android.util.Base64;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import android.os.Handler;
import android.os.Message;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.ela.Carrier.Chatcarrier;
import org.elastos.carrier.UserInfo;
import org.elastos.carrier.exceptions.SystemException;
import org.json.JSONException;

public class DappdidinfoActivity extends AppCompatActivity {
    private String dappid;
    private String didid;
    private String dididnet;
    private Db db;
    private Map<String, String> dappinfofromdb;
    private TextView did_pubkey;
    private TextView did;
    private TextView did_prvkey;
    Handler handler;
    private ImageView didcopy;
    private ImageView didpubkeycopy;
    private ImageView didprvkeycopy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dappdidinfo);
        initview();
        dappid = this.getIntent().getStringExtra("dappid");
        didid = this.getIntent().getStringExtra("didid");
        handler=new Handler();
        db = new Db();
        if(didid.equals("1")){
            //显示数据库中的数据
            did_prvkey.setVisibility(View.GONE);
            didprvkeycopy.setVisibility(View.GONE);
            dappinfofromdb = getdappinfofromdb(dappid);
            did.setText("DID:"+dappinfofromdb.get("did"));
            did_pubkey.setText("PublicKey:"+dappinfofromdb.get("pubkey"));
        }else if(didid.equals("0")){
            //第一次获取DID信息
            createdid();
        }
        didcopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager myClipboard;
                ClipData myClip;
                myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                myClip = ClipData.newPlainText("text", did.getText().toString());
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(DappdidinfoActivity.this,"did已复制地址.", Toast.LENGTH_SHORT).show();
                /*if(getlangconfig().equals("cn")){
                    Toast.makeText(AssetpayActivity.this,"已复制地址.", Toast.LENGTH_SHORT).show();
                }else if(getlangconfig().equals("en")){
                    Toast.makeText(AssetpayActivity.this,"Copied address.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AssetpayActivity.this,"已复制地址.", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
        didpubkeycopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager myClipboard;
                ClipData myClip;
                myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                myClip = ClipData.newPlainText("text", did_pubkey.getText().toString());
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(DappdidinfoActivity.this,"did公钥信息已复制地址.", Toast.LENGTH_SHORT).show();
                /*if(getlangconfig().equals("cn")){
                    Toast.makeText(AssetpayActivity.this,"已复制地址.", Toast.LENGTH_SHORT).show();
                }else if(getlangconfig().equals("en")){
                    Toast.makeText(AssetpayActivity.this,"Copied address.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AssetpayActivity.this,"已复制地址.", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
        didprvkeycopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager myClipboard;
                ClipData myClip;
                myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                myClip = ClipData.newPlainText("text", did_prvkey.getText().toString());
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(DappdidinfoActivity.this,"did私钥信息已复制地址.", Toast.LENGTH_SHORT).show();
                /*if(getlangconfig().equals("cn")){
                    Toast.makeText(AssetpayActivity.this,"已复制地址.", Toast.LENGTH_SHORT).show();
                }else if(getlangconfig().equals("en")){
                    Toast.makeText(AssetpayActivity.this,"Copied address.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AssetpayActivity.this,"已复制地址.", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }
    public void initview(){
        did = (TextView)findViewById(R.id.did);
        did_pubkey = (TextView)findViewById(R.id.did_pubkey);
        did_prvkey = (TextView)findViewById(R.id.did_prvkey);
        didcopy = (ImageView)findViewById(R.id.did_copy);
        didpubkeycopy = (ImageView)findViewById(R.id.didpubkey_copy);
        didprvkeycopy = (ImageView)findViewById(R.id.didprvkey_copy);
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
                dididnet = getwebinfo(finalUrls);
                handler.post(runnableUi);
            }
        }.start();
    }

    Runnable  runnableUi=new  Runnable(){
        @Override
        public void run() {
            JSONObject jsonobject = JSONObject.fromObject(dididnet);
            did.setText("DID:"+jsonobject.get("DID").toString());
            did_prvkey.setText("PrvateKey:"+jsonobject.get("DidPrivateKey").toString());
            did_pubkey.setText("PublicKey:"+jsonobject.get("DidPublicKey").toString());
            db.updatedappdidinfo(dappid,jsonobject.get("DID").toString(),jsonobject.get("DidPublicKey").toString());
        }
    };
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
    //从数据库获取did信息
    public Map<String, String> getdappinfofromdb(String appid){
        return db.dappinfo(appid);
    }
    //从网络获取DID信息，第一次获取
    public void back(View view){
        finish();
    }
}