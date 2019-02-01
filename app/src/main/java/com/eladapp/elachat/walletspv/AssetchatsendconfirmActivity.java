package com.eladapp.elachat.walletspv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.eladapp.elachat.ElachatActivity;
import com.eladapp.elachat.R;
import com.eladapp.elachat.application.CloudchatApp;
import com.eladapp.elachat.chat.ChatActivity;
import com.elastos.spvcore.IDidManager;
import com.elastos.spvcore.IMasterWallet;
import com.elastos.spvcore.ISubWallet;
import com.elastos.spvcore.MasterWalletManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AssetchatsendconfirmActivity extends Activity implements View.OnClickListener {
    private String assetname;
    private Button sendassetbtn;
    private EditText paypassword;
    private String toaddress;
    private String touid;
    private EditText amount;
    private EditText momo;
    private CloudchatApp cloudchatapp;

    private IMasterWallet mCurrentMasterWallet;
    private IDidManager mDidManager = null;
    private MasterWalletManager mWalletManager;
    private ArrayList<IMasterWallet> mMasterWalletList = new ArrayList<IMasterWallet>();
    private Map<String, ISubWallet> mSubWalletMap = new HashMap<String, ISubWallet>();
    private IMasterWallet masterWallet;
    private ISubWallet subWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        toaddress = this.getIntent().getStringExtra("toaddress");
        touid = this.getIntent().getStringExtra("touid");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_sendasset);
        initView();
    }
    private void initView(){
        sendassetbtn = (Button)findViewById(R.id.chatsendassetbtn);
        paypassword = (EditText) findViewById(R.id.paypwds);
        amount = (EditText) findViewById(R.id.payamount);
        momo = (EditText) findViewById(R.id.paymonos);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendassetbtn:
                //发送交易
                String txid = transaction("ELA", paypassword.getText().toString(), toaddress, Long.parseLong(amount.getText().toString()) * 10000 * 10000, momo.getText().toString());
                Intent intent = new Intent();
                intent.setClass(AssetchatsendconfirmActivity.this, ChatActivity.class);
                startActivity(intent.putExtra("friendId", touid));
            default:
                finish();
                break;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        finish();
        return true;
    }
    //获取发送交易地址
    public String getadr(){
        cloudchatapp = new CloudchatApp();
        ArrayList<IMasterWallet> Fmastwallet = cloudchatapp.getwalletlist();
        String adrinfo = Fmastwallet.get(0).GetSubWallet("ELA").GetAllAddress(0,1).toString();
        JSONObject jsonobj = JSONObject.fromObject(adrinfo);
        JSONArray jsonobja = JSONArray.fromObject(jsonobj.get("Addresses"));
        String fromaddress = String.valueOf(jsonobja.get(0));
        return fromaddress;
    }
    //获取交易手续费
    public long getfee(String toaddress, long amount, String momo){
        cloudchatapp = new CloudchatApp();
        ArrayList<IMasterWallet> Fmastwallet = cloudchatapp.getwalletlist();
        ISubWallet subWallet = Fmastwallet.get(0).GetSubWallet("ELA");
        String fromaddress = getadr();
        String transaction = subWallet.CreateTransaction(fromaddress, toaddress, amount, momo, "");
        long fee = subWallet.CalculateTransactionFee(transaction, 10000);
        return fee;
    }
    //构建交易，直接发送
    public String transaction(String chainid, String paypassword, String toaddress, long amount, String momo) {
        cloudchatapp = new CloudchatApp();
        ArrayList<IMasterWallet> Fmastwallet = cloudchatapp.getwalletlist();
        ISubWallet subWallet = Fmastwallet.get(0).GetSubWallet("ELA");
        String fromaddress = getadr();
        String txid = "";
        try{
            String transaction = subWallet.CreateTransaction(fromaddress, toaddress, amount, momo, "");
            System.out.println("Start get fee :");
            long fee = subWallet.CalculateTransactionFee(transaction, 10000);
            String rawTransactiona = subWallet.UpdateTransactionFee(transaction, fee);
            String rawTransactionb = subWallet.SignTransaction(rawTransactiona,paypassword);
            txid = subWallet.PublishTransaction(rawTransactionb);
            System.out.println("Get transaction result：" + txid);
            System.out.println("End transaction");
        }catch (Exception e){
            e.printStackTrace();
        }
        return txid;
    }
}