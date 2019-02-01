package com.eladapp.elachat.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.eladapp.elachat.R;
import java.util.Map;
import java.util.List;

public class ContactListAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> datas;

    public ContactListAdapter(Context context ,  List <Map<String, String>> datas){
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
        ViewHolder vh;
        if (view == null){
            vh = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.adapter_friendlist_item, null);
            vh.id = (TextView)view.findViewById(R.id.name);
            vh.remarks = (TextView)view.findViewById(R.id.remark);
            vh.favatar = (ImageView)view.findViewById(R.id.favatar);
            vh.favatar_gray = (ImageView)view.findViewById(R.id.favatar_gray);
            view.setTag(vh);
        } else {
            vh = (ViewHolder)view.getTag();
        }
        vh.id.setText(datas.get(i).get("userid"));
        if(datas.get(i).get("remark").equals("") || datas.get(i).get("remark").equals("离线")) {
            vh.favatar.setVisibility(View.GONE);
            vh.favatar_gray.setVisibility(View.VISIBLE);
            vh.remarks.setText("离线");
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            vh.favatar_gray.setColorFilter(filter);
        }else{
            vh.favatar_gray.setVisibility(View.GONE);
            vh.favatar.setVisibility(View.VISIBLE);
            vh.remarks.setText(datas.get(i).get("remark"));
        }
        return view;
    }
    public final class ViewHolder
    {
        public TextView id;
        public TextView remarks;
        public ImageView favatar;
        public ImageView favatar_gray;
    }
}

