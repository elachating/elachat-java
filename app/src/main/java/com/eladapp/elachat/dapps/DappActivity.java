package com.eladapp.elachat.dapps;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eladapp.elachat.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class DappActivity extends AppCompatActivity{
    private WebView webView;
    private ProgressBar progressBar;
    private String jumpurl;
    private String appname="";
    private TextView dappurl_site_name;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dapp);
        initview();
        /*Uri uridata = this.getIntent().getData();
        String authorurl = uridata.toString();
        String [] urla = authorurl.split("\\?");
        String [] urlb = urla[1].split("&");
        //获取回调地址
        String [] callbackurlarr = urlb[0].split("=");
        jumpurl = URLDecoder.decode(callbackurlarr[1].toString());
        //获取DID信息
        String [] didarr = urlb[1].split("=");
        //URLEncoder.encode(author_app_did,"utf-8")
        try {
            appname = URLDecoder.decode(didarr[1].toString(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        String appname = this.getIntent().getStringExtra("dappname");
        String jumpurl = this.getIntent().getStringExtra("jumpurl");
        dappurl_site_name.setText(appname);
        webView.loadUrl(jumpurl);
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
        //支持屏幕缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
    }
    public void initview(){
        progressBar= (ProgressBar)findViewById(R.id.progressbar);
        webView = (WebView) findViewById(R.id.webview);
        dappurl_site_name = (TextView)findViewById(R.id.dappurl_site_name);
    }
    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient=new WebViewClient(){
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.equals("http://www.google.com/")){
                Toast.makeText(DappActivity.this,"国内不能访问google,拦截该url",Toast.LENGTH_LONG).show();
                return true;//表示我已经处理过了
            }
            if(url.startsWith("https") || url.startsWith("http")){
                view.loadUrl(url);
                return true;
            }else{
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                } catch (ActivityNotFoundException e) {
                    // TODO: handle exception
                }
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

    };
    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient=new WebChromeClient(){
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定",null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();

            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("ansen","是否有上一个页面:"+webView.canGoBack());
        if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK){//点击返回按钮的时候判断有没有上一页
            webView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    /**
     * JS调用android的方法
     * @param str
     * @return
     */
    @JavascriptInterface //仍然必不可少
    public void  getClient(String str){
        Log.i("ansen","html调用客户端:"+str);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        webView.destroy();
        webView=null;
    }
    public void back(View view){
        finish();
    }
}