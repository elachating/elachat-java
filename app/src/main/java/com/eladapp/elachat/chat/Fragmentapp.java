package com.eladapp.elachat.chat;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eladapp.elachat.R;
import com.eladapp.elachat.dapps.DapplistActivity;
import com.eladapp.elachat.dapps.DapplistAdapter;
import com.eladapp.elachat.dapps.DappmainActivity;
import com.eladapp.elachat.mysetting.LicenseagreementActivity;
import com.eladapp.elachat.utils.CommonDialog;
import com.eladapp.elachat.utils.StreamTools;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragmentapp extends Fragment {
    private WebView webView;
    private ProgressBar progressBar;
    private View mContentView;
    private RollPagerView mRollViewPager;
    private ListView dapplistview_list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dapplist, container,false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initview();
        mRollViewPager.setPlayDelay(2000);
        mRollViewPager.setAnimationDurtion(1000);
        mRollViewPager.setAdapter(new TestNormalAdapter());
        mRollViewPager.setHintView(new ColorPointHintView(getContext(), Color.YELLOW, Color.WHITE));
        dapplistview_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Map<String, String> dapplistinfo = ((Map<String, String>) dapplistview_list.getItemAtPosition(position));
                    //System.out.println("DPPA列表："+dapplistinfo.toString());
                    //startActivity(new Intent(getActivity(), UserInfoActivity.class).putExtra("friendId", friendInfo.get("userid")).putExtra("curremark",friendInfo.get("remark")));
                    initDialog(dapplistinfo.get("dappname"),dapplistinfo.get("dappdid"),dapplistinfo.get("menujson"));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void initview(){
        mRollViewPager = (RollPagerView)getView().findViewById(R.id.roll_view_pager);
        dapplistview_list = (ListView)getView().findViewById(R.id.dapplistview_list);
        String url = "http://test.eladevp.com/index.php/Home/Dapplist/dapplist";
        getdapplist(url);
    }
    private void initDialog(String appname,String dappdid,String dappmenujson) {
        final CommonDialog dialog = new CommonDialog(getActivity());
        dialog.setMessage("该DAPP服务由【"+ appname +"】开发,且需提供以下授权信息：\n 1、昵称. \n 2、资产地址. \n 3、UserAddress.")
                .setImageResId(R.mipmap.ic_launcher)
                .setTitle("信息提示")
                .setNegtive("取消")
                .setPositive("确定")
                .setSingle(false)
                .setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                dialog.dismiss();
                //Toast.makeText(getActivity(),dappmenujson,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), DappmainActivity.class).putExtra("dappname", appname).putExtra("did",dappdid).putExtra("menujson",dappmenujson));
            }
            @Override
            public void onNegtiveClick() {
                dialog.dismiss();
                //Toast.makeText(getActivity(),"跳转",Toast.LENGTH_SHORT).show();
              }
        }).show();
    }
    //网络获取数据处理成适合LISTVIEW展示的数据
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.obj.equals("getdapplist")) {
                if(msg.what==1){
                    Bundle b = msg.getData();
                    List<Map<String, String>> list = new ArrayList<Map<String,String>>();
                    String res = b.getString("res");
                    JSONArray jsonArray= JSONArray.fromObject(res);
                    for(int i=0;i<jsonArray.size();i++){
                        Map<String, String> maps = new HashMap<String, String>();
                        JSONObject jsonObject= JSONObject.fromObject(jsonArray.get(i).toString());
                        maps.put("dappimg", jsonObject.get("images").toString());
                        maps.put("dappname", jsonObject.get("appname").toString());
                        maps.put("dappdesc", jsonObject.get("remark").toString());
                        maps.put("did", jsonObject.get("did").toString());
                        maps.put("menujson", jsonObject.get("menujson").toString());

                        list.add(maps);
                    }
                    //构建合适的数据展示形式
                   // List<Map<String, String>> list = new ArrayList<Map<String,String>>();
                    DapplistAdapter dapplistadapter = new DapplistAdapter(getContext(), list);
                   dapplistview_list.setAdapter(dapplistadapter);
                }else{

                }
            }
        }
    };
    //构建POST方法获取指定的DAPP信息列表
    public void getdapplist(String dappurl){
        new Thread() {
            public void run() {
                try {
                    URL url = new URL(dappurl);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(5000);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    String data = "{\"id\":\"1\"}";
                    conn.setRequestProperty("Content-Length",String.valueOf(data.length()));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(data.getBytes());
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream in = conn.getInputStream();
                        String content = StreamTools.readString(in);
                        Message msg = Message.obtain();
                        Bundle bundle = new Bundle();
                        System.out.println("DAPP列表："+content.toString());
                        bundle.putString("res",content.toString());
                        msg.setData(bundle);
                        msg.what = 1;
                        msg.obj = "getdapplist";
                        handler.sendMessage(msg);
                    } else {
                        Message msg = Message.obtain();
                        msg.what = 0;
                        msg.obj = "getdapplist";
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj = "getdapplist";
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    public class TestNormalAdapter extends StaticPagerAdapter {
        private int[] imgs = {
                R.drawable.banner1,
                R.drawable.banner2,
                R.drawable.banner3,
        };
        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }
        @Override
        public int getCount() {
            return imgs.length;
        }
    }
}