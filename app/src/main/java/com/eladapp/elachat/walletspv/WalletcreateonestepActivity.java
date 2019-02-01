package com.eladapp.elachat.walletspv;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.eladapp.elachat.ElachatActivity;
import com.eladapp.elachat.application.CloudchatApp;
import com.eladapp.elachat.chat.ChatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.commons.lang.StringUtils;
import org.ela.Elaspv.Elaspvapi;
import org.elastos.carrier.Carrier;
import org.elastos.carrier.exceptions.CarrierException;
import com.elastos.spvcore.ElastosWalletUtils;
import com.elastos.spvcore.MasterWalletManager;

import android.os.Messenger;
import com.eladapp.elachat.R;

import java.util.ArrayList;
import java.util.HashMap;

public class WalletcreateonestepActivity extends AppCompatActivity{
    private EditText phrasewrds;
    private Button prvbtn;
    private Button nextbtn;
    private Elaspvapi elaspvapi;
    private String [] phrasearr;
    private TextView prhasewordcurnum;
    //private  MasterWalletManager mWalletManager;
    private Button btn_add_wallet_step_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletcreateonestep);
        prvbtn = (Button)findViewById(R.id.btn_add_wallet_pre_step);
        nextbtn = (Button)findViewById(R.id.btn_add_wallet_next_step);
        phrasewrds = (EditText)findViewById(R.id.prhaseword);
        prhasewordcurnum = (TextView)findViewById(R.id.prhasewordcurnum);
        btn_add_wallet_step_next = (Button)findViewById(R.id.btn_add_wallet_step_next);
        final String [] phrasewordstring =  createprhase();
        phrasewrds.setText(phrasewordstring[0]);
        prvbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int wordsnum = Integer.parseInt(prhasewordcurnum.getText().toString());
                if(wordsnum>1){
                    nextbtn.setBackgroundColor(Color.parseColor("#438de2"));
                    phrasewrds.setText(phrasewordstring[wordsnum-2]);
                    prhasewordcurnum.setText(String.valueOf(wordsnum-1));
                   // btn_add_wallet_step_next.setVisibility(View.GONE);
                    btn_add_wallet_step_next.setBackgroundColor(Color.parseColor("#e5e5e5"));
                    btn_add_wallet_step_next.setEnabled(false);
                }
                if(wordsnum==1){
                    prvbtn.setBackgroundColor(Color.parseColor("#e5e5e5"));
                    btn_add_wallet_step_next.setEnabled(false);
                }
            }
        });
        nextbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int wordsnum = Integer.parseInt(prhasewordcurnum.getText().toString());
                if(wordsnum<12){
                    prvbtn.setBackgroundColor(Color.parseColor("#438de2"));
                    phrasewrds.setText(phrasewordstring[wordsnum]);
                    prhasewordcurnum.setText(String.valueOf(wordsnum+1));
                }
                if(wordsnum==12){
                    nextbtn.setBackgroundColor(Color.parseColor("#e5e5e5"));
                    btn_add_wallet_step_next.setBackgroundColor(Color.parseColor("#0070C9"));
                    //btn_add_wallet_step_next.setVisibility(View.VISIBLE);
                    btn_add_wallet_step_next.setEnabled(true);
                }
            }
        });
        btn_add_wallet_step_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String phrasewordss = StringUtils.join(phrasewordstring, " ");
                startActivity(new Intent(WalletcreateonestepActivity.this, WalletcreatetowstepActivity.class).putExtra("phrasewords",phrasewordss.toString()));
            }
        });
    }
    //创建助记词
    public String []  createprhase(){
        CloudchatApp cloudchatapp = new CloudchatApp();
        MasterWalletManager mWalletManager = cloudchatapp.getwalletmanager();
        System.out.println("对象"+mWalletManager);
        String phraseword = mWalletManager.GenerateMnemonic("english");
        phrasearr = phraseword.split(" ");
        return phrasearr;
    }
    public void back(View view){
        finish();
    }
}
