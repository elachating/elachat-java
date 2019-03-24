package com.eladapp.elachat.mysetting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import org.ela.Carrier.Chatcarrier;
import com.eladapp.elachat.R;
import com.eladapp.elachat.db.Db;
import com.eladapp.elachat.utils.CommonDialog;
import com.eladapp.elachat.utils.ImageLoaders;
import com.eladapp.elachat.utils.StreamTools;

import java.util.Map;

public class DappinfosAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> datas;
    // public ImageLoader imageLoader;
    public DappinfosAdapter(Context context ,  List <Map<String, String>> datas){
        this.context = context;
        this.datas = datas;
        //imageLoader=new ImageLoader(context);
    }
    public Map getItem(int position) {
        return datas.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    /**
     * get count of messages
     */
    public int getCount() {
        return datas.size();
    }
    @SuppressLint("NewApi")
    public View getView(int i, View view, ViewGroup viewGroup) {
        final  ViewHolder vh;
        if (view == null){
            vh = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.activity_setting_dapplist_item, null);
            vh.dappimg = (ImageView)view.findViewById(R.id.dappinfo_img);
            vh.dappname = (TextView)view.findViewById(R.id.dappinfo_appname);
            vh.dappstatus = (TextView)view.findViewById(R.id.dappinfo_status);
            vh.dappid = (TextView) view.findViewById(R.id.dappinfo_appid);
            vh.dappinfoview = (Button) view.findViewById(R.id.dappinfo_viewinfo);
            vh.dappinfodelbtn = (Button) view.findViewById(R.id.dappinfo_delbtn);
            vh.dappinfomenu = (Button) view.findViewById(R.id.dappinfo_menulist);
            vh.dappdidinfo = (Button) view.findViewById(R.id.dappinfo_didinfo);
            vh.dappinfoid = (TextView) view.findViewById(R.id.dappinfo_dappid);
            view.setTag(vh);
            vh.dappinfoid.setText((String)datas.get(i).get("appid"));
            vh.dappimg.setImageBitmap(Base64ToBitmap((String)datas.get(i).get("images")));
            vh.dappname.setText((String)datas.get(i).get("appname"));
            vh.dappid.setText((String)datas.get(i).get("appid"));
            if(datas.get(i).get("did").equals("")){
                vh.dappstatus.setText("状态：未设置DID信息");
            }
            if(datas.get(i).get("menujson").equals("")){
                vh.dappstatus.setText("状态：未设置DAPP菜单信息");
            }
        } else {
            vh = (ViewHolder)view.getTag();
        }
        vh.dappinfomenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, DappmenusetActivity.class).putExtra("dappid", vh.dappinfoid.getText().toString()));
            }
        });
        vh.dappdidinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // context.startActivity(new Intent(context, DappmenusetActivity.class).putExtra("dappid", vh.dappinfoid.getText().toString()).putExtra("did", (String)datas.get(i).get("did")));
                if(datas.get(i).get("did").equals("")){
                    String message = "本应用不保存DID私钥信息，私钥信息只出现一次，非常重要，请一定要注意保存.";
                    initDialog(message,vh.dappinfoid.getText().toString());
                }else{
                    context.startActivity(new Intent(context, DappdidinfoActivity.class).putExtra("dappid", vh.dappinfoid.getText().toString()).putExtra("didid", "1"));
                }
            }
        });
        vh.dappinfodelbtn.setOnClickListener(new View.OnClickListener(){
             @Override
             public void onClick(View view) {
                 delDialog("确定删除该DAPP应用？",vh.dappinfoid.getText().toString(),i);
             }
        });
        return view;
    }
    //Base64转化为图片
    public Bitmap Base64ToBitmap(String str){
        byte[] bytes = Base64.decode(str,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }
    private void initDialog(String message,String dappid) {
        final CommonDialog dialog = new CommonDialog(this.context);
        dialog.setMessage(message)
                .setImageResId(R.mipmap.ic_launcher)
                .setTitle("信息提示")
                .setNegtive("取消")
                .setPositive("确定")
                .setSingle(false)
                .setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        dialog.dismiss();
                        context.startActivity(new Intent(context, DappdidinfoActivity.class).putExtra("dappid", dappid).putExtra("didid", "0"));
                    }
                    @Override
                    public void onNegtiveClick() {
                        dialog.dismiss();
                        //Toast.makeText(getActivity(),"跳转",Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }
    private void delDialog(String message,String dappids,int h) {
        final CommonDialog dialog = new CommonDialog(this.context);
        dialog.setMessage(message)
                .setImageResId(R.mipmap.ic_launcher)
                .setTitle("信息提示")
                .setNegtive("取消")
                .setPositive("确定")
                .setSingle(false)
                .setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        dialog.dismiss();
                        //删除本地数据库的DAPP
                        Db db = new Db();
                        db.deldappdidinfo(dappids);
                        //删除服务器端的DAPP
                        String dappurl = "http://test.eladevp.com/index.php/Home/Dapplist/deldapp";
                        deldappinfo(dappurl,dappids);
                        removeitem(h);
                    }
                    @Override
                    public void onNegtiveClick() {
                        dialog.dismiss();
                    }
                }).show();
    }
    public void removeitem(int index) {
        if(index<0){
            return;
        } else {
            datas.remove(index);
        }
        notifyDataSetChanged();
    }
    //
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            System.out.println("返回结果:"+msg.toString());
            if (msg.obj.equals("deldappinfo")) {
                if(msg.what==1){
                    Bundle b = msg.getData();
                    String res = b.getString("res");
                    if(res.equals("1")){
                        //finish();
                    }
                }else{

                }
            }
        }
    };
    //构建POST方法获取指定的DAPP信息列表
    public void deldappinfo(String dappurl,String appids){
        new Thread() {
            public void run() {
                try {
                    URL url = new URL(dappurl);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(5000);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    String data = "{\"dappid\":\""+ appids +"\"}";
                    conn.setRequestProperty("Content-Length",String.valueOf(data.getBytes().length));
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
                        msg.obj = "deldapp";
                        handler.sendMessage(msg);
                    } else {
                        Message msg = Message.obtain();
                        msg.what = 0;
                        msg.obj = "deldapp";
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Message msg = Message.obtain();
                    System.out.println("错误："+e.getMessage());
                    msg.what = 0;
                    msg.obj = "deldapp";
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    public final class ViewHolder
    {
        public ImageView dappimg;
        public TextView dappname;
        public TextView dappstatus;
        public TextView dappid;
        public Button dappinfoview;
        public Button dappinfodelbtn;
        public Button dappinfomenu;
        public Button dappdidinfo;
        public TextView dappinfoid;
    }
}