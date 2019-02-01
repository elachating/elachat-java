package com.eladapp.elachat.walletspv;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eladapp.elachat.R;
import com.eladapp.elachat.chat.NewFriendsListAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TxlistAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> datas;
    public TxlistAdapter(Context context , List<Map<String, String>> datas){
        this.context = context;
        this.datas = datas;
    }
    public final class ViewHolder
    {
        public LinearLayout txinfo;
        public ImageView txidimg;
        public TextView txidconfirmstatus;
        public TextView txamount;
        public TextView txtime;
    }
    @SuppressLint("NewApi")
    public View getView(int i, View view, ViewGroup viewGroup) {
        final TxlistAdapter.ViewHolder vh;
        if (view == null) {
            vh = new TxlistAdapter.ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.adapter_txlist_item, null);
            vh.txinfo = (LinearLayout) view.findViewById(R.id.txinfo);
            vh.txidimg = (ImageView) view.findViewById(R.id.txidimg);
            vh.txidconfirmstatus = (TextView)view.findViewById(R.id.txidconfirmstatus);
            vh.txamount = (TextView)view.findViewById(R.id.txamount);
            vh.txtime = (TextView)view.findViewById(R.id.txtime);
            view.setTag(vh);
            /*
            *
            *
            *   jsonObjectc.put("txhash", txhash);
                jsonObjectc.put("confirmstatus", confirmstatus);
                jsonObjectc.put("amount", Double.valueOf(amount) / 100000000);
                jsonObjectc.put("txtime", txtime);
                jsonObjectc.put("direction", direction);
                jsonObjectc.put("noid", j);
            *
            * */
            if(String.valueOf(datas.get(i).get("direction")).equals("Sent")){

                if(String.valueOf(datas.get(i).get("confirmstatus")).equals("Pending")){
                    vh.txidimg.setImageResource(R.drawable.up_gray);
                    vh.txidconfirmstatus.setText("确认中");
                }else if(String.valueOf(datas.get(i).get("confirmstatus")).equals("Confirmed")){
                    vh.txidimg.setImageResource(R.drawable.up_green);
                    vh.txidconfirmstatus.setText("已确认");
                }
                vh.txamount.setText("-"+String.valueOf(datas.get(i).get("amount"))+"ELA");
            }else if(String.valueOf(datas.get(i).get("direction")).equals("Received")){
                if(String.valueOf(datas.get(i).get("confirmstatus")).equals("Pending")){
                    vh.txidimg.setImageResource(R.drawable.down_gray);
                    vh.txidconfirmstatus.setText("确认中");
                }else if(String.valueOf(datas.get(i).get("confirmstatus")).equals("Confirmed")){
                    vh.txidimg.setImageResource(R.drawable.down_green);
                    vh.txidconfirmstatus.setText("已确认");
                }
                vh.txamount.setText("+"+String.valueOf(datas.get(i).get("amount"))+"ELA");
            }
            SimpleDateFormat time=new SimpleDateFormat("HH:mm:ss YYYY-MM-dd");
            //System.out.println(time.format(System.currentTimeMillis()));
            vh.txtime.setText(String.valueOf(time.format(new Date(Long.valueOf(String.valueOf(datas.get(i).get("txtime")+"000"))))));
        }
        return view;
    }
    public Map getItem(int position) {
        return datas.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    public int getCount() {
        return datas.size();
    }
}