package com.eladapp.elachat.mysetting;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.eladapp.elachat.ElachatActivity;
import com.eladapp.elachat.R;
import java.util.Locale;

public class AppsettingActivity extends AppCompatActivity{
    private Spinner setlang_spinner;
    private ArrayAdapter<String> mAdapter ;
    private String [] mStringArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appsetting);
        initview();
    }
    public void initview(){
        setlang_spinner = (Spinner)findViewById(R.id.setlang_spinner);
        mStringArray=getResources().getStringArray(R.array.lang_spinngarr);
        //使用自定义的ArrayAdapter
        mAdapter = new LangAdapter(AppsettingActivity.this,mStringArray);
        //设置下拉列表风格(这句不些也行)
        //mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setlang_spinner.setAdapter(mAdapter);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        String langs = String.valueOf(config.locale);
        String cnn = String.valueOf(Locale.CHINA);
        String enn = String.valueOf(Locale.ENGLISH);
        if(langs.equals(cnn)){
            setlang_spinner.setSelection(1,true);
        }else if(langs.equals(enn)){
            setlang_spinner.setSelection(2,true);
        }else{
            setlang_spinner.setSelection(0,true);
        }
        //监听Item选中事件
        setlang_spinner.setOnItemSelectedListener(new ItemSelectedListenerImpl());
    }
    private class ItemSelectedListenerImpl implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position,long arg3) {
            if(position==0){
                setlang_spinner.setSelection(0);
            }else if(position==1){
                setlang_spinner.setSelection(1);
                switchLanguage("cn");
            }else if(position==2){
                setlang_spinner.setSelection(2);
                switchLanguage("en");
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }
    private void switchLanguage(String language){
        //获取资源
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        if (language.equals("cn"))
            config.locale = Locale.CHINA;//方法过时但是一样可以使用
        else
            config.setLocale(Locale.ENGLISH);//低版本无法使用,如果使用不了,就使用上面的方法
        //更新,稍后提及次方法过时.
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        //关闭页面,重新绘制
        finish();
        startActivity(new Intent(AppsettingActivity.this, ElachatActivity.class));
    }
    public void back(View view){
        finish();
    }
}