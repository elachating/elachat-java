package com.eladapp.elachat.walletspv;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.eladapp.elachat.R;
import com.eladapp.elachat.application.CloudchatApp;
import com.elastos.spvcore.IMasterWallet;
import com.elastos.spvcore.ISubWallet;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.math.NumberUtils;
import java.math.BigDecimal;
import java.util.ArrayList;

public class AssetsendActivity extends AppCompatActivity{
    private String assetname;
    private Button btn_send_asset;
    private EditText toassetadr;
    private EditText paysum;
    private EditText mono;
    private CloudchatApp cloudchatapp;
    private boolean ischeck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        assetname = this.getIntent().getStringExtra("assetname");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_send);
        btn_send_asset = (Button)findViewById(R.id.btn_send_asset);
        toassetadr = (EditText)findViewById(R.id.toassetadr);
        paysum = (EditText)findViewById(R.id.paysum);
        mono = (EditText)findViewById(R.id.mono);
        btn_send_asset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long fee;
                if(checkisnull()){
                    //手续费
                    double num;
                    java.text.DecimalFormat myformat=new java.text.DecimalFormat("#0.00000000");
                    num=Double.parseDouble(paysum.getText().toString());
                    num=Double.parseDouble(myformat.format(num));
                    BigDecimal d1 = BigDecimal.valueOf(num);
                    BigDecimal d2 = BigDecimal.valueOf(100000000d);
                    BigDecimal d3 = d1.multiply(d2);
                    System.out.print(d3.toBigInteger().longValue());
                    long amount =  d3.toBigInteger().longValue();
                    //long amount =  Long.valueOf(String.valueOf(BigDecimal.valueOf(Long.parseLong(paysum.getText().toString())).multiply(BigDecimal.valueOf(100000000))));
                    try{
                        //fee = getfee(toassetadr.getText().toString(),amount,mono.getText().toString());
                        Intent intent01=new Intent();
                        intent01.setClass(AssetsendActivity.this,AssetsendconfirmActivity.class);
                        startActivity(intent01.putExtra("toaddress",toassetadr.getText().toString()).putExtra("amount",paysum.getText().toString()).putExtra("mono",mono.getText().toString()).putExtra("assetname",assetname));
                    }catch (Exception e){
                        System.out.println("错误："+e.getMessage().toString());
                        JSONObject jsonobj = JSONObject.fromObject(e.getMessage());
                        if(jsonobj.get("Message").equals("Available token is not enough")){
                            if(getlangconfig().equals("cn")){
                                Toast.makeText(AssetsendActivity.this,"余额不足.", Toast.LENGTH_SHORT).show();
                            }else if(getlangconfig().equals("en")){
                                Toast.makeText(AssetsendActivity.this,"token is not enough.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(AssetsendActivity.this,"余额不足.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }else{
                    //Toast.makeText(AssetsendActivity.this,"转账错误.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //检测是否为空
    public boolean checkisnull(){
        ischeck = true;
        if(toassetadr.getText().toString().equals("") || !toassetadr.getText().toString().substring(0,1).equals("E") || toassetadr.getText().toString().length()!=34){

            if(getlangconfig().equals("cn")){
                Toast.makeText(AssetsendActivity.this,"接收Token地址错误.", Toast.LENGTH_SHORT).show();
            }else if(getlangconfig().equals("en")){
                Toast.makeText(AssetsendActivity.this,"Invalid receiving Token address.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(AssetsendActivity.this,"接收Token地址错误.", Toast.LENGTH_SHORT).show();
            }
            ischeck = false;
        }else{

            if(!NumberUtils.isNumber(paysum.getText().toString()) || paysum.getText().toString().equals("0")){
                if(getlangconfig().equals("cn")){
                    Toast.makeText(AssetsendActivity.this,"金额错误.", Toast.LENGTH_SHORT).show();
                }else if(getlangconfig().equals("en")){
                    Toast.makeText(AssetsendActivity.this,"Amount error.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AssetsendActivity.this,"金额错误.", Toast.LENGTH_SHORT).show();
                }
                ischeck = false;
            }

        }
        return ischeck;
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