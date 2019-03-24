package com.eladapp.elachat.dapps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import org.ela.Carrier.Chatcarrier;
import com.eladapp.elachat.R;
import com.eladapp.elachat.db.Db;
import com.eladapp.elachat.utils.ImageLoaders;

import java.util.Map;

public class DapplistAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> datas;
   // public ImageLoader imageLoader;
    public DapplistAdapter(Context context ,  List <Map<String, String>> datas){
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
            view = LayoutInflater.from(context).inflate(R.layout.adapter_dapplist_item, null);
            vh.dappimg = (ImageView)view.findViewById(R.id.dappimg);
            vh.dappname = (TextView)view.findViewById(R.id.dapp_name);
            vh.dappdesc = (TextView)view.findViewById(R.id.dapp_desc);
            vh.dappdid = (TextView)view.findViewById(R.id.dapp_did);
            vh.dappmenujson = (TextView)view.findViewById(R.id.dapp_menujson);
            view.setTag(vh);
            vh.dappname.setText((String)datas.get(i).get("dappname"));
            vh.dappdesc.setText((String)datas.get(i).get("dappdesc"));
            vh.dappdid.setText((String)datas.get(i).get("did"));
            vh.dappmenujson.setText((String)datas.get(i).get("menujson"));
            Map<String, String> p = new HashMap<String, String>();
            p = datas.get(i);
            ImageLoaders.getInstance(context).displayImage(vh.dappimg,(String)datas.get(i).get("dappimg"));
        } else {
            vh = (ViewHolder)view.getTag();
        }
        return view;
    }
    public final class ViewHolder
    {
        public ImageView dappimg;
        public TextView dappname;
        public TextView dappdesc;
        public TextView dappdid;
        public TextView dappmenujson;
    }
}