package com.eladapp.elachat.dapps;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eladapp.elachat.R;

import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class DappmainActivity extends AppCompatActivity{
    private TextView dappmain_name;
    private RelativeLayout dappmenulist_btn1;
    private ImageView dappmenulist_img1;
    private TextView dappmenulist_text1;
    private TextView dappmenulist_text11;
    private RelativeLayout dappmenulist_btn2;
    private ImageView dappmenulist_img2;
    private TextView dappmenulist_text2;
    private TextView dappmenulist_text22;
    private RelativeLayout dappmenulist_btn3;
    private ImageView dappmenulist_img3;
    private TextView dappmenulist_text3;
    private TextView dappmenulist_text33;
    private String menujson;
    private String did;
    private LinearLayout popLayout1;
    private LinearLayout popLayout2;
    private LinearLayout popLayout3;
    private View view1;
    private View view2;
    private View view3;
    private LayoutInflater inflatera;
    private LayoutInflater inflaterb;
    private LayoutInflater inflaterc;
    private boolean opena = true;// 子菜单填充状态标记
    private boolean flaga = false;// 子菜单显示状态标记
    private boolean openb = true;// 子菜单填充状态标记
    private boolean flagb = false;// 子菜单显示状态标记
    private boolean openc = true;// 子菜单填充状态标记
    private boolean flagc = false;// 子菜单显示状态标记
    private LinearLayout childLayout_one;// 第一个菜单
    private LinearLayout childLayout_tow;// 第二个菜单
    private LinearLayout childLayout_three;// 第三个菜单
    private Button dapp_submenu1;
    private TextView dapp_submenu_txt1;
    private Button dapp_submenu2;
    private TextView dapp_submenu_txt2;
    private Button dapp_submenu3;
    private TextView dapp_submenu_txt3;
    private Button dapp_submenu4;
    private TextView dapp_submenu_txt4;
    private Button dapp_submenu5;
    private TextView dapp_submenu_txt5;
    private List<Button> lista;
    private List<TextView> listb;
    private List<RelativeLayout> listc;
    private List<RelativeLayout> list1;
    private List<TextView> list2;
    private List<ImageView> list3;
    private List<TextView> list4;
    private RelativeLayout dapp_submenu_r1;
    private RelativeLayout dapp_submenu_r2;
    private RelativeLayout dapp_submenu_r3;
    private RelativeLayout dapp_submenu_r4;
    private RelativeLayout dapp_submenu_r5;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dapp_main);
        String appname = this.getIntent().getStringExtra("dappname");
        menujson = this.getIntent().getStringExtra("menujson");
        did = this.getIntent().getStringExtra("did");
        initview();
        lista = new ArrayList<Button>();
        listb = new ArrayList<TextView>();
        listc = new ArrayList<RelativeLayout>();
        list1 = new ArrayList<RelativeLayout>();
        list2 = new ArrayList<TextView>();
        list3 = new ArrayList<ImageView>();
        list4 = new ArrayList<TextView>();
        list1.add(dappmenulist_btn1);
        list1.add(dappmenulist_btn2);
        list1.add(dappmenulist_btn3);
        list2.add(dappmenulist_text1);
        list2.add(dappmenulist_text2);
        list2.add(dappmenulist_text3);
        list3.add(dappmenulist_img1);
        list3.add(dappmenulist_img2);
        list3.add(dappmenulist_img3);
        list4.add(dappmenulist_text11);
        list4.add(dappmenulist_text22);
        list4.add(dappmenulist_text33);
        inflatera = DappmainActivity.this.getLayoutInflater();
        inflaterb = DappmainActivity.this.getLayoutInflater();
        inflaterc = DappmainActivity.this.getLayoutInflater();
        dappmain_name.setText(appname);
      //  String menulist = "[{\"name\":\"天气预报\",\"url\":\"\",\"sub_button\":[{\"type\":\"view\",\"name\":\"北京天气\",\"url\":\"http://m.hao123.com/a/tianqi\"},{\"type\":\"view\",\"name\":\"上海天气\",\"url\":\"http://m.hao123.com/a/tianqi\"},{\"type\":\"view\",\"name\":\"广州天气\",\"url\":\"http://m.hao123.com/a/tianqi\"}]},{\"name\":\"天气预报2\",\"url\":\"\",\"sub_button\":[{\"type\":\"view\",\"name\":\"北京天气\",\"url\":\"http://m.hao123.com/a/tianqi\"},{\"type\":\"view\",\"name\":\"上海天气\",\"url\":\"http://www.163.com\"},{\"type\":\"view\",\"name\":\"广州天气\",\"url\":\"http://m.hao123.com/a/tianqi\"}]},{\"name\":\"空特科技\",\"url\":\"\",\"sub_button\":[{\"type\":\"view\",\"name\":\"公司简介\",\"url\":\"http://wwww.baidu.com\"},{\"type\":\"view\",\"name\":\"趣味游戏\",\"url\":\"http://www.qq.com\"}]}]";
        //String menulist =menujson;
        System.out.println("菜单列表："+menujson);
        JSONArray menujsonlist = JSONArray.fromObject(menujson);
        for(int l=0;l<list1.size();l++){
            list1.get(l).setVisibility(View.GONE);
        }
        for(int t=0;t<menujsonlist.size();t++){
            System.out.println("菜单名称："+menujsonlist.getJSONObject(t).get("name").toString());
            list1.get(t).setVisibility(View.VISIBLE);
            list2.get(t).setText(menujsonlist.getJSONObject(t).get("name").toString());
            if(menujsonlist.getJSONObject(t).get("url").toString().equals("")){
                list3.get(t).setVisibility(View.VISIBLE);
                list4.get(t).setText(menujsonlist.getJSONObject(t).get("sub_button").toString());
            }else{
                list3.get(t).setVisibility(View.GONE);
            }
        }
        view1 = inflaterc.inflate(R.layout.activity_dappmenu_submenu_one, popLayout1, true);
        childLayout_one = (LinearLayout) view1.findViewById(R.id.child_layout_one);
        childLayout_one.setVisibility(View.GONE);
        view2 = inflaterc.inflate(R.layout.activity_dappmenu_submenu_tow, popLayout2, true);
        childLayout_tow = (LinearLayout) view2.findViewById(R.id.child_layout_tow);
        childLayout_tow.setVisibility(View.GONE);
        view3 = inflaterc.inflate(R.layout.activity_dappmenu_submenu_three, popLayout3, true);
        childLayout_three = (LinearLayout) view3.findViewById(R.id.child_layout_three);
        childLayout_three.setVisibility(View.GONE);
        dappmenulist_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagb = false;
                childLayout_tow.setVisibility(View.GONE);
                flagc = false;
                childLayout_three.setVisibility(View.GONE);
                if (opena == true) {
                    //view1 = inflatera.inflate(R.layout.activity_dappmenu_submenu_one, popLayout1, true);
                  //  childLayout_one = (LinearLayout) view1.findViewById(R.id.child_layout);
                    opena = false;
                }
                if (flaga == false) {
                    flaga = true;
                    childLayout_one.setVisibility(View.VISIBLE);
                } else {
                    flaga = false;
                    childLayout_one.setVisibility(View.GONE);
                }
                dapp_submenu1 = (Button) view1.findViewById(R.id.dapp_submnu1);
                dapp_submenu_txt1  = (TextView) view1. findViewById(R.id.dapp_submnu_txt1);
                dapp_submenu2 = (Button)  view1.findViewById(R.id.dapp_submnu2);
                dapp_submenu_txt2  = (TextView) view1. findViewById(R.id.dapp_submnu_txt2);
                dapp_submenu3 = (Button) view1. findViewById(R.id.dapp_submnu3);
                dapp_submenu_txt3  = (TextView) view1. findViewById(R.id.dapp_submnu_txt3);
                dapp_submenu4 = (Button)  view1.findViewById(R.id.dapp_submnu4);
                dapp_submenu_txt4  = (TextView) view1. findViewById(R.id.dapp_submnu_txt4);
                dapp_submenu5 = (Button) view1. findViewById(R.id.dapp_submnu5);
                dapp_submenu_txt5  = (TextView) view1. findViewById(R.id.dapp_submnu_txt5);
                dapp_submenu_r1  = (RelativeLayout) view1. findViewById(R.id.dapp_submnu_r1);
                dapp_submenu_r2  = (RelativeLayout) view1. findViewById(R.id.dapp_submnu_r2);
                dapp_submenu_r3  = (RelativeLayout) view1. findViewById(R.id.dapp_submnu_r3);
                dapp_submenu_r4  = (RelativeLayout) view1. findViewById(R.id.dapp_submnu_r4);
                dapp_submenu_r5  = (RelativeLayout)  view1.findViewById(R.id.dapp_submnu_r5);
                showsubmenu(dappmenulist_text11.getText().toString(),view1);
                submenujump();
            }
        });
        dappmenulist_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flaga = false;
                childLayout_one.setVisibility(View.GONE);
                flagc = false;
               childLayout_three.setVisibility(View.GONE);
                //Toast.makeText(getApplicationContext(), "子菜单："+dappmenulist_text22.getText().toString(), Toast.LENGTH_SHORT).show();
                if (openb == true) {
                    //view2 = inflaterb.inflate(R.layout.activity_dappmenu_submenu_tow, popLayout2, true);
                   // childLayout_tow = (LinearLayout) view2.findViewById(R.id.child_layout_tow);
                    openb = false;
                }
                if (flagb == false) {
                    flagb = true;
                    childLayout_tow.setVisibility(View.VISIBLE);
                } else {
                    flagb = false;
                    childLayout_tow.setVisibility(View.GONE);

                }
                dapp_submenu1 = (Button) view2.findViewById(R.id.dapp_submnu1);
                dapp_submenu_txt1  = (TextView) view2. findViewById(R.id.dapp_submnu_txt1);
                dapp_submenu2 = (Button)  view2.findViewById(R.id.dapp_submnu2);
                dapp_submenu_txt2  = (TextView) view2. findViewById(R.id.dapp_submnu_txt2);
                dapp_submenu3 = (Button) view2. findViewById(R.id.dapp_submnu3);
                dapp_submenu_txt3  = (TextView) view2. findViewById(R.id.dapp_submnu_txt3);
                dapp_submenu4 = (Button)  view2.findViewById(R.id.dapp_submnu4);
                dapp_submenu_txt4  = (TextView) view2. findViewById(R.id.dapp_submnu_txt4);
                dapp_submenu5 = (Button) view2. findViewById(R.id.dapp_submnu5);
                dapp_submenu_txt5  = (TextView) view2. findViewById(R.id.dapp_submnu_txt5);
                dapp_submenu_r1  = (RelativeLayout) view2. findViewById(R.id.dapp_submnu_r1);
                dapp_submenu_r2  = (RelativeLayout) view2. findViewById(R.id.dapp_submnu_r2);
                dapp_submenu_r3  = (RelativeLayout) view2. findViewById(R.id.dapp_submnu_r3);
                dapp_submenu_r4  = (RelativeLayout) view2. findViewById(R.id.dapp_submnu_r4);
                dapp_submenu_r5  = (RelativeLayout)  view2.findViewById(R.id.dapp_submnu_r5);
                showsubmenu(dappmenulist_text22.getText().toString(),view2);
                submenujump();
            }
        });
        dappmenulist_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flaga = false;
                childLayout_one.setVisibility(View.GONE);
                flagb = false;
                childLayout_tow.setVisibility(View.GONE);
                if (openc == true) {
                   // view3 = inflaterc.inflate(R.layout.activity_dappmenu_submenu_three, popLayout3, true);
                    //childLayout_three = (LinearLayout) view3.findViewById(R.id.child_layout_three);
                    openc = false;
                }
                if (flagc == false) {
                    flagc = true;
                    childLayout_three.setVisibility(View.VISIBLE);
                } else {
                    flagc = false;
                    childLayout_three.setVisibility(View.GONE);
                }
                dapp_submenu1 = (Button) view3.findViewById(R.id.dapp_submnu1);
                dapp_submenu_txt1  = (TextView) view3. findViewById(R.id.dapp_submnu_txt1);
                dapp_submenu2 = (Button)  view3.findViewById(R.id.dapp_submnu2);
                dapp_submenu_txt2  = (TextView) view3. findViewById(R.id.dapp_submnu_txt2);
                dapp_submenu3 = (Button) view3. findViewById(R.id.dapp_submnu3);
                dapp_submenu_txt3  = (TextView) view3. findViewById(R.id.dapp_submnu_txt3);
                dapp_submenu4 = (Button)  view3.findViewById(R.id.dapp_submnu4);
                dapp_submenu_txt4  = (TextView) view3. findViewById(R.id.dapp_submnu_txt4);
                dapp_submenu5 = (Button) view3. findViewById(R.id.dapp_submnu5);
                dapp_submenu_txt5  = (TextView) view3. findViewById(R.id.dapp_submnu_txt5);
                dapp_submenu_r1  = (RelativeLayout) view3. findViewById(R.id.dapp_submnu_r1);
                dapp_submenu_r2  = (RelativeLayout) view3. findViewById(R.id.dapp_submnu_r2);
                dapp_submenu_r3  = (RelativeLayout) view3. findViewById(R.id.dapp_submnu_r3);
                dapp_submenu_r4  = (RelativeLayout) view3. findViewById(R.id.dapp_submnu_r4);
                dapp_submenu_r5  = (RelativeLayout)  view3.findViewById(R.id.dapp_submnu_r5);
                showsubmenu(dappmenulist_text33.getText().toString(),view3);
                submenujump();
            }
        });
    }
    //隐藏所有
    public void hidesubmenu(){
        flaga = false;
        flagb = false;
        flagc = false;
        childLayout_one.setVisibility(View.GONE);
        childLayout_tow.setVisibility(View.GONE);
        childLayout_three.setVisibility(View.GONE);
    }
    //子菜单跳转
    public void submenujump(){
        dapp_submenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidesubmenu();
               startActivity(new Intent(DappmainActivity.this, DappActivity.class).putExtra("dappname", dapp_submenu1.getText().toString()).putExtra("jumpurl",dapp_submenu_txt1.getText().toString()));
             }
        });
        dapp_submenu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidesubmenu();
                startActivity(new Intent(DappmainActivity.this, DappActivity.class).putExtra("dappname", dapp_submenu2.getText().toString()).putExtra("jumpurl",dapp_submenu_txt2.getText().toString()));
            }
        });
        dapp_submenu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidesubmenu();
                startActivity(new Intent(DappmainActivity.this, DappActivity.class).putExtra("dappname", dapp_submenu3.getText().toString()).putExtra("jumpurl",dapp_submenu_txt3.getText().toString()));
            }
        });
        dapp_submenu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidesubmenu();
                startActivity(new Intent(DappmainActivity.this, DappActivity.class).putExtra("dappname", dapp_submenu4.getText().toString()).putExtra("jumpurl",dapp_submenu_txt4.getText().toString()));
            }
        });
        dapp_submenu5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidesubmenu();
                startActivity(new Intent(DappmainActivity.this, DappActivity.class).putExtra("dappname", dapp_submenu5.getText().toString()).putExtra("jumpurl",dapp_submenu_txt5.getText().toString()));
            }
        });

    }
    //设置子菜单列表
    public void showsubmenu(String submenulists,View v){
        lista.clear();
        listb.clear();
        listc.clear();
        lista.add(dapp_submenu1);
        lista.add(dapp_submenu2);
        lista.add(dapp_submenu3);
        lista.add(dapp_submenu4);
        lista.add(dapp_submenu5);
        listb.add(dapp_submenu_txt1);
        listb.add(dapp_submenu_txt2);
        listb.add(dapp_submenu_txt3);
        listb.add(dapp_submenu_txt4);
        listb.add(dapp_submenu_txt5);
        listc.add(dapp_submenu_r1);
        listc.add(dapp_submenu_r2);
        listc.add(dapp_submenu_r3);
        listc.add(dapp_submenu_r4);
        listc.add(dapp_submenu_r5);
       for(int p=0;p<listc.size();p++){
            listc.get(p).setVisibility(v.GONE);
        }
        JSONArray submenujsonlists = JSONArray.fromObject(submenulists);
        for(int j=0;j<submenujsonlists.size();j++){
            listc.get(j).setVisibility(v.VISIBLE);
            System.out.println("子菜单："+submenujsonlists.getJSONObject(j).get("name").toString());
            lista.get(j).setText(submenujsonlists.getJSONObject(j).get("name").toString());
            listb.get(j).setText(submenujsonlists.getJSONObject(j).get("url").toString());
        }
    }
    public void initview(){
        dappmain_name = (TextView)findViewById(R.id.dappmain_name);
        dappmenulist_btn1 = (RelativeLayout) findViewById(R.id.dappmenulist_btn1);
        dappmenulist_img1 = (ImageView)findViewById(R.id.dappmenulist_img1);
        dappmenulist_text1 = (TextView)findViewById(R.id.dappmenulist_text1);
        dappmenulist_text11 = (TextView)findViewById(R.id.dappmenulist_text11);
        dappmenulist_btn2 = (RelativeLayout) findViewById(R.id.dappmenulist_btn2);
        dappmenulist_img2 = (ImageView)findViewById(R.id.dappmenulist_img2);
        dappmenulist_text2 = (TextView)findViewById(R.id.dappmenulist_text2);
        dappmenulist_text22 = (TextView)findViewById(R.id.dappmenulist_text22);
        dappmenulist_btn3 = (RelativeLayout) findViewById(R.id.dappmenulist_btn3);
        dappmenulist_img3 = (ImageView)findViewById(R.id.dappmenulist_img3);
        dappmenulist_text3 = (TextView)findViewById(R.id.dappmenulist_text3);
        dappmenulist_text33 = (TextView)findViewById(R.id.dappmenulist_text33);
        popLayout1 = (LinearLayout) findViewById(R.id.pop_layout1);
        popLayout2 = (LinearLayout) findViewById(R.id.pop_layout2);
        popLayout3 = (LinearLayout) findViewById(R.id.pop_layout3);
    }
    public void back(View view){
        finish();
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