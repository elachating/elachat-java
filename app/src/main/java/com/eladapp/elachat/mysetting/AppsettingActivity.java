package com.eladapp.elachat.mysetting;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.eladapp.elachat.R;
import com.eladapp.elachat.db.Db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppsettingActivity extends AppCompatActivity{
    ImageView adddappinfo;
    private ListView listView;
    private DappinfosAdapter dappinfosadapter;
    private Messenger messenger;
    private Db db = new Db();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appsetting);
        initview();
        adddappinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppsettingActivity.this, AdddappinfoActivity.class));
            }
        });
    }
    public void initview(){
        adddappinfo = (ImageView)findViewById(R.id.adddappinfo);
        listView = (ListView) findViewById(R.id.setting_dapplist);
        List<Map<String, String>> list = new ArrayList<Map<String,String>>();
        list = db.dappinfolist();
        dappinfosadapter = new DappinfosAdapter(this, list);
        listView.setAdapter(dappinfosadapter);
    }

    public void back(View view){
        finish();
    }
}