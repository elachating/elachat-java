package com.eladapp.elachat.mysetting;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eladapp.elachat.R;
import com.eladapp.elachat.application.CloudchatApp;
import com.eladapp.elachat.walletspv.AssetpayActivity;
import com.elastos.spvcore.IMasterWallet;
import com.elastos.spvcore.MasterWalletManager;

public class AssetoutputprvstoreActivity extends AppCompatActivity {
    private EditText setkeystorepwd;
    private EditText resetkeystorepwd;
    private EditText curpaypwd;
    private Button outputkeystorebtn;
    private RelativeLayout setkeystorepwdout;
    private RelativeLayout resetkeysorepwdout;
    private RelativeLayout curpaypwdout;
    private RelativeLayout outputkeystorebtnout;
    private RelativeLayout outputkeystorecout;
    private TextView outputkeystorec;
    private Boolean checkpwd = true;
    private CloudchatApp cloudchatapp;
    private LinearLayout copybtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_keystore);
        initview();
        copybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager myClipboard;
                ClipData myClip;
                myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                myClip = ClipData.newPlainText("text", outputkeystorec.getText().toString());
                myClipboard.setPrimaryClip(myClip);
                if(getlangconfig().equals("cn")){
                    Toast.makeText(AssetoutputprvstoreActivity.this,"KeyStore已复制.", Toast.LENGTH_SHORT).show();
                }else if(getlangconfig().equals("en")){
                    Toast.makeText(AssetoutputprvstoreActivity.this,"KeyStore has been copyed.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AssetoutputprvstoreActivity.this,"KeyStore已复制.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        outputkeystorebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkkeystorein(setkeystorepwd.getText().toString() ,resetkeystorepwd.getText().toString(),curpaypwd.getText().toString());
                if(checkpwd){
                    String keystorec = exportkeystore(setkeystorepwd.getText().toString(),curpaypwd.getText().toString());
                    outputkeystorec.setText(keystorec);
                    setkeystorepwdout.setVisibility(View.GONE);
                    resetkeysorepwdout.setVisibility(View.GONE);
                    curpaypwdout.setVisibility(View.GONE);
                    outputkeystorebtnout.setVisibility(View.GONE);
                    outputkeystorecout.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    //导出keystore文件
    public String exportkeystore(String keystorepwd,String paypwd){
        cloudchatapp = new CloudchatApp();
        MasterWalletManager mwalletmanage =  cloudchatapp.getwalletmanager();
        IMasterWallet curmasterwallet = mwalletmanage.GetAllMasterWallets().get(0);
        String keystores =  mwalletmanage.ExportWalletWithKeystore(curmasterwallet,keystorepwd,paypwd);
        return keystores;
    }
    public void  initview(){
        setkeystorepwd = (EditText)findViewById(R.id.set_keystore_pwd);
        resetkeystorepwd = (EditText)findViewById(R.id.re_set_keystore_pwd);
        curpaypwd = (EditText)findViewById(R.id.asset_paypwd);
        outputkeystorebtn = (Button)findViewById(R.id.output_keystore_btn);
        setkeystorepwdout = (RelativeLayout)findViewById(R.id.setkeystorepwdout);
        resetkeysorepwdout = (RelativeLayout)findViewById(R.id.resetkeystorepwdout);
        curpaypwdout = (RelativeLayout)findViewById(R.id.repaypwdout);
        outputkeystorebtnout = (RelativeLayout)findViewById(R.id.keystoreoutbtn);
        outputkeystorecout = (RelativeLayout)findViewById(R.id.keystore_c_out);
        outputkeystorec = (TextView)findViewById(R.id.keystore_c);
        outputkeystorec.setMovementMethod(ScrollingMovementMethod.getInstance());
        copybtn = (LinearLayout)findViewById(R.id.copybtn);
    }
    public void checkkeystorein(String keystorepwd,String rekeystorepwd,String curpaypwd){
        if(!keystorepwd.equals(rekeystorepwd) || keystorepwd.equals("")){
            if(getlangconfig().equals("cn")){
                Toast.makeText(getApplicationContext(),"两次输入keystore密码不一致.",Toast.LENGTH_SHORT).show();
            }else if(getlangconfig().equals("en")){
                Toast.makeText(getApplicationContext(),"Twice input keystore password is differences.",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"两次输入keystore密码不一致.",Toast.LENGTH_SHORT).show();
            }
            checkpwd = false;
            return;
        }
        if(curpaypwd.equals("")){
            if(getlangconfig().equals("cn")){
                Toast.makeText(getApplicationContext(),"当前钱包支付密码不能为空.",Toast.LENGTH_SHORT).show();
            }else if(getlangconfig().equals("en")){
                Toast.makeText(getApplicationContext(),"Current wallet payment password cannot be empty.",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"当前钱包支付密码不能为空.",Toast.LENGTH_SHORT).show();
            }
            checkpwd = false;
            return;
        }
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