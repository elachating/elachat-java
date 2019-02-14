package com.eladapp.elachat.did;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eladapp.elachat.R;
import com.eladapp.elachat.db.Db;
import com.eladapp.elachat.utils.StreamTools;

import net.sf.json.JSONObject;

import org.ela.Carrier.Chatcarrier;
import org.elastos.carrier.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class DidAuthorActivity extends AppCompatActivity{
    private TextView didname;
    private TextView nickname;
    private TextView useradr;
    private TextView walletadr;
    private TextView author_didpubkey;
    private TextView author_didprvkey;
    private Button refusebtn;
    private Db db;
    private Button authorbtn;
    private String callbackurl = "";
    private String didid = "";
    private String appid = "";
    private String appname = "";
    private String sign = "";
    private String appcate = "";
    private String state = "";
    private String pubkey = "";
    private Boolean isdid;
    private TextView author_mid_tip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authordid);
        didname = (TextView)findViewById(R.id.author_did_name);
        nickname = (TextView)findViewById(R.id.author_usernickname);
        useradr = (TextView)findViewById(R.id.author_useraddr);
        walletadr = (TextView)findViewById(R.id.author_walletaddr);
        author_didpubkey = (TextView)findViewById(R.id.author_didpubkey);
        author_didprvkey = (TextView)findViewById(R.id.author_didprvkey);
        refusebtn = (Button) findViewById(R.id.author_refuse_btn);
        authorbtn = (Button) findViewById(R.id.author_btn);
        author_mid_tip = (TextView)findViewById(R.id.author_mid_tip);
        //handler=new Handler();
        db = new Db();
        Uri uridata = this.getIntent().getData();
        /**
         *  形式上为：elachatapp://identity?callbackurl=&did=&appid=&appname=&sign=&cate=&pubkey=&state=
         */
        String authorurl = uridata.toString();
        String [] urla = authorurl.split("\\?");
        String [] urlb = urla[1].split("&");
        //获取回调地址
        String [] callbackurlarr = urlb[0].split("=");
         callbackurl = URLDecoder.decode(callbackurlarr[1].toString());
        //获取DID信息
        String [] didarr = urlb[1].split("=");
         didid = didarr[1].toString();
        //获取应用信息
        String [] appidarr = urlb[2].split("=");
         appid = appidarr[1].toString();
        //获取应用名称
        String [] appnamearr = urlb[3].split("=");
         appname = appnamearr[1].toString();
        //获取签名信息
        String [] signarr = urlb[4].split("=");
         sign = signarr[1].toString();
        //获取应用类型
        String [] appcatearr = urlb[5].split("=");
         appcate = appcatearr[1].toString();
        //获取公钥信息
        String [] pubkeyarr = urlb[6].split("=");
        pubkey = pubkeyarr[1].toString();
        //获取自定义参数值
        String [] statearr = urlb[7].split("=");
        state = statearr[1].toString();

        if(getlangconfig().equals("cn")){
            author_mid_tip.setText("该网页由"+author_mid_tip+"开发，向其提供以下权限");
        }else if(getlangconfig().equals("en")){
            author_mid_tip.setText("The website is developed by "+author_mid_tip+",Provide it with the following permissions.");
        }else{
            author_mid_tip.setText("该网页由"+author_mid_tip+"开发，向其提供以下权限");
        }
        System.out.println("参数内容："+uridata.toString());
        JSONArray didinfo = db.getdidinfo();
        if(didinfo.toString().equals("[]")){
           //
        }else{
            try {
                JSONObject jsonobjects = JSONObject.fromObject(didinfo.get(0).toString());
                System.out.println("字符串："+didinfo.get(0).toString());

                didname.setText(jsonobjects.get("did").toString());
                Chatcarrier chatcarrier = new Chatcarrier();
                UserInfo myinfo = chatcarrier.getmyinfo();
                if(myinfo.getName().equals(jsonobjects.get("nickname").toString())){
                    nickname.setText(jsonobjects.get("nickname").toString());
                }else{
                    nickname.setText(myinfo.getName().toString());
                }
                useradr.setText(jsonobjects.get("useradr").toString());
                walletadr.setText(jsonobjects.get("walletadr").toString());
                author_didpubkey.setText(jsonobjects.get("pubkey").toString());
                author_didprvkey.setText(jsonobjects.get("prvkey").toString());
            } catch (JSONException e) {
                e.getMessage();
            }
        }
        /*
        if(didname.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "请到我的DID管理设置创建DID!", Toast.LENGTH_SHORT).show();
        }
        if(nickname.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "请到好友列表设置自己的昵称!", Toast.LENGTH_SHORT).show();
        }
        if(walletadr.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "您还没创建资产，请到资产界面创建资产!", Toast.LENGTH_SHORT).show();
        }*/
        authorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //构建POST，推送消息到指定网站
                String author_app_didpubkey = author_didpubkey.getText().toString();

                if(getlangconfig().equals("cn")){
                    if(didname.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "请到我的DID管理设置创建DID!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(nickname.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "请到好友列表设置自己的昵称!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(walletadr.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "您还没创建资产，请到资产界面创建资产!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //首先检测必填参数是否为空
                    if(callbackurl.equals("")){
                        Toast.makeText(getApplicationContext(), "回调地址为空,不能授权!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(didid.equals("")){
                        Toast.makeText(getApplicationContext(), "参数DID为空,不能授权!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(appid.equals("")){
                        Toast.makeText(getApplicationContext(), "参数APPID为空,不能授权!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(appname.equals("")){
                        Toast.makeText(getApplicationContext(), "参数appname为空,不能授权!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(sign.equals("")){
                        Toast.makeText(getApplicationContext(), "参数sign为空,不能授权!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(appcate.equals("")){
                        Toast.makeText(getApplicationContext(), "参数appcate为空,不能授权!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(pubkey.equals("")){
                        Toast.makeText(getApplicationContext(), "参数pubkey为空,不能授权!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }else if(getlangconfig().equals("en")){
                    if(didname.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Please go to my DID management settings to create DID.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(nickname.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Please set up your nickname on your friends list.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(walletadr.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "You haven't created an asset yet. Go to the assets to create an asset.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //首先检测必填参数是否为空
                    if(callbackurl.equals("")){
                        Toast.makeText(getApplicationContext(), "Callback address is empty and cannot be authorized", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(didid.equals("")){
                        Toast.makeText(getApplicationContext(), "The parameter DID is empty and cannot be authorized.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(appid.equals("")){
                        Toast.makeText(getApplicationContext(), "The parameter APPID is empty and cannot be authorized.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(appname.equals("")){
                        Toast.makeText(getApplicationContext(), "The parameter appname is empty and cannot be authorized.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(sign.equals("")){
                        Toast.makeText(getApplicationContext(), "The parameter sign is empty and cannot be authorized.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(appcate.equals("")){
                        Toast.makeText(getApplicationContext(), "The parameter appcate is empty and cannot be authorized.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(pubkey.equals("")){
                        Toast.makeText(getApplicationContext(), "The parameter pubkey is empty and cannot be authorized.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else{
                    if(didname.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "请到我的DID管理设置创建DID!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(nickname.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "请到好友列表设置自己的昵称!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(walletadr.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "您还没创建资产，请到资产界面创建资产!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //首先检测必填参数是否为空
                    if(callbackurl.equals("")){
                        Toast.makeText(getApplicationContext(), "回调地址为空,不能授权!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(didid.equals("")){
                        Toast.makeText(getApplicationContext(), "参数DID为空,不能授权!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(appid.equals("")){
                        Toast.makeText(getApplicationContext(), "参数APPID为空,不能授权!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(appname.equals("")){
                        Toast.makeText(getApplicationContext(), "参数appname为空,不能授权!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(sign.equals("")){
                        Toast.makeText(getApplicationContext(), "参数sign为空,不能授权!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(appcate.equals("")){
                        Toast.makeText(getApplicationContext(), "参数appcate为空,不能授权!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(pubkey.equals("")){
                        Toast.makeText(getApplicationContext(), "参数pubkey为空,不能授权!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //然后验证签名
                String url = "http://203.189.235.252:8080/trucks/verifydid.jsp?didpubkey="+pubkey+"&sig="+sign+"&msg="+didid;

                URL urls = null;
                try {
                    urls = new URL(url);
                    getwebinfo(urls);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        refusebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拒绝授权
                finish();
            }
        });
    }
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            String author_app_didprvkey = author_didprvkey.getText().toString();
            System.out.println("请求的："+msg.obj);
            if(msg.obj.equals("checksign")){
               if(msg.what==1){
                   Bundle b = msg.getData();
                   String res = b.getString("res");
                   if(res.equals("0")){
                       if(getlangconfig().equals("cn")){
                           Toast.makeText(getApplicationContext(), "验证签名失败,不能授权!", Toast.LENGTH_SHORT).show();
                       }else if(getlangconfig().equals("en")){
                           Toast.makeText(getApplicationContext(), "Validation signature failed and cannot be authorized.", Toast.LENGTH_SHORT).show();
                       }else{
                           Toast.makeText(getApplicationContext(), "验证签名失败,不能授权!", Toast.LENGTH_SHORT).show();
                       }
                   }else{
                       String url = "http://203.189.235.252:8080/trucks/signdid.jsp?didprvkey="+author_app_didprvkey+"&msg="+didid;
                       URL urls = null;
                       try {
                           urls = new URL(url);
                           getwebsigninfo(urls);
                       } catch (MalformedURLException e) {
                           e.printStackTrace();
                       }
                   }
                }else{
                   if(getlangconfig().equals("cn")){
                       Toast.makeText(getApplicationContext(), "服务器连接失败,不能授权!", Toast.LENGTH_SHORT).show();
                   }else if(getlangconfig().equals("en")){
                       Toast.makeText(getApplicationContext(), "Server connection failed and cannot be authorized.", Toast.LENGTH_SHORT).show();
                   }else{
                       Toast.makeText(getApplicationContext(), "服务器连接失败,不能授权!", Toast.LENGTH_SHORT).show();
                   }
               }
            }else if(msg.obj.equals("didsign")){
                if(msg.what==1){
                    Bundle b = msg.getData();
                    String res = b.getString("res").replaceAll(" ", "");
                    if(res.equals("")){
                        if(getlangconfig().equals("cn")){
                            Toast.makeText(getApplicationContext(), "获取签名失败,不能授权!", Toast.LENGTH_SHORT).show();
                        }else if(getlangconfig().equals("en")){
                            Toast.makeText(getApplicationContext(), "Failed to obtain signature, not authorized.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "获取签名失败,不能授权!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        sendpostmsg(res);
                    }
                }else{
                    if(getlangconfig().equals("cn")){
                        Toast.makeText(getApplicationContext(), "服务器连接失败,不能授权!", Toast.LENGTH_SHORT).show();
                    }else if(getlangconfig().equals("en")){
                        Toast.makeText(getApplicationContext(), "Server connection failed and cannot be authorized.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "服务器连接失败,不能授权!", Toast.LENGTH_SHORT).show();
                    }
                }
            }else if(msg.obj.equals("pushappinfo")){
                if(msg.what==1){
                    Bundle b = msg.getData();
                    String res = b.getString("res").replaceAll(" ", "");
                    if(res.equals("1")){
                        finish();
                    }else{
                        if(getlangconfig().equals("cn")){
                            Toast.makeText(getApplicationContext(), "授权失败!", Toast.LENGTH_SHORT).show();
                        }else if(getlangconfig().equals("en")){
                            Toast.makeText(getApplicationContext(), "privilege grant failed", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "授权失败!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    if(getlangconfig().equals("cn")) {
                        Toast.makeText(getApplicationContext(), "服务器连接失败,不能授权!", Toast.LENGTH_SHORT).show();
                    }else if(getlangconfig().equals("en")){
                        Toast.makeText(getApplicationContext(), "Server connection failed and cannot be authorized.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "服务器连接失败,不能授权!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    };


    //请求验证签名
    private void getwebinfo(URL url) {
        new Thread() {
            public void run() {
                try{
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
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
                        System.out.println("结果："+buffer.toString());
                        Message msg = Message.obtain();
                        Bundle  bundle = new Bundle();
                        bundle.putString("res", buffer.toString());
                        msg.setData(bundle);
                        msg.what = 1;
                        msg.obj = "checksign";
                        handler.sendMessage(msg);
                } catch (MalformedURLException e){
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj = "checksign";
                    handler.sendMessage(msg);
                } catch (IOException e){
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj = "checksign";
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    //请求验证签名
    private void getwebsigninfo(URL url) {
        new Thread() {
            public void run() {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = null;
                    int code = httpURLConnection.getResponseCode();
                    if (code == 200) {
                        inputStream = httpURLConnection.getInputStream();
                    } else {
                        inputStream = httpURLConnection.getErrorStream(); // 得到网络返回的输入流
                    }
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    StringBuffer buffer = new StringBuffer();
                    String temp = null;
                    while ((temp = bufferedReader.readLine()) != null) {
                        buffer.append(temp);
                    }
                    bufferedReader.close();//记得关闭
                    reader.close();
                    inputStream.close();
                        Message msg = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putString("res",buffer.toString());
                        msg.setData(bundle);
                        msg.what = 1;
                        msg.obj = "didsign";
                        handler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj = "didsign";
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj = "didsign";
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    //构建POST方法推送到指定授权网站
    public void sendpostmsg(String addsign){
        String author_app_nickname = nickname.getText().toString();
        String author_app_walletadr = walletadr.getText().toString();
        String author_app_useradr = useradr.getText().toString();
        String author_app_did = didname.getText().toString();
        String author_app_didpubkey = author_didpubkey.getText().toString();
        new Thread() {
            public void run() {
                try {
                    URL url = new URL(callbackurl);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(5000);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    String data = "{\"did\":\""+ URLEncoder.encode(author_app_did,"utf-8")  +"\",\"nickname\":\""+ URLEncoder.encode(author_app_nickname,"utf-8") +"\",\"useradr\":\""+ URLEncoder.encode(author_app_useradr,"utf-8") +"\",\"didpubkey\":\""+ URLEncoder.encode(author_app_didpubkey,"utf-8") +"\",\"walletadr\":\""+ URLEncoder.encode(author_app_walletadr,"utf-8") +"\",\"sign\":\""+ URLEncoder.encode(addsign,"utf-8") +"\",\"state\":\""+ state +"\"}";
                    //String data = "did="+ URLEncoder.encode(author_app_did,"utf-8")+"&nickname="+ URLEncoder.encode(author_app_nickname,"utf-8")+"&useradr=" + URLEncoder.encode(author_app_useradr,"utf-8")+"&didpubkey=" + URLEncoder.encode(author_app_didpubkey,"utf-8")+"&walletadr=" + URLEncoder.encode(author_app_walletadr,"utf-8")+"&sign=" + URLEncoder.encode(addsign,"utf-8");
                    conn.setRequestProperty("Content-Length",String.valueOf(data.length()));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(data.getBytes());
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream in = conn.getInputStream();
                        // 把inputstream转换成字符串
                        String content = StreamTools.readString(in);

                        System.out.println("推送成功！");
                        Message msg = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putString("res",content.toString());
                        msg.setData(bundle);
                        msg.what = 1;
                        msg.obj = "pushappinfo";
                        handler.sendMessage(msg);
                    } else {
                        Message msg = Message.obtain();
                        msg.what = 0;
                        msg.obj = "pushappinfo";
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println("错误："+e.getMessage());
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj = "pushappinfo";
                    handler.sendMessage(msg);
                }
            }
        }.start();
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

