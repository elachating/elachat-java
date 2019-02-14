package com.eladapp.elachat.walletspv;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eladapp.elachat.R;
import com.eladapp.elachat.application.CloudchatApp;
import com.elastos.spvcore.IMasterWallet;

import java.util.ArrayList;

/**
 * @author liu
 * @date 2018-10-3
 */
public class AssetmenurActivity extends Activity implements OnClickListener{
    private String curprice=null;
    private CloudchatApp cloudchatapp;
    private ArrayList<IMasterWallet> mainmasterwallet;
    private LinearLayout addasset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assetrightmenu);
        initView();
    }
    private void initView(){
        addasset = findViewById(R.id.add_asset_layout);
        addasset.setOnClickListener(this);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        finish();
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_asset_layout:
                cloudchatapp = new CloudchatApp();
                mainmasterwallet =  cloudchatapp.getwalletlist();
                if(mainmasterwallet==null || mainmasterwallet.toString().equals("[]")){
                    Intent intent = new Intent();
                    intent.setClass(this, WalletcreateonestepActivity.class);
                    startActivity(intent);
                }else {
                    if(getlangconfig().equals("cn")){
                        Toast.makeText(getApplicationContext(), "已经创建了资产，无需再创建!", Toast.LENGTH_SHORT).show();
                    }else if(getlangconfig().equals("en")){
                        Toast.makeText(getApplicationContext(), "Assets have been created, no more need to be created.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "已经创建了资产，无需再创建!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
        this.finish();
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