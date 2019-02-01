package com.eladapp.elachat.chat;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.eladapp.elachat.R;
import com.eladapp.elachat.did.DidmanageActivity;
import com.eladapp.elachat.mysetting.AppsettingActivity;
import com.eladapp.elachat.mysetting.AssetsettingActivity;
import com.eladapp.elachat.mysetting.LicenseagreementActivity;
import com.eladapp.elachat.mysetting.MessagesettingActivity;

public class Fragmentprofile extends Fragment {
    private RelativeLayout appsetting;
    private RelativeLayout asset_setting;
    private RelativeLayout did_setting;
    private RelativeLayout message_setting;
    private RelativeLayout certificate_setting;
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
}