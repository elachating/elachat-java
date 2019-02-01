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
import com.elastos.spvcore.IDidManager;
import com.elastos.spvcore.IMasterWallet;
import com.elastos.spvcore.ISubWallet;
import com.elastos.spvcore.MasterWalletManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AssetsendconfirmActivity extends Activity implements View.OnClickListener {
    private String assetname;
    private Button sendassetbtn;
    private EditText paypassword;
    private String toaddress;
    private String amount;
    private String momo;
    private  CloudchatApp cloudchatapp;

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
        assetname = this.getIntent().getStringExtra("assetname");
        toaddress = this.getIntent().getStringExtra("toaddress");
        amount = this.getIntent().getStringExtra("amount");
        momo = this.getIntent().getStringExtra("mono");
        System.out.println("收币地址："+toaddress);
        rootpath = getApplicationContext().getFilesDir().getParent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_pop);
        initView();
        sendassetbtn.setOnClickListener(this);
    }

    private void initView(){
        sendassetbtn = (Button)findViewById(R.id.sendassetbtn);
        paypassword = (EditText) findViewById(R.id.paypwd);
    }

    @Override
    public void onClick(View v) {
        System.out.println("点击："+v.getId());
        switch (v.getId()) {
            case R.id.sendassetbtn:
                //发送交易
                System.out.println("发送交易：");
                double num;
                java.text.DecimalFormat myformat=new java.text.DecimalFormat("#0.00000000");
                num=Double.parseDouble(amount);
                num=Double.parseDouble(myformat.format(num));
                BigDecimal d1 = BigDecimal.valueOf(num);
                BigDecimal d2 = BigDecimal.valueOf(100000000d);
                BigDecimal d3 = d1.multiply(d2);
                System.out.print(d3.toBigInteger().longValue());
                long amounts =  d3.toBigInteger().longValue();
                String txid = transaction("ELA", paypassword.getText().toString(), toaddress, amounts, momo);
                System.out.println("交易ID："+txid);
                cloudchatapp.initwallet(rootpath);
                Intent intent = new Intent();
                intent.setClass(AssetsendconfirmActivity.this, ElachatActivity.class);
                startActivity(intent.putExtra("id", 2));
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
            String transaction = subWallet.CreateTransaction("", toaddress, amount, momo, "");
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