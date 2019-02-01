package com.eladapp.elachat.walletspv;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.eladapp.elachat.R;
import com.eladapp.elachat.application.CloudchatApp;
import com.elastos.spvcore.IMasterWallet;
import com.elastos.spvcore.ISubWallet;

import net.sf.json.JSONObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetdetailActivity extends AppCompatActivity{
    private LinearLayout assetskbtn;
    private LinearLayout assetzzbtn;
    private TextView token_sum;
    private String assetname;
    private TextView asset_sum;
    private CloudchatApp cloudchatapp;
    private IMasterWallet mCurrentMasterWallet;
    private ArrayList<IMasterWallet> mMasterWalletList = new ArrayList<IMasterWallet>();
    private ListView txlist;
    Handler handler;
    private String curprice="";
    List<Map<String, String>> listmap=new ArrayList<Map<String,String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        assetname = this.getIntent().getStringExtra("assetname");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_detail);
        handler=new Handler();
        assetzzbtn = (LinearLayout)findViewById(R.id.asset_zz_btn);
        assetskbtn = (LinearLayout)findViewById(R.id.asset_sk_btn);
        token_sum = (TextView)findViewById(R.id.token_sum);
        txlist = (ListView) findViewById(R.id.txlist);
        asset_sum = (TextView)findViewById(R.id.asset_sum);
        token_sum.setText(getblance());
        assetzzbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AssetdetailActivity.this, AssetsendActivity.class).putExtra("assetname","ELA"));
            }
        });
        assetskbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AssetdetailActivity.this, AssetpayActivity.class).putExtra("assetname","ELA"));
            }
        });
        //System.out.println("交易记录列表："+transactionlist().toString());
        List<Map<String, String>> list = new ArrayList<Map<String,String>>();
        list = transactionlist();
        TxlistAdapter txlistadapter = new TxlistAdapter(this, list);
        txlist.setAdapter(txlistadapter);


        final String path = "http://ela.chat/quota/op.php?parm=detail&maincoin=usdt&subcoin=ela";
        URL urls = null;
        try {
             urls = new URL(path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        final URL finalUrls = urls;

        new Thread(){
            public void run(){
                curprice = getwebinfo(finalUrls);
                handler.post(runnableUi);
            }
        }.start();
    }
    private String getwebinfo(URL url) {
        String price = "";
        try {
            //1,找水源--创建URL
            //URL url = new URL("https://www.baidu.com/");//放网站
            //2,开水闸--openConnection
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            System.out.println("链接信息："+httpURLConnection.toString());
            System.out.println("链接信息："+httpURLConnection.getResponseMessage());
            //httpURLConnection.getResponseMessage();
            //3，建管道--InputStream
            InputStream inputStream = null;
            int code = httpURLConnection.getResponseCode();
            if (code == 200) {
                 inputStream = httpURLConnection.getInputStream();
            } else {
                 inputStream = httpURLConnection.getErrorStream(); // 得到网络返回的输入流
            }

            //4，建蓄水池蓄水-InputStreamReader
            InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
            //5，水桶盛水--BufferedReader
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuffer buffer = new StringBuffer();
            String temp = null;

            while ((temp = bufferedReader.readLine()) != null) {
                //取水--如果不为空就一直取
                buffer.append(temp);
            }
            bufferedReader.close();//记得关闭
            reader.close();
            inputStream.close();
            try {
                org.json .JSONObject jsonobja = new  org.json .JSONObject(buffer.toString());
                org.json .JSONObject jsonobjb = new  org.json .JSONObject(jsonobja.get("tick").toString());
                System.out.println("当前价格："+jsonobjb.get("close"));
                //asset_sum.setText("≈ $"+String.valueOf(Double.valueOf(jsonobjb.get("close").toString())*Double.valueOf(getblance()))+"(huobi)");
                price =  jsonobjb.get("close").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("MAIN",buffer.toString());//打印结果

        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("链接错误："+e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("异常错误："+e.getMessage());
        }
        return price;
    }


    //获取钱余额
    public String getblance(){
        cloudchatapp = new CloudchatApp();
        ArrayList<IMasterWallet> Fmastwallet = cloudchatapp.getwalletlist();
        BigDecimal balance =BigDecimal.valueOf(Fmastwallet.get(0).GetSubWallet("ELA").GetBalance()).divide(BigDecimal.valueOf(100000000));
        return String.valueOf(balance);
    }
    //获取交易记录

    //获得指定钱包地址的的交易记录
    public List<Map<String, String>>  transactionlist() {
        String transactionlists = "";
        cloudchatapp = new CloudchatApp();
        ArrayList<IMasterWallet> Fmastwallet = cloudchatapp.getwalletlist();
        ISubWallet subWallet = Fmastwallet.get(0).GetSubWallet("ELA");
        try {
            transactionlists = subWallet.GetAllTransaction(0, 100, "");
            org.json.JSONObject jsonObjectb = new org.json.JSONObject(transactionlists);
            org.json.JSONArray lista = jsonObjectb.getJSONArray("Transactions");
            for (int j = 0; j < lista.length(); j++) {
                Map<String, String> maps = new HashMap<String, String>();
                long amount = lista.getJSONObject(j).getLong("Amount");
                String confirmstatus = lista.getJSONObject(j).getString("Status");
                String direction = lista.getJSONObject(j).getString("Direction");
                String txtime = lista.getJSONObject(j).getString("Timestamp");
                String txhash = lista.getJSONObject(j).getString("TxHash");
                maps.put("shorttxhash", txhash.replaceAll("(\\d{4})\\d{54}(\\d{4})", "$1****$2"));
                maps.put("txhash", txhash);
                maps.put("confirmstatus", confirmstatus);
                maps.put("amount", String.valueOf(Double.valueOf(amount) / 100000000));
                maps.put("txtime", txtime);
                maps.put("direction", String.valueOf(direction));
                maps.put("noid", String.valueOf(j));
                System.out.println("MAP:"+maps.toString());
                listmap.add(maps);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listmap;
    }
    public void back(View view){
        finish();
    }
    // 构建Runnable对象，在runnable中更新界面
    Runnable  runnableUi=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            //asset_sum.setText("≈ $"+String.valueOf(Double.valueOf(curprice)*Double.valueOf(getblance())));
            asset_sum.setText("$"+String.valueOf(Double.valueOf(curprice)*Double.valueOf(getblance()))+"(huobi)");
        }

    };
}