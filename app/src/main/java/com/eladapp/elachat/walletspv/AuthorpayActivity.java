package com.eladapp.elachat.walletspv;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eladapp.elachat.R;
import com.eladapp.elachat.db.Db;
import com.eladapp.elachat.utils.StreamTools;

import net.sf.json.JSONObject;

import org.ela.Carrier.Chatcarrier;
import org.elastos.carrier.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class AuthorpayActivity extends AppCompatActivity{
    private String callbackurl = "";
    private String didid = "";
    private String appid = "";
    private String appname = "";
    private String amount = "";
    private String paytitle = "";
    private String  receivewalletaddress= "";
    private String sign = "";
    private String appcate = "";
    private String state = "";
    private String pubkey = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authordid);
        Uri uridata = this.getIntent().getData();
        /**
         *  形式上为：elachatapp://walletpay?callbackurl=&did=&appid=&appname=&amount=&paytitle=&receivewalletaddress=&sign=&cate=&pubkey=&state=
         */
        String authorurl = uridata.toString();
        String [] urla = authorurl.split("\\?");
        String [] urlb = urla[1].split("&");
        //参数 callbackurl 为支付成功后回调地址，支付信息会推送给该地址，网站应用将支付信息更新到自己数据库中；
        String [] callbackurlarr = urlb[0].split("=");
        callbackurl = URLDecoder.decode(callbackurlarr[1].toString());
        //参数 did 是指网站本身的DID，可以通过Elastos提供的方式获取，也可以通过elachat提供的工具获取；
        String [] didarr = urlb[1].split("=");
        didid = didarr[1].toString();
        //参数 apppid 是指在elachat申请时,分配的APPID；
        String [] appidarr = urlb[2].split("=");
        appid = appidarr[1].toString();
        //参数 appname 是指网站应用在elachat显示的应用名称；
        String [] appnamearr = urlb[3].split("=");
        appname = appnamearr[1].toString();
        //参数 amount  是指支付的金额
        String [] amountarr = urlb[4].split("=");
        amount = amountarr[1].toString();
        //参数 paytitle 是指购买物品的描述信息
        String [] paytitlearr = urlb[5].split("=");
        paytitle = paytitlearr[1].toString();
        //参数 receivewalletaddress 是指网站应用提供的接收钱包地址
        String [] receivewalletaddressarr = urlb[6].split("=");
        receivewalletaddress = receivewalletaddressarr[1].toString();
        //参数 sign 是指采用did签名方式处理的did+appid+appname+amount+paytitle+receivewalletaddress 的签名信息
        String [] signarr = urlb[7].split("=");
        sign = signarr[1].toString();
        //参数 cate 是指应用的类型，与授权信息的cate一致
        String [] appcatearr = urlb[8].split("=");
        appcate = appcatearr[1].toString();
        //参数 pubkey 是指did的公钥，该公钥用于did的验签
        String [] pubkeyarr = urlb[9].split("=");
        pubkey = pubkeyarr[1].toString();
        //参数 state  是指自定义参数
        String [] statearr = urlb[10].split("=");
        pubkey = statearr[1].toString();










    }
}