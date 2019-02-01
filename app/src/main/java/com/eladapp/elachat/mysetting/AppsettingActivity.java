package com.eladapp.elachat.mysetting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.eladapp.elachat.R;

public class AppsettingActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appsetting);
    }
    public void initview(){

    }
    public void back(View view){
        finish();
    }
}