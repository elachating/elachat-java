package com.eladapp.elachat.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Button;
import java.util.List;
import org.ela.Carrier.Chatcarrier;
import com.eladapp.elachat.R;
import com.eladapp.elachat.db.Db;
import java.util.Map;

public class NewFriendsListAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> datas;
    private Chatcarrier chatcarrier = new Chatcarrier();
    private Db db = new Db();
    public NewFriendsListAdapter(Context context ,  List <Map<String, String>> datas){
        this.context = context;
        this.datas = datas;
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
            view = LayoutInflater.from(context).inflate(R.layout.adapter_newfriend_item, null);
            vh.userid = (TextView)view.findViewById(R.id.userid);
            vh.hello = (TextView)view.findViewById(R.id.hello);
            vh.acceptbtn = (Button)view.findViewById(R.id.acceptbtn);
            vh.accepted = (TextView)view.findViewById(R.id.accepted);
            view.setTag(vh);
            vh.userid.setText((String)datas.get(i).get("userid"));
            vh.hello.setText((String)datas.get(i).get("hello"));
            if(datas.get(i).get("yn").equals("0")){
                vh.acceptbtn.setVisibility(View.VISIBLE);
                vh.accepted.setVisibility(View.GONE);
            }else{
                vh.acceptbtn.setVisibility(View.GONE);
                vh.accepted.setVisibility(View.VISIBLE);
            }
        } else {
            vh = (ViewHolder)view.getTag();
        }
        vh.acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vh.acceptbtn.setVisibility(View.GONE);
                vh.accepted.setVisibility(View.VISIBLE);
                chatcarrier.acceptfriend(vh.userid.getText().toString());
                db.updatenewfriend(vh.userid.getText().toString());
            }
        });
        return view;
    }
    public final class ViewHolder
    {
        public TextView id;
        public TextView userid;
        public TextView hello;
        public Button acceptbtn;
        public TextView accepted;
    }
}

