package com.eladapp.elachat.mysetting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eladapp.elachat.R;
import com.eladapp.elachat.dapps.DappmainActivity;
import com.eladapp.elachat.db.Db;
import com.eladapp.elachat.utils.CommonDialog;
import com.eladapp.elachat.utils.StreamTools;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DappmenusetActivity extends AppCompatActivity{
    ImageView dappmenu_next_btn;
    ImageView dappmenu_prv_btn;
    ImageView dappmenu_save_btn;

    ImageView dappinfo_mainmenu_add_one;
    ImageView dappinfo_mainmenu_add_tow;
    ImageView dappinfo_mainmenu_add_three;

    EditText dappinfo_mainmenu_one;
    EditText dappinfo_mainmenu_tow;
    EditText dappinfo_mainmenu_three;

    EditText dappinfo_submenu_one_one;
    EditText dappinfo_submenu_one_one_url;
    EditText dappinfo_submenu_one_tow;
    EditText dappinfo_submenu_one_tow_url;
    EditText dappinfo_submenu_one_three;
    EditText dappinfo_submenu_one_three_url;
    EditText dappinfo_submenu_one_four;
    EditText dappinfo_submenu_one_four_url;
    EditText dappinfo_submenu_one_five;
    EditText dappinfo_submenu_one_five_url;


    EditText dappinfo_submenu_tow_one;
    EditText dappinfo_submenu_tow_one_url;
    EditText dappinfo_submenu_tow_tow;
    EditText dappinfo_submenu_tow_tow_url;
    EditText dappinfo_submenu_tow_three;
    EditText dappinfo_submenu_tow_three_url;
    EditText dappinfo_submenu_tow_four;
    EditText dappinfo_submenu_tow_four_url;
    EditText dappinfo_submenu_tow_five;
    EditText dappinfo_submenu_tow_five_url;

    EditText dappinfo_submenu_three_one;
    EditText dappinfo_submenu_three_one_url;
    EditText dappinfo_submenu_three_tow;
    EditText dappinfo_submenu_three_tow_url;
    EditText dappinfo_submenu_three_three;
    EditText dappinfo_submenu_three_three_url;
    EditText dappinfo_submenu_three_four;
    EditText dappinfo_submenu_three_four_url;
    EditText dappinfo_submenu_three_five;
    EditText dappinfo_submenu_three_five_url;

    LinearLayout dappmenu_one;
    LinearLayout dappmenu_tow;
    LinearLayout dappmenu_three;
    LinearLayout dappinfo_submenu_one_tow_layout;
    LinearLayout dappinfo_submenu_one_three_layout;
    LinearLayout dappinfo_submenu_one_four_layout;
    LinearLayout dappinfo_submenu_one_five_layout;
    LinearLayout dappinfo_submenu_one_tow_url_layout;
    LinearLayout dappinfo_submenu_one_three_url_layout;
    LinearLayout dappinfo_submenu_one_four_url_layout;
    LinearLayout dappinfo_submenu_one_five_url_layout;

    LinearLayout dappinfo_submenu_tow_tow_layout;
    LinearLayout dappinfo_submenu_tow_three_layout;
    LinearLayout dappinfo_submenu_tow_four_layout;
    LinearLayout dappinfo_submenu_tow_five_layout;
    LinearLayout dappinfo_submenu_tow_tow_url_layout;
    LinearLayout dappinfo_submenu_tow_three_url_layout;
    LinearLayout dappinfo_submenu_tow_four_url_layout;
    LinearLayout dappinfo_submenu_tow_five_url_layout;

    LinearLayout dappinfo_submenu_three_tow_layout;
    LinearLayout dappinfo_submenu_three_three_layout;
    LinearLayout dappinfo_submenu_three_four_layout;
    LinearLayout dappinfo_submenu_three_five_layout;
    LinearLayout dappinfo_submenu_three_tow_url_layout;
    LinearLayout dappinfo_submenu_three_three_url_layout;
    LinearLayout dappinfo_submenu_three_four_url_layout;
    LinearLayout dappinfo_submenu_three_five_url_layout;


    static int page = 1;
    static int a = 1;
    static int b = 1;
    static int c = 1;
    String dappid;
    private Db db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dappmenulist);
        dappid = this.getIntent().getStringExtra("dappid");
        initview();
        dappmenu_save_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String menujson = "[";
                    if (dappinfo_mainmenu_one.getText().toString().equals("")) {
                        //menujson = menujson +"]";
                    } else {
                        menujson = menujson + "{\\\"name\\\":\\\"" + dappinfo_mainmenu_one.getText().toString() + "\\\",\\\"url\\\":\\\"\\\",";
                        menujson = menujson + "\\\"sub_button\\\":[";
                        if (dappinfo_submenu_one_one.getText().toString().equals("")) {
                            menujson = menujson + "]}";
                        } else {
                            menujson = menujson + "{\\\"type\\\":\\\"view\\\",\\\"name\\\":\\\"" + dappinfo_submenu_one_one.getText().toString() + "\\\",\\\"url\\\":\\\"" + dappinfo_submenu_one_one_url.getText().toString() + "\\\"}";
                            if (dappinfo_submenu_one_tow.getText().toString().equals("")) {
                                menujson = menujson + "]}";
                            } else {
                                menujson = menujson + ",{\\\"type\\\":\\\"view\\\",\\\"name\\\":\\\"" + dappinfo_submenu_one_tow.getText().toString() + "\\\",\\\"url\\\":\\\"" + dappinfo_submenu_one_tow_url.getText().toString() + "\\\"}";

                                if (dappinfo_submenu_one_three.getText().toString().equals("")) {
                                    menujson = menujson + "]}";
                                } else {
                                    menujson = menujson + ",{\\\"type\\\":\\\"view\\\",\\\"name\\\":\\\"" + dappinfo_submenu_one_three.getText().toString() + "\\\",\\\"url\\\":\\\"" + dappinfo_submenu_one_three_url.getText().toString() + "\\\"}";

                                    if (dappinfo_submenu_one_four.getText().toString().equals("")) {
                                        menujson = menujson + "]}";
                                    } else {
                                        menujson = menujson + ",{\\\"type\\\":\\\"view\\\",\\\"name\\\":\\\"" + dappinfo_submenu_one_four.getText().toString() + "\\\",\\\"url\\\":\\\"" + dappinfo_submenu_one_four_url.getText().toString() + "\\\"}";

                                        if (dappinfo_submenu_one_five.getText().toString().equals("")) {
                                            menujson = menujson + "]}";
                                        } else {
                                            menujson = menujson + ",{\\\"type\\\":\\\"view\\\",\\\"name\\\":\\\"" + dappinfo_submenu_one_five.getText().toString() + "\\\",\\\"url\\\":\\\"" + dappinfo_submenu_one_five_url.getText().toString() + "\\\"}";
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //第二个菜单
                    if (dappinfo_mainmenu_tow.getText().toString().equals("")) {

                    } else {
                        menujson = menujson + ",{\\\"name\\\":\\\"" + dappinfo_mainmenu_tow.getText().toString() + "\\\",\\\"url\\\":\\\"\\\",";

                        menujson = menujson + "\\\"sub_button\\\":[";
                        if (dappinfo_submenu_tow_one.getText().toString().equals("")) {
                            menujson = menujson + "]}";
                        } else {
                            menujson = menujson + "{\\\"type\\\":\\\"view\\\",\\\"name\\\":\\\"" + dappinfo_submenu_tow_one.getText().toString() + "\\\",\\\"url\\\":\\\"" + dappinfo_submenu_tow_one_url.getText().toString() + "\\\"}";

                            if (dappinfo_submenu_tow_tow.getText().toString().equals("")) {
                                menujson = menujson + "]}";
                            } else {
                                menujson = menujson + ",{\\\"type\\\":\\\"view\\\",\\\"name\\\":\\\"" + dappinfo_submenu_tow_tow.getText().toString() + "\\\",\\\"url\\\":\\\"" + dappinfo_submenu_tow_tow_url.getText().toString() + "\\\"}";

                                if (dappinfo_submenu_tow_three.getText().toString().equals("")) {
                                    menujson = menujson + "]}";
                                } else {
                                    menujson = menujson + ",{\\\"type\\\":\\\"view\\\",\\\"name\":\\\"" + dappinfo_submenu_tow_three.getText().toString() + "\\\",\\\"url\\\":\\\"" + dappinfo_submenu_tow_three_url.getText().toString() + "\\\"}";

                                    if (dappinfo_submenu_tow_four.getText().toString().equals("")) {
                                        menujson = menujson + "]}";
                                    } else {
                                        menujson = menujson + ",{\\\"type\\\":\\\"view\\\",\\\"name\\\":\\\"" + dappinfo_submenu_tow_four.getText().toString() + "\\\",\\\"url\\\":\\\"" + dappinfo_submenu_tow_four_url.getText().toString() + "\\\"}";

                                        if (dappinfo_submenu_tow_five.getText().toString().equals("")) {
                                            menujson = menujson + "]}";
                                        } else {
                                            menujson = menujson + ",{\\\"type\\\":\\\"view\\\",\\\"name\\\":\\\"" + dappinfo_submenu_tow_five.getText().toString() + "\\\",\\\"url\\\":\\\"" + dappinfo_submenu_tow_five_url.getText().toString() + "\\\"}";
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //第三个菜单

                    if (dappinfo_mainmenu_three.getText().toString().equals("")) {

                    } else {
                        menujson = menujson + ",{\\\"name\\\":\\\"" + dappinfo_mainmenu_three.getText().toString() + "\\\",\\\"url\\\":\\\"\\\",";

                        menujson = menujson + "\\\"sub_button\\\":[";
                        if (dappinfo_submenu_three_one.getText().toString().equals("")) {
                            menujson = menujson + "]}";
                        } else {
                            menujson = menujson + "{\\\"type\\\":\\\"view\\\",\\\"name\\\":\\\"" + dappinfo_submenu_three_one.getText().toString() + "\\\",\\\"url\\\":\\\"" + dappinfo_submenu_three_one_url.getText().toString() + "\\\"}";

                            if (dappinfo_submenu_three_tow.getText().toString().equals("")) {
                                menujson = menujson + "]}";
                            } else {
                                menujson = menujson + ",{\\\"type\\\":\\\"view\\\",\\\"name\\\":\\\"" + dappinfo_submenu_three_tow.getText().toString() + "\\\",\\\"url\\\":\\\"" + dappinfo_submenu_three_tow_url.getText().toString() + "\\\"}";

                                if (dappinfo_submenu_three_three.getText().toString().equals("")) {
                                    menujson = menujson + "]}";
                                } else {
                                    menujson = menujson + ",{\\\"type\\\":\\\"view\\\",\\\"name\\\":\\\"" + dappinfo_submenu_three_three.getText().toString() + "\\\",\\\"url\\\":\\\"" + dappinfo_submenu_three_three_url.getText().toString() + "\\\"}";

                                    if (dappinfo_submenu_three_four.getText().toString().equals("")) {
                                        menujson = menujson + "]}";
                                    } else {
                                        menujson = menujson + ",{\\\"type\\\":\\\"view\\\",\\\"name\\\":\\\"" + dappinfo_submenu_three_four.getText().toString() + "\\\",\\\"url\\\":\\\"" + dappinfo_submenu_three_four_url.getText().toString() + "\\\"}";

                                        if (dappinfo_submenu_three_five.getText().toString().equals("")) {
                                            menujson = menujson + "]}";
                                        } else {
                                            menujson = menujson + ",{\\\"type\\\":\\\"view\\\",\\\"name\\\":\\\"" + dappinfo_submenu_three_five.getText().toString() + "\\\",\\\"url\\\":\\\"" + dappinfo_submenu_three_five_url.getText().toString() + "\\\"}";
                                        }
                                    }
                                }
                            }
                        }
                    }
                    menujson = menujson +"]";
                    String dappurl = "http://test.eladevp.com/index.php/Home/Dapplist/updateappinfomenu";
                    updatemenu(dappid,menujson);
                    updatedappinfo(dappurl,dappid,menujson);
            }
        });
        dappmenu_prv_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(page==3){
                    dappmenu_one.setVisibility(View.GONE);
                    dappmenu_three.setVisibility(View.GONE);
                    dappmenu_tow.setVisibility(View.VISIBLE);
                    page = page-1;
                    dappmenu_next_btn.setVisibility(View.VISIBLE);
                }else if(page==2){
                    dappmenu_three.setVisibility(View.GONE);
                    dappmenu_tow.setVisibility(View.GONE);
                    dappmenu_one.setVisibility(View.VISIBLE);
                    page = page-1;
                    dappmenu_prv_btn.setVisibility(View.GONE);
                }
                System.out.println("当前："+page);
            }
        });
        dappmenu_next_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(page==1){
                    dappmenu_one.setVisibility(View.GONE);
                    dappmenu_three.setVisibility(View.GONE);
                    dappmenu_tow.setVisibility(View.VISIBLE);
                    page = page+1;
                    dappmenu_prv_btn.setVisibility(View.VISIBLE);
                }else if(page==2){
                    dappmenu_one.setVisibility(View.GONE);
                    dappmenu_tow.setVisibility(View.GONE);
                    dappmenu_three.setVisibility(View.VISIBLE);
                    page = page+1;
                    dappmenu_next_btn.setVisibility(View.GONE);
                }
                System.out.println("当前："+page);
            }
        });
        dappinfo_mainmenu_add_one.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(a==1){
                    dappinfo_submenu_one_tow_layout.setVisibility(View.VISIBLE);
                    dappinfo_submenu_one_tow_url_layout.setVisibility(View.VISIBLE);
                    a = a+1;
                }else if(a==2){
                    dappinfo_submenu_one_three_layout.setVisibility(View.VISIBLE);
                    dappinfo_submenu_one_three_url_layout.setVisibility(View.VISIBLE);
                    a = a+1;
                }else if(a==3){
                    dappinfo_submenu_one_four_layout.setVisibility(View.VISIBLE);
                    dappinfo_submenu_one_four_url_layout.setVisibility(View.VISIBLE);
                    a = a+1;
                }else if(a==4){
                    dappinfo_submenu_one_five_layout.setVisibility(View.VISIBLE);
                    dappinfo_submenu_one_five_url_layout.setVisibility(View.VISIBLE);
                    a = a+1;
                }
                System.out.println("当前："+a);
            }
        });
        dappinfo_mainmenu_add_tow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(b==1){
                    dappinfo_submenu_tow_tow_layout.setVisibility(View.VISIBLE);
                    dappinfo_submenu_tow_tow_url_layout.setVisibility(View.VISIBLE);
                    b = b+1;
                }else if(b==2){
                    dappinfo_submenu_tow_three_layout.setVisibility(View.VISIBLE);
                    dappinfo_submenu_tow_three_url_layout.setVisibility(View.VISIBLE);
                    b = b+1;
                }else if(b==3){
                    dappinfo_submenu_tow_four_layout.setVisibility(View.VISIBLE);
                    dappinfo_submenu_tow_four_url_layout.setVisibility(View.VISIBLE);
                    b = b+1;
                }else if(b==4){
                    dappinfo_submenu_tow_five_layout.setVisibility(View.VISIBLE);
                    dappinfo_submenu_tow_five_url_layout.setVisibility(View.VISIBLE);
                    b = b+1;
                }
                System.out.println("当前："+b);
            }
        });
        dappinfo_mainmenu_add_three.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(c==1){
                    dappinfo_submenu_three_tow_layout.setVisibility(View.VISIBLE);
                    dappinfo_submenu_three_tow_url_layout.setVisibility(View.VISIBLE);
                    c = c+1;
                }else if(c==2){
                    dappinfo_submenu_three_three_layout.setVisibility(View.VISIBLE);
                    dappinfo_submenu_three_three_url_layout.setVisibility(View.VISIBLE);
                    c = c+1;
                }else if(c==3){
                    dappinfo_submenu_three_four_layout.setVisibility(View.VISIBLE);
                    dappinfo_submenu_three_four_url_layout.setVisibility(View.VISIBLE);
                    c = c+1;
                }else if(c==4){
                    dappinfo_submenu_three_five_layout.setVisibility(View.VISIBLE);
                    dappinfo_submenu_three_five_url_layout.setVisibility(View.VISIBLE);
                    c = c+1;
                }
                System.out.println("当前："+c);
            }
        });
    }
    //
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            System.out.println("返回结果:"+msg.toString());
            if (msg.obj.equals("updatedappinfo")) {
                if(msg.what==1){
                    Bundle b = msg.getData();
                    String res = b.getString("res");
                    if(res.equals("1")){
                        finish();
                    }
                }else{

                }
            }
        }
    };
    //构建POST方法获取指定的DAPP信息列表
    public void updatedappinfo(String dappurl,String appid,String menujson){
        new Thread() {
            public void run() {
                try {
                    URL url = new URL(dappurl);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(5000);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    String data = "{\"dappid\":\""+ appid +"\",\"menujson\":\""+ menujson +"\"}";
                    conn.setRequestProperty("Content-Length",String.valueOf(data.getBytes().length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(data.getBytes());
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream in = conn.getInputStream();
                        String content = StreamTools.readString(in);
                        Message msg = Message.obtain();
                        Bundle bundle = new Bundle();
                        System.out.println("DAPP列表："+content.toString());
                        bundle.putString("res",content.toString());
                        msg.setData(bundle);
                        msg.what = 1;
                        msg.obj = "updatedappinfo";
                        handler.sendMessage(msg);
                    } else {
                        Message msg = Message.obtain();
                        msg.what = 0;
                        msg.obj = "updatedappinfo";
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Message msg = Message.obtain();
                    System.out.println("错误："+e.getMessage());
                    msg.what = 0;
                    msg.obj = "updatedappinfo";
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    //更新菜单
    public void updatemenu(String dappid,String menulist){
        db = new Db();
        db.updatedappmenuinfo(dappid,menulist);
    }
    public void initview(){
        dappmenu_prv_btn = (ImageView)findViewById(R.id.dappmenu_prv_btn);
        dappmenu_next_btn = (ImageView)findViewById(R.id.dappmenu_next_btn);
        dappmenu_save_btn = (ImageView)findViewById(R.id.dappmenu_save_btn);

        dappinfo_mainmenu_add_one = (ImageView)findViewById(R.id.dappinfo_mainmenu_add_one);
        dappinfo_mainmenu_add_tow = (ImageView)findViewById(R.id.dappinfo_mainmenu_add_tow);
        dappinfo_mainmenu_add_three = (ImageView)findViewById(R.id.dappinfo_mainmenu_add_three);

        dappmenu_one = (LinearLayout) findViewById(R.id.dappmenu_one);
        dappmenu_tow = (LinearLayout) findViewById(R.id.dappmenu_tow);
        dappmenu_three = (LinearLayout) findViewById(R.id.dappmenu_three);

        dappinfo_submenu_one_tow_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_one_tow_layout);
        dappinfo_submenu_one_three_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_one_three_layout);
        dappinfo_submenu_one_four_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_one_four_layout);
        dappinfo_submenu_one_five_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_one_five_layout);
        dappinfo_submenu_one_tow_url_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_one_tow_url_layout);
        dappinfo_submenu_one_three_url_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_one_three_url_layout);
        dappinfo_submenu_one_four_url_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_one_four_url_layout);
        dappinfo_submenu_one_five_url_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_one_five_url_layout);

        dappinfo_submenu_tow_tow_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_tow_tow_layout);
        dappinfo_submenu_tow_three_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_tow_three_layout);
        dappinfo_submenu_tow_four_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_tow_four_layout);
        dappinfo_submenu_tow_five_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_tow_five_layout);
        dappinfo_submenu_tow_tow_url_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_tow_tow_url_layout);
        dappinfo_submenu_tow_three_url_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_tow_three_url_layout);
        dappinfo_submenu_tow_four_url_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_tow_four_url_layout);
        dappinfo_submenu_tow_five_url_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_tow_five_url_layout);

        dappinfo_submenu_three_tow_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_three_tow_layout);
        dappinfo_submenu_three_three_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_three_three_layout);
        dappinfo_submenu_three_four_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_three_four_layout);
        dappinfo_submenu_three_five_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_three_five_layout);
        dappinfo_submenu_three_tow_url_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_three_tow_url_layout);
        dappinfo_submenu_three_three_url_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_three_three_url_layout);
        dappinfo_submenu_three_four_url_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_three_four_url_layout);
        dappinfo_submenu_three_five_url_layout = (LinearLayout) findViewById(R.id.dappinfo_submenu_three_five_url_layout);

        dappinfo_mainmenu_one = (EditText)findViewById(R.id.dappinfo_mainmenu_one);
        dappinfo_mainmenu_tow = (EditText)findViewById(R.id.dappinfo_mainmenu_tow);
        dappinfo_mainmenu_three = (EditText)findViewById(R.id.dappinfo_mainmenu_three);


        dappinfo_submenu_one_one = (EditText)findViewById(R.id.dappinfo_submenu_one_one);
        dappinfo_submenu_one_one_url = (EditText)findViewById(R.id.dappinfo_submenu_one_one_url);
        dappinfo_submenu_one_tow = (EditText)findViewById(R.id.dappinfo_submenu_one_tow);
        dappinfo_submenu_one_tow_url = (EditText)findViewById(R.id.dappinfo_submenu_one_tow_url);
        dappinfo_submenu_one_three = (EditText)findViewById(R.id.dappinfo_submenu_one_three);
        dappinfo_submenu_one_three_url = (EditText)findViewById(R.id.dappinfo_submenu_one_three_url);
        dappinfo_submenu_one_four = (EditText)findViewById(R.id.dappinfo_submenu_one_four);
        dappinfo_submenu_one_four_url = (EditText)findViewById(R.id.dappinfo_submenu_one_four_url);
        dappinfo_submenu_one_five = (EditText)findViewById(R.id.dappinfo_submenu_one_five);
        dappinfo_submenu_one_five_url = (EditText)findViewById(R.id.dappinfo_submenu_one_five_url);


        dappinfo_submenu_tow_one = (EditText)findViewById(R.id.dappinfo_submenu_tow_one);
        dappinfo_submenu_tow_one_url = (EditText)findViewById(R.id.dappinfo_submenu_tow_one_url);
        dappinfo_submenu_tow_tow = (EditText)findViewById(R.id.dappinfo_submenu_tow_tow);
        dappinfo_submenu_tow_tow_url = (EditText)findViewById(R.id.dappinfo_submenu_tow_tow_url);
        dappinfo_submenu_tow_three = (EditText)findViewById(R.id.dappinfo_submenu_tow_three);
        dappinfo_submenu_tow_three_url = (EditText)findViewById(R.id.dappinfo_submenu_tow_three_url);
        dappinfo_submenu_tow_four = (EditText)findViewById(R.id.dappinfo_submenu_tow_four);
        dappinfo_submenu_tow_four_url = (EditText)findViewById(R.id.dappinfo_submenu_tow_four_url);
        dappinfo_submenu_tow_five = (EditText)findViewById(R.id.dappinfo_submenu_tow_five);
        dappinfo_submenu_tow_five_url = (EditText)findViewById(R.id.dappinfo_submenu_tow_five_url);


        dappinfo_submenu_three_one = (EditText)findViewById(R.id.dappinfo_submenu_three_one);
        dappinfo_submenu_three_one_url = (EditText)findViewById(R.id.dappinfo_submenu_three_one_url);
        dappinfo_submenu_three_tow = (EditText)findViewById(R.id.dappinfo_submenu_three_tow);
        dappinfo_submenu_three_tow_url = (EditText)findViewById(R.id.dappinfo_submenu_three_tow_url);
        dappinfo_submenu_three_three = (EditText)findViewById(R.id.dappinfo_submenu_three_three);
        dappinfo_submenu_three_three_url = (EditText)findViewById(R.id.dappinfo_submenu_three_three_url);
        dappinfo_submenu_three_four = (EditText)findViewById(R.id.dappinfo_submenu_three_four);
        dappinfo_submenu_three_four_url = (EditText)findViewById(R.id.dappinfo_submenu_three_four_url);
        dappinfo_submenu_three_five = (EditText)findViewById(R.id.dappinfo_submenu_three_five);
        dappinfo_submenu_three_five_url = (EditText)findViewById(R.id.dappinfo_submenu_three_five_url);
        page = 1;
        a = 1;
        b = 1;
        c = 1;
    }
    public void back(View view){
        finish();
    }
}