package com.eladapp.elachat.walletspv;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eladapp.elachat.ElachatActivity;
import com.eladapp.elachat.R;
import com.eladapp.elachat.application.CloudchatApp;
import com.eladapp.elachat.chat.ChatActivity;
import com.eladapp.elachat.chat.Fragmentwallet;
import com.elastos.spvcore.ElastosWalletUtils;
import com.elastos.spvcore.IDidManager;
import com.elastos.spvcore.IMasterWallet;
import com.elastos.spvcore.ISubWallet;
import com.elastos.spvcore.MasterWalletManager;

import org.ela.Elaspv.Elaspvapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WalletcreatetowstepActivity extends AppCompatActivity {
    private EditText paypwd;
    private EditText cpaypwd;
    private Button confirmbtn;

    private IMasterWallet mCurrentMasterWallet;
    private IDidManager mDidManager = null;
    private MasterWalletManager mWalletManager;
    private ArrayList<IMasterWallet> mMasterWalletList = new ArrayList<IMasterWallet>();
    private Map<String, ISubWallet> mSubWalletMap = new HashMap<String, ISubWallet>();
    private IMasterWallet masterWallet;
    private ISubWallet subWallet;
    private String rootpath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletcreatetowstep);

        rootpath = getApplicationContext().getFilesDir().getParent();
        final String phrasewords = this.getIntent().getStringExtra("phrasewords");
        paypwd = (EditText)findViewById(R.id.paypwd);
        cpaypwd = (EditText)findViewById(R.id.cpaypwd);
        confirmbtn = (Button)findViewById(R.id.btn_add_wallet_step_confirm);
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean rs = checkpwd( paypwd.getText().toString(), cpaypwd.getText().toString());
                System.out.println("支付密码："+paypwd.getText().toString());
                System.out.println("确认支付密码："+cpaypwd.getText().toString());
                System.out.println(rs);
                if(rs){
                    System.out.println("是否：");
                   // Elaspvapi elaspvapi = new Elaspvapi();
                    ISubWallet subwallets =   createmasterwallet(rootpath,"ELA",phrasewords,cpaypwd.getText().toString(),cpaypwd.getText().toString(),"ELA");
                   System.out.println("钱包名称："+subwallets.GetChainId());
                    System.out.println("钱包地址："+subwallets.GetAllAddress(0,100));
                    System.out.println("钱包余额信息："+subwallets.GetBalanceInfo());

                    startActivity(new Intent(WalletcreatetowstepActivity.this, ElachatActivity.class).putExtra("id","2"));
                }
            }
        });
    }
    //检测支付密码
    public boolean checkpwd(String paypwd,String cpaypwd){
        boolean paypwdrs = true;
        if(!isLetterDigit(paypwd)){
            Toast.makeText(WalletcreatetowstepActivity.this,"密码必须包含字母和数字且长度8位到20位.",Toast.LENGTH_SHORT).show();
            paypwdrs = false;
        }
        if(!paypwd.equals(cpaypwd)){
            Toast.makeText(WalletcreatetowstepActivity.this,"支付密码与确认密码不一致.",Toast.LENGTH_SHORT).show();
            paypwdrs = false;
        }
        return paypwdrs;
    }
    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;
        boolean isLetter = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]{8,20}$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
    }
    public ISubWallet createmasterwallet(String rootpath,String masterwalletid, String mnemonic, String phrasepwd, String paypwd, String chainid){
        CloudchatApp cloudchatapp = new CloudchatApp();
        mWalletManager = cloudchatapp.getwalletmanager();
        long feePerKb = 10000;
        masterWallet = mWalletManager.CreateMasterWallet(masterwalletid, mnemonic,phrasepwd, paypwd, true);
        subWallet = masterWallet.CreateSubWallet(chainid,feePerKb);
        cloudchatapp.initwallet(rootpath);
        return subWallet;
    }
}