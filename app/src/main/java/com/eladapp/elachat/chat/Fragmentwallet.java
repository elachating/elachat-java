package com.eladapp.elachat.chat;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eladapp.elachat.R;
import com.eladapp.elachat.application.CloudchatApp;
import com.eladapp.elachat.utils.HttppostUtil;
import com.eladapp.elachat.walletspv.AssetdetailActivity;
import com.eladapp.elachat.walletspv.AssetmenurActivity;
import com.eladapp.elachat.walletspv.WalletcreateonestepActivity;
import com.eladapp.elachat.walletspv.WalletcreatetowstepActivity;
import com.elastos.spvcore.IMasterWallet;

import org.apache.commons.lang.StringUtils;
import org.ela.Elaspv.Elaspvapi;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.net.Proxy.Type.HTTP;

public class Fragmentwallet extends Fragment {
    //Button addwalletbtn;
    LinearLayout asset_bg;
    LinearLayout asset_sum_line;
    TextView assetsum;
    TextView token_ela_sum;
    ImageView menupopmainasset;
    TextView elasum;
    LinearLayout asset_ela;
    Handler handler;
    private String curprice=null;
    private CloudchatApp cloudchatapp;
    private  ArrayList<IMasterWallet> mainmasterwallet;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_wallet, container,false);
        handler=new Handler();
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        menupopmainasset = (ImageView)getView().findViewById(R.id.menupopmainasset);
        menupopmainasset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopDialog(v);
            }
        });
        asset_ela.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AssetdetailActivity.class).putExtra("assetname","ELA"));
            }
        });
    }
    protected void initView() {
        asset_bg = (LinearLayout)getView().findViewById(R.id.asset_bg);
        asset_sum_line = (LinearLayout)getView().findViewById(R.id.asset_sum_line);
        assetsum = (TextView)getView().findViewById(R.id.assetsum);
        asset_ela = (LinearLayout)getView().findViewById(R.id.asset_ela);
        token_ela_sum = (TextView)getView().findViewById(R.id.token_ela_sum);
        elasum = (TextView)getView().findViewById(R.id.elasum);
        //判断是否有钱包
         cloudchatapp = new CloudchatApp();
        mainmasterwallet =  cloudchatapp.getwalletlist();
        System.out.println("主钱包:"+mainmasterwallet.toString());
        if(mainmasterwallet==null || mainmasterwallet.toString().equals("[]")){
            asset_bg.setVisibility(View.GONE);
            asset_sum_line.setVisibility(View.GONE);
        }else{
            asset_bg.setVisibility(View.VISIBLE);
            asset_sum_line.setVisibility(View.VISIBLE);
            assetsum.setText("总资产：≈ "+getblance());
            asset_ela.setVisibility(View.VISIBLE);
            token_ela_sum.setText(getblance());
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
    }
    public void openPopDialog(View view) {
        Intent intent01=new Intent();
        intent01.setClass(getActivity(),AssetmenurActivity.class);
        startActivity(intent01);
    }
    //获取钱余额
    public String getblance(){
        ArrayList<IMasterWallet> Fmastwallet = cloudchatapp.getwalletlist();
        BigDecimal balance =BigDecimal.valueOf(Fmastwallet.get(0).GetSubWallet("ELA").GetBalance()).divide(BigDecimal.valueOf(100000000));
        //System.out.println("资产首页余额："+balance);
        //long balance = Fmastwallet.get(0).GetSubWallet("ELA").GetBalance()/100000000;
       // BigDecimal balance =BigDecimal.valueOf(Fmastwallet.get(0).GetSubWallet("ELA").GetBalance()).divide(BigDecimal.valueOf(100000000));
        return String.valueOf(balance);
    }

    private String getwebinfo(URL url) {
        String price = "";
        try {
            //2,开水闸--openConnection
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
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
                price =  jsonobjb.get("close").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("链接错误："+e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("异常错误："+e.getMessage());
        }
        return price;
    }
    // 构建Runnable对象，在runnable中更新界面
    Runnable  runnableUi=new  Runnable(){
        @Override
        public void run() {
            //更新界面
          assetsum.setText("≈ $"+String.valueOf(Double.valueOf(curprice)*Double.valueOf(getblance())));
          elasum.setText("$"+String.valueOf(Double.valueOf(curprice)*Double.valueOf(getblance()))+"(huobi)");
        }
    };
}