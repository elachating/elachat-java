package com.eladapp.elachat.did;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.eladapp.elachat.R;
import com.eladapp.elachat.utils.CommonDialog;

public class DidAuthorShowActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //frienduserid = this.getIntent().getStringExtra("friendId");
       // initDialog();
    }
    private void initDialog() {
        final CommonDialog dialog = new CommonDialog(DidAuthorShowActivity.this);
        dialog.setMessage("该DAPP服务由**提供,且需提供相关的信息授权。")
                .setImageResId(R.mipmap.ic_launcher)
                .setTitle("信息提示")
                .setSingle(true).setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                dialog.dismiss();
                Toast.makeText(DidAuthorShowActivity.this,"xxxx",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNegtiveClick() {
                dialog.dismiss();
                Toast.makeText(DidAuthorShowActivity.this,"ssss",Toast.LENGTH_SHORT).show();
            }
        }).show();
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

