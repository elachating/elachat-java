package com.eladapp.elachat.mysetting;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.eladapp.elachat.R;
import com.eladapp.elachat.application.CloudchatApp;
import com.elastos.spvcore.IMasterWallet;
import com.elastos.spvcore.WalletException;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AsseteditpaypwdActivity extends AppCompatActivity {
    private EditText old_paypwd;
    private EditText re_new_paypwd;
    private EditText re_new_confirm_paypwd;
    private Button edit_paypwd_btn;
    private Boolean checkpwd = true;
    private CloudchatApp cloudchatapp;
    private IMasterWallet curmastwallet;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_paypwd);
        initiview();
        edit_paypwd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoldornewpwd(old_paypwd.getText().toString(),re_new_paypwd.getText().toString(),re_new_confirm_paypwd.getText().toString());
                if(checkpwd){
                    String editrs = updatepaypwd(old_paypwd.getText().toString(),re_new_paypwd.getText().toString());
                    if(editrs.equals("ok")){
                        if(getlangconfig().equals("cn")){
                            Toast.makeText(getApplicationContext(), "修改成功.", Toast.LENGTH_SHORT).show();
                        }else if(getlangconfig().equals("en")){
                            Toast.makeText(getApplicationContext(), "Modified Success.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "修改成功.", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }else{
                        if(getlangconfig().equals("cn")){
                            Toast.makeText(getApplicationContext(), "原支付密码不正确.", Toast.LENGTH_SHORT).show();
                        }else if(getlangconfig().equals("en")){
                            Toast.makeText(getApplicationContext(), "The original payment password is incorrect.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "原支付密码不正确.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
    public void initiview(){
        old_paypwd = (EditText)findViewById(R.id.re_old_paypwd);
        re_new_paypwd = (EditText)findViewById(R.id.re_new_paypwd);
        re_new_confirm_paypwd = (EditText)findViewById(R.id.re_confirm_new_paypwd);
        edit_paypwd_btn = (Button)findViewById(R.id.edit_paypwd_btn);
    }
    public void checkoldornewpwd(String oldpwd,String newpwd,String confirmnewpwd){
        if(oldpwd.equals("")){
            if(getlangconfig().equals("cn")){
                Toast.makeText(getApplicationContext(),"旧密码不能为空.",Toast.LENGTH_SHORT).show();
            }else if(getlangconfig().equals("en")){
                Toast.makeText(getApplicationContext(),"Old passwords cannot be empty.",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"旧密码不能为空.",Toast.LENGTH_SHORT).show();
            }
            checkpwd = false;
            return;
        }
        if(!newpwd.equals(confirmnewpwd) || newpwd.equals("")){
            if(getlangconfig().equals("cn")){
                Toast.makeText(getApplicationContext(),"两次输入新密码不一致.",Toast.LENGTH_SHORT).show();
            }else if(getlangconfig().equals("en")){
                Toast.makeText(getApplicationContext(),"Two inconsistent new passwords.",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"两次输入新密码不一致.",Toast.LENGTH_SHORT).show();
            }
            checkpwd = false;
            return;
        }
    }
    public String updatepaypwd(String oldpwd,String newpwd){
        String updateinfo = "";
        cloudchatapp = new CloudchatApp();
        ArrayList<IMasterWallet> Fmastwallet = cloudchatapp.getwalletlist();
        try{
            Fmastwallet.get(0).ChangePassword(oldpwd,newpwd);
            updateinfo = "ok";
        }catch (WalletException e){
            updateinfo = e.GetErrorInfo();
        }
        return updateinfo;
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