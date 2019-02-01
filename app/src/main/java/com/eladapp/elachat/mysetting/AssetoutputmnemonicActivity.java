package com.eladapp.elachat.mysetting;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import com.elastos.spvcore.IMasterWallet;
import com.elastos.spvcore.MasterWalletManager;

public class AssetoutputmnemonicActivity extends AppCompatActivity {
    private EditText mnemonicpaypwd;
    private Button outputmnemonicbtn;
    private RelativeLayout mnemoniccurpaypwdout;
    private RelativeLayout outputmnemonicbtnout;
    private RelativeLayout outputmnemonicout;
    private TextView outputmnemonic;
    private Boolean checkpwd = true;
    private CloudchatApp cloudchatapp;
    private LinearLayout copybtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outputmnemonic);
        initview();
        copybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager myClipboard;
                ClipData myClip;
                myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                myClip = ClipData.newPlainText("text", outputmnemonic.getText().toString());
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(AssetoutputmnemonicActivity.this,"助记词已复制.", Toast.LENGTH_SHORT).show();
            }
        });
        outputmnemonicbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkkeystorein(mnemonicpaypwd.getText().toString());
                if(checkpwd){
                    String mnemonic = exportmnmonic(mnemonicpaypwd.getText().toString());
                    outputmnemonic.setText(mnemonic);
                    mnemoniccurpaypwdout.setVisibility(View.GONE);
                    outputmnemonicbtnout.setVisibility(View.GONE);
                    outputmnemonicout.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    //导出keystore文件
    public String exportmnmonic(String paypwd){
        cloudchatapp = new CloudchatApp();
        MasterWalletManager mwalletmanage =  cloudchatapp.getwalletmanager();
        IMasterWallet curmasterwallet = mwalletmanage.GetAllMasterWallets().get(0);
        String mnemonic =  mwalletmanage.ExportWalletWithMnemonic(curmasterwallet,paypwd);
        return mnemonic;
    }
    public void  initview(){
        mnemonicpaypwd = (EditText)findViewById(R.id.asset_mnemonic_paypwd);
        mnemoniccurpaypwdout = (RelativeLayout)findViewById(R.id.mnemonicpaypwdout);
        outputmnemonicbtnout = (RelativeLayout)findViewById(R.id.mnemonicoutbtn);
        outputmnemonicbtn = (Button) findViewById(R.id.output_mnemonic_btn);
        outputmnemonicout = (RelativeLayout) findViewById(R.id.mnemonic_c_out);
        outputmnemonic = (TextView)findViewById(R.id.mnemonic_c);
        copybtn = (LinearLayout)findViewById(R.id.copybtn);
    }
    public void checkkeystorein(String curpaypwd){
        if(curpaypwd.equals("")){
            Toast.makeText(getApplicationContext(),"当前钱包支付密码不能为空.",Toast.LENGTH_SHORT).show();
            checkpwd = false;
            return;
        }
    }
    public void back(View view){
        finish();
    }
}