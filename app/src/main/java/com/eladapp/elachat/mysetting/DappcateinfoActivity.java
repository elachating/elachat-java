package com.eladapp.elachat.mysetting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eladapp.elachat.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DappcateinfoActivity extends AppCompatActivity {
    DappcatelistAdapter dappcatelistadapter;
    private List<Map<String, String>> list=new ArrayList<Map<String,String>>();
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dappinfocate);
        initview();
        refresh();
    }
    public void initview(){
        listView = (ListView)findViewById(R.id.dappcatelist);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Map<String, String> catelist = ((Map<String, String>) listView.getItemAtPosition(position));
                    //startActivity(new Intent(DappcateinfoActivity.this, AdddappinfoActivity.class).putExtra("cateid", catelist.get("cateid")).putExtra("catename",catelist.get("catename")));
                     //数据是使用Intent返回
                    Intent intent = new Intent();
                    //把返回数据存入Intent
                    intent.putExtra("cateid", catelist.get("cateid"));
                    intent.putExtra("catename", catelist.get("catename"));
                    //设置返回数据
                    DappcateinfoActivity.this.setResult(RESULT_OK, intent);
                    //关闭Activity
                    DappcateinfoActivity.this.finish();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    protected void refresh(){
        Map<String, String> mapa = new HashMap<String, String>();
        Map<String, String> mapb = new HashMap<String, String>();
        Map<String, String> mapc = new HashMap<String, String>();
        mapa.put("cateid", "1");
        mapa.put("catename","游戏娱乐");
        mapb.put("cateid", "2");
        mapb.put("catename","新闻社区");
        mapc.put("cateid", "3");
        mapc.put("catename","其他");
        list.add(mapa);
        list.add(mapb);
        list.add(mapc);
        dappcatelistadapter = new DappcatelistAdapter(list, this);
        listView.setAdapter(dappcatelistadapter);

    }
    public void back(View view){
        finish();
    }
    public class DappcatelistAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<Map<String, String>> datas;

        public DappcatelistAdapter(List<Map<String, String>> datas, Context context) {
            this.datas = datas;
            this.context = context;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Map getItem(int position) {
            return datas.get(position);
        }

        public long getItemId(int position) {
            return position;
        }
        @SuppressLint("NewApi")
        public View getView(int i, View view, ViewGroup viewGroup) {
            final  ViewHolder vh;
            if (view == null){
                vh = new ViewHolder();
                view = LayoutInflater.from(context).inflate(R.layout.activity_dappinfocatelist, null);
                vh.cateid = (TextView) view.findViewById(R.id.cateid);
                vh.catename = (TextView)view.findViewById(R.id.catename);
                view.setTag(vh);
                vh.cateid.setText((String)datas.get(i).get("cateid"));
                vh.catename.setText((String)datas.get(i).get("catename"));
            } else {
                vh = (ViewHolder)view.getTag();
            }
            return view;
        }
        public final class ViewHolder
        {
            public TextView cateid;
            public TextView catename;
        }
    }
}