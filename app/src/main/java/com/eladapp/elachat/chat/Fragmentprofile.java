package com.eladapp.elachat.chat;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.eladapp.elachat.R;
import com.eladapp.elachat.application.CloudchatApp;
import com.eladapp.elachat.did.DidmanageActivity;
import com.eladapp.elachat.mysetting.AppsettingActivity;
import com.eladapp.elachat.mysetting.AssetsettingActivity;
import com.eladapp.elachat.mysetting.LicenseagreementActivity;
import com.eladapp.elachat.mysetting.MessagesettingActivity;
import com.eladapp.elachat.walletspv.WalletcreateonestepActivity;
import com.elastos.spvcore.IMasterWallet;

import org.ela.Carrier.Chatcarrier;
import org.elastos.carrier.Carrier;
import org.elastos.carrier.FriendInfo;
import org.elastos.carrier.UserInfo;

import java.util.ArrayList;

public class Fragmentprofile extends Fragment {
    private RelativeLayout appsetting;
    private RelativeLayout asset_setting;
    private RelativeLayout did_setting;
    private RelativeLayout message_setting;
    private RelativeLayout certificate_setting;
    private CloudchatApp cloudchatapp;
    private ArrayList<IMasterWallet> mainmasterwallet;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_setting, container,false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initview();
        appsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(getActivity(), AppsettingActivity.class));
            }
        });
        asset_setting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent().setClass(getActivity(), AssetsettingActivity.class));
            }
        });
        did_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkdidpro();
                startActivity(new Intent().setClass(getActivity(), DidmanageActivity.class));
            }
        });
        message_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(getActivity(), MessagesettingActivity.class));
            }
        });
        certificate_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(getActivity(), LicenseagreementActivity.class));
            }
        });
    }
    public void  initview(){
        appsetting = (RelativeLayout)getView().findViewById(R.id.app_setting);
        asset_setting = (RelativeLayout)getView().findViewById(R.id.asset_setting);
        did_setting = (RelativeLayout)getView().findViewById(R.id.did_setting);
        message_setting = (RelativeLayout)getView().findViewById(R.id.message_setting);
        certificate_setting = (RelativeLayout)getView().findViewById(R.id.certificate_setting);
    }
    //判断是否修改了Carrier昵称和创建了资产
    public void checkdidpro(){
        Chatcarrier chatcarrier = new Chatcarrier();
        UserInfo myinfo = chatcarrier.getmyinfo();
        String myusername = myinfo.getName();
        if(myusername.equals("")){
            if(getlangconfig().equals("cn")){
                Toast.makeText(getActivity(), "昵称不能为空，请设置昵称", Toast.LENGTH_SHORT).show();
            }else if(getlangconfig().equals("en")){
                Toast.makeText(getActivity(), "Nickname cannot be empty. Please set a nickname.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "昵称不能为空，请设置昵称", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent();
            intent.setClass(getContext(), MyInfoActivity.class);
            startActivity(intent);
        }
        cloudchatapp = new CloudchatApp();
        mainmasterwallet =  cloudchatapp.getwalletlist();
        if(mainmasterwallet==null || mainmasterwallet.toString().equals("[]")){
            if(getlangconfig().equals("cn")){
                Toast.makeText(getActivity(), "未创建资产，请创建后操作!", Toast.LENGTH_SHORT).show();
            }else if(getlangconfig().equals("en")){
                Toast.makeText(getActivity(), "No assets were created, please do after creation.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "未创建资产，请创建后操作!", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent();
            intent.setClass(getContext(), WalletcreateonestepActivity.class);
            startActivity(intent);
        }
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